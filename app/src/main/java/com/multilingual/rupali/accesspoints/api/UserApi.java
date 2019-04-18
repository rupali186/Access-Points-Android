package com.multilingual.rupali.accesspoints.api;

import com.multilingual.rupali.accesspoints.models.EditUser;
import com.multilingual.rupali.accesspoints.models.SignInUser;
import com.multilingual.rupali.accesspoints.models.User;
import com.multilingual.rupali.accesspoints.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @GET("users")
    Call<UserResponse> getUsers();
    @GET("users/{userId}")
    Call<User> getUserById(@Path("userId") String userId);
    @GET("users/me")
    Call<User> getCurrentUser(@Header("x-auth") String xAuth);
    @GET("users/new")
    Call<UserResponse> getNewUsers(@Query("limit") int limit);
    @GET("users/newtolocker")
    Call<UserResponse> getNewLockerUsers(@Query("limit") int limit);
    @GET("users/target")
    Call<UserResponse> getTargetUsers(@Query("limit") int limit, @Query("threshold") int threshold);
    @POST("users/login")
    Call<User> loginUser(@Body SignInUser user);
    @POST("users")
    Call<User> createNewUser(@Body User user);
    @DELETE("users/me/token")
    Call<Void> logoutCurrentUser(@Header("x-auth") String xAuth);
    @DELETE("users/me")
    Call<User> deleteCurrentUser(@Header("x-auth") String xAuth);
    @PATCH("users/{userId}")
    Call<EditUser> updateUser(@Path("userId") String userId, @Header("x-auth") String xAuth, @Body User user);
}