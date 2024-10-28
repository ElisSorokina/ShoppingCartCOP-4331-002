package org.example.data.repository;

import org.example.data.model.Cart;
import org.example.data.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    Cart findByBuyer(User buyer);
}
