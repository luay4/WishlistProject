package com.example.wishlistproject.models;

import java.util.ArrayList;

public class User {

    private int userID;
    private String username;
    private String password;
    private ArrayList<String> wishes = new ArrayList<>();

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getWishes() {
        return wishes;
    }

    public void clearWishlist() {
        wishes.clear();
    }

    public void addToWishlist(String wish) {
        wishes.add(wish);
    }
}
