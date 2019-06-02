package com.multilingual.rupali.accesspoints.api;

import com.multilingual.rupali.accesspoints.models.Coupon;
import com.multilingual.rupali.accesspoints.models.CouponMail;
import com.multilingual.rupali.accesspoints.models.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MailAPI {
    @POST("sendCouponMail")
    Call<CouponMail> sendCoupon(@Body CouponMail couponMail);
    @POST("OrderDetMail")
    Call<Order> sendOrderDetail( @Body Order order);
}
