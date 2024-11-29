package org.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.service.SellerService;
import org.example.service.SessionStore;
import org.example.utils.ProtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC implementation for handling seller-related operations.
 */
@Service
public class SellerServiceGrpcImpl extends SellerServiceGrpc.SellerServiceImplBase {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private SessionStore sessionStore;

    /**
     * Retrieves the list of items for the seller.
     *
     * @param request the request to get the item list.
     * @param responseObserver the response observer to send the item list.
     */
    @Override
    public void getItemList(GetItemListRequest request, StreamObserver<GetItemListResponse> responseObserver) {
        var sessionId = checkSessionId(request.getSessionId(), responseObserver);
        if (sessionId == null) return;

        var responseBuilder = GetItemListResponse.newBuilder();
        sellerService.getItemList(sessionId)
                .stream()
                .map(ProtoUtils::toProto)
                .forEach(responseBuilder::addItem);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     * Updates the seller's item list.
     *
     * @param request the request containing updated item details.
     * @param responseObserver the response observer to confirm the update.
     */
    @Override
    public void updateItemList(UpdateItemListRequest request, StreamObserver<Empty> responseObserver) {
        var sessionId = checkSessionId(request.getSessionId(), responseObserver);
        if (sessionId == null) return;
        var protoItemList = request.getItemList();
        var deletedItemIds = protoItemList.stream().filter(v -> v.getDeleted()).map(v -> v.getId()).map(UUID::fromString).collect(Collectors.toList());
        var itemList = protoItemList.stream().filter(v -> !v.getDeleted()).map(v -> ProtoUtils.toDomain(v, sessionId , sessionStore)).collect(Collectors.toList());
        try {
            sellerService.updateItemList(itemList, deletedItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(Status.INTERNAL.withDescription("Unable to update items, check logs").asRuntimeException());
            return;
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    /**
     * Retrieves the profit report for the seller.
     *
     * @param request the request to get the profit report.
     * @param responseObserver the response observer to send the profit report.
     */
    @Override
    public void getProfitReport(GetProfitReportRequest request, StreamObserver<GetProfitReportResponse> responseObserver) {
        // Validate session
        var sessionId = checkSessionId(request.getSessionId(), responseObserver);
        if (sessionId == null) return;

        try {
            responseObserver.onNext(sellerService.getProfitReport(sessionId));
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(Status.INTERNAL.withDescription("Unable to fetch profit report").asRuntimeException());
        }
    }

    /**
     * Validates the session ID and ensures the user has the role of SELLER.
     *
     * @param sessionIdStr the session ID to be validated.
     * @param responseObserver the response observer to handle error cases.
     * @return the session ID if valid, otherwise null.
     */
    private UUID checkSessionId(String sessionIdStr, StreamObserver<?> responseObserver) {
        UUID sessionId = null;
        try {
            sessionId = UUID.fromString(sessionIdStr);
        } catch (Exception e) {
            System.err.println("Invalid session id or not specified");
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("Invalid session id").asRuntimeException());
            return null;
        }
        if (sessionStore.getRole(sessionId) != Role.SELLER) {
            System.err.println("This method is available for sellers only");
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("For sellers only").asRuntimeException());
            return null;
        }
        return sessionId;
    }

}
