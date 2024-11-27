package org.example;

import io.grpc.ManagedChannel;
import org.example.grpc.BuyerServiceGrpc;
import org.example.grpc.Card;
import org.example.grpc.CheckoutRequest;

import io.grpc.ManagedChannel;
import org.example.grpc.BuyerServiceGrpc;

import java.awt.event.WindowListener;

public class CheckoutController {
    private final BuyerServiceGrpc.BuyerServiceBlockingStub buyerServiceStub;
    private final String sessionId;

    // Constructor expects a ManagedChannel, not a stub
    public CheckoutController(ManagedChannel channel, String sessionId) {
        this.buyerServiceStub = BuyerServiceGrpc.newBlockingStub(channel); // Initialize stub from channel
        this.sessionId = sessionId;
    }

    public void openCheckoutWindow(WindowListener windowListener) {
        CheckoutWindow checkoutWindow = new CheckoutWindow(this);
        checkoutWindow.setVisible(true);
        checkoutWindow.addWindowListener(windowListener);
    }

    public void processCheckout(String address, String cardNumber, int expYear, int expMonth, int cvv) {
        var card = org.example.grpc.Card.newBuilder()
                .setCardNumber(cardNumber)
                .setExpYear(expYear)
                .setExpMonth(expMonth)
                .setCvv(cvv)
                .build();

        var request = org.example.grpc.CheckoutRequest.newBuilder()
                .setSessionId(sessionId)
                .setAddress(address)
                .setCard(card)
                .build();

        buyerServiceStub.checkout(request);
        // Perform gRPC call
    }
}