package com.multilingual.rupali.accesspoints.api;

import com.multilingual.rupali.accesspoints.models.Coupon;
import com.multilingual.rupali.accesspoints.models.EditCoupon;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CouponApi {
    @POST("coupons")
    Call<Coupon> createCouponCode(@Body Coupon coupon);
    @GET("coupons/searchCode")
    Call<Coupon> searchCouponCode(@Query("code") String code,@Query("user_email") String user_email);
    @PATCH("coupons/{couponId}")
    Call<Coupon> updateCoupon(@Path("couponId") String couponId, @Body EditCoupon editCoupon);
}
