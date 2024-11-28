package org.example;

import org.example.grpc.GetProfitReportRequest;
import org.example.grpc.GetProfitReportResponse;
import org.example.grpc.SellerServiceGrpc;

import javax.swing.*;
import java.awt.*;

public class ProfitReportWindow extends JFrame {

    public ProfitReportWindow(SellerServiceGrpc.SellerServiceBlockingStub sellerServiceStub, String sessionId) {
        setTitle("Profit Report");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fetch profit report from server
        GetProfitReportResponse reportResponse = sellerServiceStub.getProfitReport(
                GetProfitReportRequest.newBuilder().setSessionId(sessionId).build()
        );

        // Build UI
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);

        // Populate report details
        reportArea.append("Total Profit: " + reportResponse.getTotalProfit() / 100.0 + "\n"); // Convert cents to dollars
        reportArea.append("Item-wise Profit:\n");
        reportResponse.getItemProfitsList().forEach(itemProfit ->
                reportArea.append(itemProfit.getItemName() + ": " + itemProfit.getProfit() / 100.0 + "\n")
        );

        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }
}