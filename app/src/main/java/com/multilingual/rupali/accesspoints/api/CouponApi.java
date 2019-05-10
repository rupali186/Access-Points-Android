package com.multilingual.rupali.accesspoints.api;

import com.multilingual.rupali.accesspoints.models.Coupon;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CouponApi {
    @POST("coupons")
    Call<Coupon> createCouponCode(@Body Coupon coupon);
}
