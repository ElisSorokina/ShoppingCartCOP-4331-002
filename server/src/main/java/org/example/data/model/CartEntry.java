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
    @Column(name = "itemName", nullable = false)
    private String itemName;
    @Column(name = "sellPrice", nullable = false)
    private Integer sellPrice;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

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
