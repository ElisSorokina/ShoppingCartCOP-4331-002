package org.example.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "cartEntry"
)

public class CartEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "itemId", nullable = false)
    private UUID itemId;
    @Column(name = "itemCount", nullable = false)
    private Integer itemCount;
    @ManyToOne
    @JoinColumn(name="cartId", nullable=false)
    private Cart cart;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
