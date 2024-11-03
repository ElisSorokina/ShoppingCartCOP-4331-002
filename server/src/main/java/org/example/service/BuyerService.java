package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.model.Cart;
import org.example.data.model.CartEntry;
import org.example.data.model.Item;
import org.example.data.model.User;
import org.example.data.repository.CartRepository;
import org.example.data.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BuyerService {


    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;

    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    @Transactional
    public void addItem(UUID itemId, User user) {
        var cart = cartRepository.findByBuyer(user);
        var cartEntries = cart.getCartEntries();
        var item = itemRepository.findById(itemId).get();
        if(item.getQuantity()<1){
            throw new RuntimeException("Item is not available");
        }
        for (CartEntry cartEntry : cartEntries) {
            if (cartEntry.getItemId().equals(itemId)) {
                cartEntry.setItemCount(cartEntry.getItemCount() + 1);
                item.setQuantity(item.getQuantity()-1);
                return;
            }
        }

        var cartEntry = new CartEntry();
        cartEntry.setCart(cart);
        cartEntry.setItemId(itemId);
        cartEntry.setItemCount(1);
        item.setQuantity(item.getQuantity()-1);

        cart.getCartEntries().add(cartEntry);
    }
}

