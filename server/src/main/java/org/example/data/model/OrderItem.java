package org.example.data.model;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents an item included in an order.
 */
@Entity
@Table(
        name = "orderItem",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"orderId", "itemId"})}
)
public class OrderItem {
    /**
     * Unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The order to which this item belongs.
     */
    @ManyToOne
    @JoinColumn(name="orderId", nullable=false)
    private Order order;

    /**
     * The item that was ordered.
     */
    @ManyToOne
    @JoinColumn(name="itemId", nullable=false)
    private Item item;

    /**
     * The quantity of the item in the order.
     */
    @Column
    private Integer itemQuantity;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}