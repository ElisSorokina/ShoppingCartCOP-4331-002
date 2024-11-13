package org.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.data.model.Cart;
import org.example.data.model.User;
import org.example.service.BuyerService;
import org.example.service.SessionStore;
import org.example.utils.ProtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuyerServiceGrpcImpl extends BuyerServiceGrpc.BuyerServiceImplBase {
    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private BuyerService buyerService;

    @Override
    public void getItemList(GetItemListRequest request, StreamObserver<GetItemListResponse> responseObserver) {
      var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;

        var responseBuilder = GetItemListResponse.newBuilder();
        buyerService.getItemList()
                .stream()
                .map(ProtoUtils::toProto)
                .forEach(responseBuilder::addItem);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCartItemList(GetCartItemListRequest request, StreamObserver<GetCartItemListResponse> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        var cart = buyerService.getCart(user);

        responseObserver.onNext(GetCartItemListResponse.newBuilder().setCart(ProtoUtils.toProto(cart)).build());
        responseObserver.onCompleted();
    }

    private User checkSessionId(String sessionIdStr, StreamObserver<?> responseObserver) {
        UUID sessionId = null;
        try {
            sessionId = UUID.fromString(sessionIdStr);
        } catch (Exception e) {
            System.err.println("Invalid session id or not specified");
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("Invalid session id").asRuntimeException());
            return null;
        }
        if (sessionStore.getRole(sessionId) != Role.BUYER) {
            System.err.println("This method is available for customers only");
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("For customers only").asRuntimeException());
            return null;
        }
        return sessionStore.getUser(sessionId);
    }

    @Override
    public void addItemToCart(AddItemRequest request, StreamObserver<Empty> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        buyerService.addItemToCart(UUID.fromString(request.getItemId()), user);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void updateCart(UpdateCartRequest request, StreamObserver<Empty> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        buyerService.updateCart(UUID.fromString(request.getItemId()), user, request.getItemCount());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteItemFromCart(DeleteItemRequest request, StreamObserver<Empty> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        buyerService.deleteItemFromCart(UUID.fromString(request.getItemId()), user);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void checkout(CheckoutRequest request, StreamObserver<CheckoutResponse> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;

    }
}


