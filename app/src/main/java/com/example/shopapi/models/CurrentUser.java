package com.example.shopapi.models;

public class CurrentUser {
    String email;

    String password;

    public CurrentUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
