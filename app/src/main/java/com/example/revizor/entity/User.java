package com.example.revizor.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    private String login;
    private String email;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}