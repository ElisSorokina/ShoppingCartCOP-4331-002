package org.example.service;

import org.example.data.model.Item;
import org.example.data.model.User;
import org.example.data.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SellerService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SessionStore sessionStore;

    public List<Item> getItemList(UUID sessionId) {
        return itemRepository.findBySeller(sessionStore.getUser(sessionId));
    }

}
