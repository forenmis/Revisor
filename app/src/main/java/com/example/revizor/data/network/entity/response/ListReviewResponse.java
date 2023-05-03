package com.example.revizor.data.network.entity.response;

import com.example.revizor.entity.Review;

import java.util.List;

public class ListReviewResponse {

    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
