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

/**
 * Service class for managing buyer-related operations.
 */
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

    /**
     * Retrieves the list of all available items.
     *
     * @return a list of items available for purchase.
     */
    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    /**
     * Adds items to the buyer's cart.
     *
     * @param itemIds the set of item IDs to add to the cart.
     * @param buyer the buyer who owns the cart.
     */
    @Transactional
    public void addItemsToCart(Set<UUID> itemIds, User buyer) {
        var itemIdsLocal = new HashSet<>(itemIds);
        var cart = cartRepository.findByBuyer(buyer);
        var cartEntries = cart.getCartEntries();
        var items = itemRepository.findByIdIn(itemIdsLocal);
        var itemsById = new HashMap<UUID, Item>();

        for (Item item : items) {
            if (item.getQuantity() < 1) {
                throw new RuntimeException("Item is not available");
            }
            itemsById.put(item.getId(), item);
        }

        for (CartEntry cartEntry : cartEntries) {
            if (itemIdsLocal.contains(cartEntry.getItemId())) {
                cartEntry.setItemCount(cartEntry.getItemCount() + 1);
                var item = itemsById.get(cartEntry.getItemId());
                item.setQuantity(item.getQuantity()-1);
                itemIdsLocal.remove(cartEntry.getItemId());
            }
        }

        for (UUID itemId : itemIdsLocal) {
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

    /**
     * Retrieves the buyer's shopping cart.
     *
     * @param buyer the buyer whose cart is to be retrieved.
     * @return the cart associated with the specified buyer.
     */
    @Transactional
    public Cart getCart(User buyer){
        return cartRepository.findByBuyer(buyer);
    }

    /**
     * Deletes an item from the buyer's cart.
     *
     * @param itemId the ID of the item to delete.
     * @param buyer the buyer who owns the cart.
     */
    @Transactional
    public void deleteItemFromCart(UUID itemId, User buyer) {
        var cart = cartRepository.findByBuyer(buyer);
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

    /**
     * Updates the quantity of a specific item in the buyer's cart.
     *
     * @param itemId the ID of the item to update.
     * @param buyer the buyer who owns the cart.
     * @param newItemCount the new quantity of the item.
     */
    @Transactional
    public void updateCart(UUID itemId, User buyer, int newItemCount) {
        var cart = cartRepository.findByBuyer(buyer);
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


    /**
     * Handles the checkout process for the buyer's cart.
     *
     * @param buyer the buyer who is checking out.
     * @param address the shipping address for the order.
     * @param card the payment card details.
     * @return the order created after successful checkout.
     */
    @Transactional
    public Order checkout(User buyer, String address, Card card) {
        var cart = cartRepository.findByBuyer(buyer);
        var cartEntries = cart.getCartEntries();
        var cartEntryByItemId = new HashMap<UUID, CartEntry>();
        for (CartEntry cartEntry : cartEntries) {
            cartEntryByItemId.put(cartEntry.getItemId(), cartEntry);
        }
        var itemIds = cartEntryByItemId.keySet();
        var items = itemRepository.findByIdIn(itemIds);

        Order entity = new Order();
        entity.setBuyer(buyer);
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

