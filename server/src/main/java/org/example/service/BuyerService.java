package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.model.*;
import org.example.data.repository.CartRepository;
import org.example.data.repository.ItemRepository;
import org.example.data.repository.OrderRepository;
import org.example.grpc.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuyerService {


    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderRepository orderRepository;

    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    @Transactional
    public void addItemToCart(UUID itemId, User user) {
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var item = itemRepository.findById(itemId).get();
        if (item.getQuantity() < 1) {
            throw new RuntimeException("Item is not available");
        }
        for (CartEntry cartEntry : cartEntries) {
            if (cartEntry.getItemId().equals(itemId)) {
                cartEntry.setItemCount(cartEntry.getItemCount() + 1);
                item.setQuantity(item.getQuantity() - 1);
                return;
            }
        }

        var cartEntry = new CartEntry();
        cartEntry.setCart(cart);
        cartEntry.setItemId(itemId);
        cartEntry.setItemCount(1);
        item.setQuantity(item.getQuantity() - 1);

        cart.getCartEntries().add(cartEntry);
        cartRepository.save(cart);
    }

    @Transactional
    public Cart getCart(User user){
        return cartRepository.findByBuyer(user);
    }

    @Transactional
    public void deleteItemFromCart(UUID itemId, User user) {
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var item = itemRepository.findById(itemId).get();
        for (CartEntry cartEntry : cartEntries) {
            if (cartEntry.getItemId().equals(itemId)) {
                cartEntries.remove(cartEntry);
                item.setQuantity(item.getQuantity() - cartEntry.getItemCount());
                break;
            }
        }
        cartRepository.save(cart);
        itemRepository.save(item);

    }

    @Transactional
    public void updateCart(UUID itemId, User user, int newItemCount) {
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var item = itemRepository.findById(itemId).get();

        for (CartEntry cartEntry : cartEntries) {

            if (cartEntry.getItemId().equals(itemId)) {
                int oldItemCount = cartEntry.getItemCount();
                cartEntry.setItemCount(newItemCount);

                item.setQuantity(item.getQuantity() + oldItemCount - newItemCount);
                return;
            }
        }
    }

    @Transactional
    public void checkout(User user, String address, Card card) {
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var cartEntryByItemId = new HashMap<UUID, CartEntry>();
        for (CartEntry cartEntry : cartEntries) {
            cartEntryByItemId.put(cartEntry.getItemId(), cartEntry);
        }
        var itemIds = cartEntryByItemId.keySet();
        var items = itemRepository.findByIdIn(itemIds);

        var order = orderRepository.save(new Order());

        var orderItems = new HashSet<OrderItem>();
        var totalAmount = 0;
        for (Item item : items) {
            var cartEntry = cartEntryByItemId.get(item.getId());
            var orderItem=new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setItemQuantity(cartEntry.getItemCount());
            orderItems.add(orderItem);
            totalAmount += cartEntry.getItemCount() * item.getSellPriceCents();
        }
        orderRepository.save(order);
        paymentService.makePayment(card, totalAmount);

    }
}

