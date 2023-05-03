package com.example.revizor.data.network.entity.request;

public class ReviewRequest {
    private String title;
    private String text;
    private String address;
    private String author;


    public ReviewRequest(String title, String text, String address, String author) {
        this.title = title;
        this.text = text;
        this.address = address;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
