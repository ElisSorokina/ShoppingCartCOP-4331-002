package org.example.data.repository;

import org.example.data.model.Cart;
import org.example.data.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Repository interface for accessing Cart entities from the database.
 */
public interface CartRepository extends CrudRepository<Cart, UUID> {
    /**
     * Finds a cart by the associated buyer.
     *
     * @param buyer the buyer whose cart is to be retrieved.
     * @return the cart associated with the specified buyer.
     */
    Cart findByBuyer(User buyer);
}