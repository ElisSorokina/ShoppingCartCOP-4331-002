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
    public void getItemList(GetItemListRequest request, StreamObserver<GetItemListResponse> responseObserver) {
        var sessionId = checkSessionId(request.getSessionId(), responseObserver);
        if (sessionId == null) return;

        var responseBuilder = GetItemListResponse.newBuilder();
        sellerService.getItemList(sessionId)
                .stream()
                .map(this::toProto)
                .forEach(responseBuilder::addItem);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private UUID checkSessionId(String sessionIdStr, StreamObserver<?> responseObserver) {
        var sessionId = UUID.fromString(sessionIdStr);
        if (sessionStore.getRole(sessionId) != Role.SELLER) {
            System.err.println("This method is available for sellers only");
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("For sellers only").asRuntimeException());
            return null;
        }
        return sessionId;
    }

    @Override
    public void updateItemList(UpdateItemListRequest request, StreamObserver<Empty> responseObserver) {
        var sessionId = checkSessionId(request.getSessionId(), responseObserver);
        if (sessionId == null) return;
        var protoItemList = request.getItemList();
        var deletedItemIds = protoItemList.stream().filter(v -> v.getDeleted()).map(v -> v.getId()).map(UUID::fromString).collect(Collectors.toList());
        var itemList = protoItemList.stream().filter(v -> !v.getDeleted()).map(v -> toDomain(v, sessionId)).collect(Collectors.toList());
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

    org.example.grpc.Item toProto(Item item) {
        return org.example.grpc.Item.newBuilder()
                .setId(item.getId().toString())
                .setName(item.getName())
                .setQuantity(item.getQuantity())
                .setInvoicePriceCents(item.getInvoicePriceCents())
                .setSellPriceCents(item.getSellPriceCents())
                .build();
    }

    Item toDomain(org.example.grpc.Item protoItem, UUID sessionId) {
        var item = new Item();
        if(protoItem.getId() == null || protoItem.getId().isEmpty()) {
            item.setId(UUID.randomUUID());
        } else {
            item.setId(UUID.fromString(protoItem.getId()));
        }

        item.setName(protoItem.getName());
        item.setQuantity(protoItem.getQuantity());
        item.setInvoicePriceCents(protoItem.getInvoicePriceCents());
        item.setSellPriceCents(protoItem.getSellPriceCents());
        item.setSeller(sessionStore.getUser(sessionId));
        return item;
    }

}
