package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.model.*;
import org.example.data.repository.CartRepository;
import org.example.data.repository.ItemRepository;
import org.example.data.repository.OrderRepository;
import org.example.grpc.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    public void addItemsToCart(Set<UUID> immutableItemIds, User user) {
        var itemIds = new HashSet<>(immutableItemIds);
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var items = itemRepository.findByIdIn(itemIds);
        var itemsById = new HashMap<UUID, Item>();

        for (Item item : items) {
            if (item.getQuantity() < 1) {
                throw new RuntimeException("Item is not available");
            }
            itemsById.put(item.getId(), item);
        }

        for (CartEntry cartEntry : cartEntries) {
            if (itemIds.contains(cartEntry.getItemId())) {
                cartEntry.setItemCount(cartEntry.getItemCount() + 1);
                var item = itemsById.get(cartEntry.getItemId());
                item.setQuantity(item.getQuantity()-1);
                itemIds.remove(cartEntry.getItemId());

            }
        }

        for (UUID itemId : itemIds) {
            var cartEntry = new CartEntry();
            cartEntry.setCart(cart);
            cartEntry.setItemId(itemId);
            cartEntry.setItemCount(1);
            var item = itemsById.get(itemId);
            cartEntry.setItemName(item.getName());
            cartEntry.setSellPrice(item.getSellPriceCents());
            item.setQuantity(item.getQuantity() - 1);
            cart.getCartEntries().add(cartEntry);
        }

        cartRepository.save(cart);
        itemRepository.saveAll(items);
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
    public Order checkout(User user, String address, Card card) {
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var cartEntryByItemId = new HashMap<UUID, CartEntry>();
        for (CartEntry cartEntry : cartEntries) {
            cartEntryByItemId.put(cartEntry.getItemId(), cartEntry);
        }
        var itemIds = cartEntryByItemId.keySet();
        var items = itemRepository.findByIdIn(itemIds);

        Order entity = new Order();
        entity.setBuyer(user);
        entity.setOrderDate(LocalDateTime.now());
        var order = orderRepository.save(entity);
        var orderItems = order.getOrderItems();
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
        Order savedOrder = orderRepository.save(order);
        paymentService.makePayment(card, totalAmount);

        cart.getCartEntries().clear();
        cartRepository.save(cart);

        return savedOrder;
    }
}

