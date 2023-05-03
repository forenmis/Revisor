package com.example.revizor.data.network;

import com.example.revizor.data.network.entity.request.ReviewRequest;
import com.example.revizor.data.network.entity.request.SignUpRequest;
import com.example.revizor.data.network.entity.response.ListReviewResponse;
import com.example.revizor.data.network.entity.response.SignUpResponse;
import com.example.revizor.entity.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @POST("users")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc",
            "X-Parse-Revocable-Session: 1",
            "Content-Type: application/json"
    })
    Single<SignUpResponse> apiRegistration(@Body SignUpRequest body);

    @GET("login")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc",
            "X-Parse-Revocable-Session: 1"
    })
    Single<SignUpResponse> apiLogin(@Query("username") String username,
                                    @Query("password") String password);

    @GET("users/me")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc",
            "X-Parse-Revocable-Session: 1"
    })
    Single<User> apiCurrentUser(@Header("X-Parse-Session-Token") String token);

    @POST("classes/Review")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc",
            "Content-Type: application/json"
    })
    Completable apiPostReview(@Header("X-Parse-Session-Token") String token,
                              @Body ReviewRequest body);

    @GET("classes/Review")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc",

    })
    Single<ListReviewResponse> apiGetReviewList(@Header("X-Parse-Session-Token") String token);

    @GET("classes/Review")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc",

    })
    Single<ListReviewResponse> apiGetReviewDetails(
            @Header("X-Parse-Session-Token") String token,
            @Query(value = "where", encoded = true) String json
    );

    @DELETE("classes/Review/{objectId}")
    @Headers({
            "X-Parse-Application-Id: ZBgDdyJtVZePeiKre8aec0qIvyPAw15G8q9mlbNx",
            "X-Parse-REST-API-Key: UPOLlVKvxLhTilIz5zM87BbCpoqkyNcmtf4zH9Xc"
    })
    Completable apiDeleteReview(
            @Header("X-Parse-Session-Token") String token,
            @Path("objectId")
            String objectId
    );
}
