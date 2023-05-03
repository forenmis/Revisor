package com.example.revizor.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.revizor.entity.Review;
import com.example.revizor.entity.SendStatus;
import com.example.revizor.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class Database {
    private final SQLiteDatabase db;

    @SuppressWarnings("resource")
    public Database(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
    }

    public Completable addReviewAsync(Review review) {
        return Completable.create(emitter -> {
            addReview(review);
            if (!emitter.isDisposed()) emitter.onComplete();
        });
    }

    private void addReview(Review review) {
        ContentValues cv = new ContentValues();
        cv.put("caption", review.getCaption());
        cv.put("title", review.getTitle());
        cv.put("photoUrl", review.getPhotoUrl());
        cv.put("date", review.getDate());
        cv.put("sendStatus", Utils.getStrStatus(review.getSendStatus()));
        cv.put("location", review.getLocation());
        db.insert("Reviewes", null, cv);
    }

    public Single<List<Review>> getAllReviewAsync() {
        return Single.fromCallable(() -> getAllReviews());
    }

    private List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Reviewes", null)) {
            if (cursor.getCount() == 0) return reviews;
            cursor.moveToNext();
            int idIndex = cursor.getColumnIndex("id");
            int captionIndex = cursor.getColumnIndex("caption");
            int titleIndex = cursor.getColumnIndex("title");
            int photoUrlIndex = cursor.getColumnIndex("photoUrl");
            int dateIndex = cursor.getColumnIndex("date");
            int statusIndex = cursor.getColumnIndex("sendStatus");
            int locationIndex = cursor.getColumnIndex("location");

            do {
                Review review = new Review();
                review.setId(cursor.getLong(idIndex));
                review.setCaption(cursor.getString(captionIndex));
                review.setTitle(cursor.getString(titleIndex));
                review.setPhotoUrl(cursor.getString(photoUrlIndex));
                review.setDate(cursor.getString(dateIndex));
                review.setSendStatus(Utils.getStatusByStr(cursor.getString(statusIndex)));
                review.setLocation(cursor.getString(locationIndex));
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        return reviews;
    }

    private Review getReviewById(long id) {
        Review review = new Review();
        try (Cursor cursor = db.rawQuery("SELECT * FROM Reviewes WHERE id = " + id,
                null)) {
            cursor.moveToFirst();

            int captionIndex = cursor.getColumnIndex("caption");
            int titleIndex = cursor.getColumnIndex("title");
            int photoUrlIndex = cursor.getColumnIndex("photoUrl");
            int dateIndex = cursor.getColumnIndex("date");
            int statusIndex = cursor.getColumnIndex("sendStatus");
            int locationIndex = cursor.getColumnIndex("location");

            review.setId(id);
            review.setCaption(cursor.getString(captionIndex));
            review.setTitle(cursor.getString(titleIndex));
            review.setPhotoUrl(cursor.getString(photoUrlIndex));
            review.setSendStatus(Utils.getStatusByStr(cursor.getString(statusIndex)));
            review.setDate(cursor.getString(dateIndex));
            review.setLocation(cursor.getString(locationIndex));

            return review;
        }
    }

    public Single<Review> getReviewByIdAsync(long id) {
        return Single.fromCallable(() -> getReviewById(id));
    }

    private void changeStatusReview(Review review, SendStatus status) {
        ContentValues cv = new ContentValues();
        cv.put("sendStatus", Utils.getStrStatus(status));

        db.update("Reviewes", cv, "id = ?", new String[]{String.valueOf(review.getId())});
    }

    public Completable changeStatusReviewAsync(Review review, SendStatus status) {
        return Completable.create(emitter -> {
            changeStatusReview(review, status);
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }
}
