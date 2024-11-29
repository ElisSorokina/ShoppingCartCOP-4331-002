package org.example.data.model;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents an entry in a shopping cart, containing item details and quantity.
 */
@Entity
@Table(
        name = "cartEntry"
)
public class CartEntry {
    /**
     * Unique identifier for the cart entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Identifier of the item added to the cart.
     */
    @Column(name = "itemId", nullable = false)
    private UUID itemId;

    /**
     * The quantity of the item in the cart.
     */
    @Column(name = "itemCount", nullable = false)
    private Integer itemCount;

    /**
     * The name of the item in the cart.
     */
    @Column(name = "itemName", nullable = false)
    private String itemName;

    /**
     * The selling price of the item in the cart.
     */
    @Column(name = "sellPrice", nullable = false)
    private Integer sellPrice;

    /**
     * The cart to which this entry belongs.
     */
    @ManyToOne
    @JoinColumn(name="cartId", nullable=false)
    private Cart cart;

    // Getters and setters
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
