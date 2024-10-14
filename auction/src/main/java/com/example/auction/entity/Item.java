package com.example.auction.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID" , nullable = false)
    private User user;

    @OneToMany(mappedBy = "item" , fetch = FetchType.LAZY ,cascade = CascadeType.PERSIST)
    private List<Bid> bids = new ArrayList<Bid>();

    @Version
    private long version;

    public Item() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public Item(String name, String description, Double price, String imageUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, User user) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.user = user;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(price, item.price) && Objects.equals(imageUrl, item.imageUrl) && Objects.equals(startDateTime, item.startDateTime) && Objects.equals(endDateTime, item.endDateTime) && Objects.equals(user.getId(), item.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, imageUrl, startDateTime, endDateTime);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
