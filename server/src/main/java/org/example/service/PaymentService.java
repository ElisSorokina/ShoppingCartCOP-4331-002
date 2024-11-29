package org.example.service;

import org.example.grpc.Card;
import org.springframework.stereotype.Service;


/**
 * This is a mock-up of a payment service that doesn't do real logic.
 */
@Service
public class PaymentService {
    public void makePayment(Card card, int totalAmount){
        System.out.println("Making payment using card " + card.getCardNumber().substring(11, 16) + " for amount of " + totalAmount);
    }
}
