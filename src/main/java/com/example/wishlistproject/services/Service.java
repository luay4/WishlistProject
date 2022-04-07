package com.example.wishlistproject.services;

import com.example.wishlistproject.repositories.WishRepository;

import java.sql.Statement;

public class Service {

    private WishRepository wr = new WishRepository();
    private String sqlString;
    private Statement stmt;

}
