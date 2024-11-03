package org.example.data.repository;

import org.example.data.model.Item;
import org.example.data.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends CrudRepository<Item, UUID> {
    List<Item> findBySeller(User seller);
    List<Item> findAll();
}
