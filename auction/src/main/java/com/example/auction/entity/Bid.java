package com.example.auction.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NamedEntityGraph(name = "Bid.item", attributeNodes = @NamedAttributeNode("item"))
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Double amount;

    private LocalDateTime bidTime;

    public Bid() {
    }

    public Bid(User user, Double amount, Item item, LocalDateTime bidTime) {
        this.user = user;
        this.amount = amount;
        this.item = item;
        this.bidTime = bidTime;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(user.getId(), bid.user.getId()) && Objects.equals(item.getId(), bid.item.getId()) && Objects.equals(amount, bid.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, item, amount, bidTime);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
}
