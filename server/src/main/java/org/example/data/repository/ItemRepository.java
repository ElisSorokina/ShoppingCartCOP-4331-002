package org.example.data.repository;

import org.example.data.model.Item;
import org.example.data.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findBySeller(User seller);
}
