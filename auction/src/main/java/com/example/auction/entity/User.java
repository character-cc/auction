package com.example.auction.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String userName;

    private String password;

    private String phoneNumber;

    private String role =  "ROLE_USER";

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_adresses" , joinColumns = @JoinColumn(name = "user_id"))
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    private List<Item> items = new ArrayList<Item>();

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY ,cascade = CascadeType.PERSIST)
    private List<Bid> bids = new ArrayList<Bid>();

    public User(){

    }

    public User(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();

        this.addresses = new HashSet<>(user.getAddresses());
        this.items = new ArrayList<>(user.getItems());
        this.bids = new ArrayList<>(user.getBids());
    }

    public User(String userName, String password, String phoneNumber, Set<Address> addresses) {
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getId() {
        return Id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }
}
