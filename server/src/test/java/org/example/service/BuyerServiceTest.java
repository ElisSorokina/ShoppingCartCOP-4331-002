package org.example.service;

import org.example.data.model.*;
import org.example.data.repository.CartRepository;
import org.example.data.repository.ItemRepository;
import org.example.data.repository.OrderRepository;
import org.example.grpc.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuyerServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private PaymentService paymentService;
    @Mock
    private OrderRepository orderRepository;

    BuyerService buyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buyerService = new BuyerService();
        ReflectionTestUtils.setField(buyerService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(buyerService, "cartRepository", cartRepository);
        ReflectionTestUtils.setField(buyerService, "paymentService", paymentService);
        ReflectionTestUtils.setField(buyerService, "orderRepository", orderRepository);


    }

    @Test
    void testCheckout(){
        var user = new User();
        var card = Card.newBuilder()
                .setCardHolderName("Ben Dover")
                .setCardNumber("1465896784352645")
                .setCvv(228)
                .setExpMonth(10)
                .setExpYear(2012)
                .build();

        var address = " 1100 S Ocean Blvd, Palm Beach, FL 33480";
        var order = new Order();
        var orderItems = new LinkedHashSet<OrderItem>();
        order.setOrderItems(orderItems);

        var cart = new Cart();
        var cartEntry1 = new CartEntry();
        cartEntry1.setItemCount(2);
        UUID itemId1 = UUID.randomUUID();
        cartEntry1.setItemId(itemId1);

        var cartEntry2 = new CartEntry();
        cartEntry2.setItemCount(1);
        UUID itemId2 = UUID.randomUUID();
        cartEntry2.setItemId(itemId2);
        cart.setCartEntries(Set.of(cartEntry1, cartEntry2));

        var item1 = new Item();
        item1.setId(itemId1);
        item1.setSellPriceCents(10);

        var item2 = new Item();
        item2.setId(itemId2);
        item2.setSellPriceCents(2);

        when(cartRepository.findByBuyer(user)).thenReturn(cart);
        when(itemRepository.findByIdIn(Set.of(itemId1,itemId2))).thenReturn(List.of(item1,item2));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        buyerService.checkout(user, address, card);
        assertEquals(2, orderItems.size());

        var iterator = orderItems.iterator();
        var orderItem1 = iterator.next();
        assertEquals(item1, orderItem1.getItem());
        assertEquals(cartEntry1.getItemCount(), orderItem1.getItemQuantity());

        var orderItem2 = iterator.next();
        assertEquals(item2, orderItem2.getItem());
        assertEquals(cartEntry2.getItemCount(), orderItem2.getItemQuantity());

        verify(orderRepository).save(same(order));
        verify(paymentService).makePayment(card, 22);
    }
}
