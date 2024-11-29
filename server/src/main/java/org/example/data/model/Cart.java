package org.example.data.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

/**
 * Represents a shopping cart associated with a specific user.
 */
@Entity
@Table(
        name = "cart",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"buyerId"})}
)
public class Cart {
    /**
     * Unique identifier for the cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The user who owns the cart.
     */
    @OneToOne
    @JoinColumn(name="buyerId", nullable=false)
    private User buyer;

    /**
     * The set of entries in the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartEntry> cartEntries;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Set<CartEntry> getCartEntries() {
        return cartEntries;
    }

    public void setCartEntries(Set<CartEntry> cartEntries) {
        this.cartEntries = cartEntries;
    }
}