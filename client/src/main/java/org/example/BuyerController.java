package org.example;

import io.grpc.ManagedChannel;
import org.example.grpc.*;

import java.util.List;

public class BuyerController {
    private BuyerServiceGrpc.BuyerServiceBlockingStub buyerServiceStub;
    private BuyerWindow buyerWindow;
    private String sessionId;
    private BuyerItemTableModel model;

    public BuyerController(ManagedChannel channel, String sessionId) {
        this.sessionId = sessionId;
        buyerServiceStub = BuyerServiceGrpc.newBlockingStub(channel);
        initTableModel();
        buyerWindow = new BuyerWindow(this, model);
        buyerWindow.setVisible(true);
    }

    private void initTableModel() {
        var itemListRes = buyerServiceStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sessionId).build());
        model = new BuyerItemTableModel(itemListRes.getItemList());
    }


    public void addToCart() {
        int rowCount = model.getRowCount();
        List<String> itemList = model.getSelectedIds();


        var addToCartReq = AddItemRequest.newBuilder()
                .setSessionId(sessionId)  // Provide the actual session ID here
                .addAllItemId(itemList)
                .build();

        buyerServiceStub.addItemToCart(addToCartReq);
        model.setItemList(buyerServiceStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sessionId).build()).getItemList());
    }
}
