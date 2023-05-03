package com.example.revizor.screens.drafts.draft_details;

import com.example.revizor.entity.Review;

public interface OnDraftClickCallback {
    void onDraftClick(Review review);

    void onDraftSend(Review review);
}
