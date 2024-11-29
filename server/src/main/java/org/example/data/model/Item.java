package org.example.data.model;

import jakarta.persistence.*;

import java.util.UUID;


/**
 * Represents an item available for purchase.
 */
@Entity
@Table(
        name = "item",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "sellerId"})}
)
public class Item {
    /**
     * Unique identifier for the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Name of the item.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Available quantity of the item.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Invoice price of the item in cents.
     */
    @Column(name = "invoicePriceCents", nullable = false)
    private Integer invoicePriceCents;

    /**
     * Selling price of the item in cents.
     */
    @Column(name = "sellPriceCents", nullable = false)
    private Integer sellPriceCents;

    /**
     * The user who is selling the item.
     */
    @ManyToOne
    @JoinColumn(name="sellerId", nullable=false)
    private User seller;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getInvoicePriceCents() {
        return invoicePriceCents;
    }

    public void setInvoicePriceCents(Integer invoicePriceCents) {
        this.invoicePriceCents = invoicePriceCents;
    }

    public Integer getSellPriceCents() {
        return sellPriceCents;
    }

    public void setSellPriceCents(Integer sellPriceCents) {
        this.sellPriceCents = sellPriceCents;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
