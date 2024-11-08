package org.example.data.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "orders"
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
