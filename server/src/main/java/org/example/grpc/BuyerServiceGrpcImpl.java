package org.example.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.data.model.User;
import org.example.service.BuyerService;
import org.example.service.SessionStore;
import org.example.utils.ProtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC implementation for handling buyer-related operations.
 */
@Service
public class BuyerServiceGrpcImpl extends BuyerServiceGrpc.BuyerServiceImplBase {
    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private BuyerService buyerService;

    /**
     * Retrieves the list of available items.
     *
     * @param request the request to get the item list.
     * @param responseObserver the response observer to send the item list.
     */
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

    /**
     * Retrieves the list of items in the buyer's cart.
     *
     * @param request the request to get the cart item list.
     * @param responseObserver the response observer to send the cart item list.
     */
    @Override
    public void getCartItemList(GetCartItemListRequest request, StreamObserver<GetCartItemListResponse> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        var cart = buyerService.getCart(user);

        responseObserver.onNext(GetCartItemListResponse.newBuilder().setCart(ProtoUtils.toProto(cart)).build());
        responseObserver.onCompleted();
    }

    /**
     * Adds items to the buyer's cart.
     *
     * @param request the request containing the item IDs to add.
     * @param responseObserver the response observer to confirm the addition of items to the cart.
     */
    @Override
    public void addItemToCart(AddItemRequest request, StreamObserver<Empty> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        buyerService.addItemsToCart(
                request.getItemIdList().stream().map(UUID::fromString).collect(Collectors.toSet()),
                user);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    /**
     * Updates the quantity of a specific item in the buyer's cart.
     *
     * @param request the request containing the item ID and the new quantity.
     * @param responseObserver the response observer to confirm the update.
     */
    @Override
    public void updateCart(UpdateCartRequest request, StreamObserver<Empty> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        buyerService.updateCart(UUID.fromString(request.getItemId()), user, request.getItemCount());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    /**
     * Deletes an item from the buyer's cart.
     *
     * @param request the request containing the item ID to be deleted.
     * @param responseObserver the response observer to confirm the deletion.
     */
    @Override
    public void deleteItemFromCart(DeleteItemRequest request, StreamObserver<Empty> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;
        buyerService.deleteItemFromCart(UUID.fromString(request.getItemId()), user);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    /**
     * Handles the checkout process for the buyer's cart.
     *
     * @param request the request containing checkout details, such as address and payment information.
     * @param responseObserver the response observer to send the order details upon successful checkout.
     */
    @Override
    public void checkout(CheckoutRequest request, StreamObserver<CheckoutResponse> responseObserver) {
        var user = checkSessionId(request.getSessionId(), responseObserver);
        if (user == null) return;

       var order =  buyerService.checkout(user, request.getAddress(), request.getCard());
       var checkoutResponse = CheckoutResponse.newBuilder().setOrder(ProtoUtils.toProto(order)).build();
        responseObserver.onNext(checkoutResponse);
        responseObserver.onCompleted();

    }

    /**
     * Validates the session ID and ensures the user has the role of BUYER.
     *
     * @param sessionIdStr the session ID to be validated.
     * @param responseObserver the response observer to handle error cases.
     * @return the user associated with the session if valid, otherwise null.
     */
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
}


