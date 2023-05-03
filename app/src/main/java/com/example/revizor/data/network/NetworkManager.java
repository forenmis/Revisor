package com.example.revizor.data.network;

import android.content.Context;

import com.example.revizor.app.App;
import com.example.revizor.app.AppPreferences;
import com.example.revizor.data.network.entity.request.ReviewDetailsRequest;
import com.example.revizor.data.network.entity.request.ReviewRequest;
import com.example.revizor.data.network.entity.request.SignUpRequest;
import com.example.revizor.entity.Review;
import com.example.revizor.entity.User;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private final Api api;
    private final AppPreferences appPreferences;
    private final Gson gson = new Gson();

    public NetworkManager(Context context) {
        appPreferences = App.getInstance(context).getAppPreferences();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public Single<String> registration(String username, String email, String password) {
        SignUpRequest request = new SignUpRequest(username, email, password);
        return api.apiRegistration(request)
                .map(signUpResponse -> signUpResponse.getSessionToken())
                .doOnSuccess(token -> appPreferences.saveSessionToken(token));
    }

    public Single<String> logging(String username, String password) {
        return api.apiLogin(username, password)
                .map(signUpResponse -> signUpResponse.getSessionToken())
                .doOnSuccess(token -> appPreferences.saveSessionToken(token));
    }

    public Single<User> getCurrentUser() {
        return api.apiCurrentUser(appPreferences.sessionToken());
    }

    public Completable postReview(String title, String text, String address, String author) {
        ReviewRequest request = new ReviewRequest(title, text, address, author);
        return api.apiPostReview(appPreferences.sessionToken(), request);
    }

    public Single<List<Review>> getAllReviews(String token) {
        return api.apiGetReviewList(token)
                .map(listReviewResponse -> listReviewResponse.getResults());
    }

    public Single<Review> getReviewDetails(String objectId) {
        ReviewDetailsRequest request = new ReviewDetailsRequest();
        request.setObjectId(objectId);
        String json = gson.toJson(request);
        return api.apiGetReviewDetails(appPreferences.sessionToken(), json)
                .map(response -> response.getResults().get(0));
    }

    public Completable deleteReview(String token, String objectId) {
        return api.apiDeleteReview(token, objectId);
    }
}
