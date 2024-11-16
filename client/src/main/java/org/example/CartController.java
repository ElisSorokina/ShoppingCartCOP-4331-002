package org.example;

import io.grpc.ManagedChannel;
import org.example.grpc.*;

import java.util.List;

public class CartController {
    private BuyerServiceGrpc.BuyerServiceBlockingStub buyerServiceBlockingStub;
    private CartWindow cartWindow;
    private String sessionId;
    private CartTableModel model;


    public CartController(ManagedChannel channel, String sessionId) {
        this.sessionId = sessionId;
        buyerServiceBlockingStub = BuyerServiceGrpc.newBlockingStub(channel);
    }

    public void openCart(){
        model = new CartTableModel(getCartItemList());
        cartWindow = new CartWindow(this, model);
        cartWindow.setVisible(true);
    }

    private List<CartEntry> getCartItemList() {
        GetCartItemListResponse cartItemList = buyerServiceBlockingStub.getCartItemList(GetCartItemListRequest.newBuilder().setSessionId(sessionId).build());
        return cartItemList.getCart().getCartEntriesList();
    }

    public void delete(){

        var deletedList = model.getDeleted();
        for (CartEntry cartEntry: deletedList) {
            buyerServiceBlockingStub.deleteItemFromCart(DeleteItemRequest.newBuilder().setSessionId(sessionId).setItemId(cartEntry.getItemId()).build());
        }
        model.setCartEntries(getCartItemList());
        model.fireTableDataChanged();
    }


}
