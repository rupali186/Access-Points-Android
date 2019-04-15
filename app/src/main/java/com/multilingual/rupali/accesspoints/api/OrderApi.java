package com.multilingual.rupali.accesspoints.api;

import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.response.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderApi {
    @GET("orders")
    Call<OrderResponse> getOrders();
    @GET("orders/{orderId}")
    Call<Order> getOrderById(@Path("orderId") String orderId);
    @Headers("Content-Type: application/json")
    @POST("orders")
    Call<Order> createNewOrder(@Header("x-auth") String xAuth,@Body Order order);
    @PATCH("orders/{orderId}")
    Call<Order> updateOrder(@Path("orderId") String orderId);
    @DELETE ("orders/{orderId}")
    Call<Order> deleteOrder(@Path("orderId") String orderId,@Header("x-auth") String xAuth);
}