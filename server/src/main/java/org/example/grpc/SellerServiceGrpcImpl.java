package org.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.data.model.Item;
import org.example.service.SellerService;
import org.example.service.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SellerServiceGrpcImpl extends SellerServiceGrpc.SellerServiceImplBase {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private SessionStore sessionStore;

    @Override
    public void getItemList(ItemListRequest request, StreamObserver<ItemListResponse> responseObserver) {
        UUID sessionId = UUID.fromString(request.getSessionId());
        if (sessionStore.getRole(sessionId) != Role.SELLER) {
            System.err.println("This method is available for sellers only");
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("For sellers only").asRuntimeException());
            return;
        }

        ItemListResponse.Builder responseBuilder = ItemListResponse.newBuilder();
        sellerService.getItemList(sessionId)
                .stream()
                .map(this::toProto)
                .forEach(responseBuilder::addItem);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    org.example.grpc.Item toProto(Item item) {
        return org.example.grpc.Item.newBuilder()
                .setId(item.getId())
                .setName(item.getName())
                .setQuantity(item.getQuantity())
                .setInvoicePriceCents(item.getInvoicePriceCents())
                .setSellPriceCents(item.getSellPriceCents())
                .build();
    }

}
