package org.example.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "orderItem",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"orderId", "itemId"})}
)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="orderId", nullable=false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="itemId", nullable=false)
    private Item item;

    @Column
    private Integer itemQuantity;

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
