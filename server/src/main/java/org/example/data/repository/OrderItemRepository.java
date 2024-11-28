package org.example.data.repository;

import org.example.data.model.Order;
import org.example.data.model.OrderItem;
import org.example.data.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends CrudRepository<OrderItem, UUID> {
    /**
     * Fetches all order items  sold by the specified seller.
     *
     * @param seller the seller whose items are part of the orders.
     * @return a list of orders containing items sold by the seller.
     */
    @Query("SELECT DISTINCT oi FROM OrderItem oi JOIN oi.item i WHERE i.seller = :seller")
    List<OrderItem> findOrderItemsBySeller(@Param("seller") User seller);

}
