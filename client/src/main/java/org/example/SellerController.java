package org.example;

import io.grpc.ManagedChannel;
import org.example.grpc.GetItemListRequest;
import org.example.grpc.Item;
import org.example.grpc.SellerServiceGrpc;
import org.example.grpc.UpdateItemListRequest;

public class SellerController {
    private SellerServiceGrpc.SellerServiceBlockingStub sellerServiceStub;
    private SellerWindow sellerWindow;
    private String sessionId;
    private SellerItemTableModel model;

    public SellerController(ManagedChannel channel, String sessionId) {
        this.sessionId = sessionId;
        sellerServiceStub = SellerServiceGrpc.newBlockingStub(channel);
        initTableModel();
        sellerWindow = new SellerWindow(this, model);
        sellerWindow.setVisible(true);
    }

    private void initTableModel() {
        var itemListRes = sellerServiceStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sessionId).build());
        model = new SellerItemTableModel(itemListRes.getItemList());
    }

    public void saveItemList() {
        int rowCount = model.getRowCount();

        UpdateItemListRequest.Builder updateRequest = UpdateItemListRequest.newBuilder()
                .setSessionId(sessionId)  // Provide the actual session ID here
                .addAllItem(model.getItemList());

        sellerServiceStub.updateItemList(updateRequest.build());
        model.setItemList(sellerServiceStub.getItemList(GetItemListRequest.newBuilder().setSessionId(sessionId).build()).getItemList());
    }

    public void addNewItem() {
        model.addRow(Item.getDefaultInstance());
    }

    public void showProfitReport() {
        ProfitReportWindow reportWindow = new ProfitReportWindow(sellerServiceStub, sessionId);
        reportWindow.setVisible(true);
    }
}
