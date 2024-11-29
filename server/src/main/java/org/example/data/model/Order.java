package org.example.data.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents an order placed by a buyer.
 */
@Entity
@Table(name = "orders")
public class Order {
    /**
     * Unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The set of items included in the order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    /**
     * The user who placed the order.
     */
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    /**
     * The total amount for the order in cents.
     */
    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    /**
     * The date and time when the order was placed.
     */
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
