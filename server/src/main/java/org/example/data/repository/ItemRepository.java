package org.example.data.repository;

import org.example.data.model.Item;
import org.example.data.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Repository interface for accessing Item entities from the database.
 */
public interface ItemRepository extends CrudRepository<Item, UUID> {
    /**
     * Finds items sold by the specified seller.
     *
     * @param seller the seller whose items are to be retrieved.
     * @return a list of items sold by the specified seller.
     */
    List<Item> findBySeller(User seller);

    /**
     * Retrieves all items.
     *
     * @return a list of all items.
     */
    List<Item> findAll();

    /**
     * Finds items by a set of item IDs.
     *
     * @param ids the set of item IDs.
     * @return a list of items corresponding to the specified IDs.
     */
    List<Item> findByIdIn(Set<UUID> ids);
}
