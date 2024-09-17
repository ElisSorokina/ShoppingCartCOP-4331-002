package org.example.data.model;

import jakarta.persistence.*;


@Entity
@Table(
        name = "item",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "sellerId"})}
)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "invoicePriceCents", nullable = false)

    private Integer invoicePriceCents;

    @Column(name = "sellPriceCents", nullable = false)
    private Integer sellPriceCents;

    @ManyToOne
    @JoinColumn(name="sellerId", nullable=false)
    private User seller;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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