package com.multilingual.rupali.accesspoints.models;

public class CouponMail {
    String email,name, code;
    public CouponMail(String email, String name, String coupon) {
        this.email = email;
        this.name = name;
        this.code = coupon;
    }
    public CouponMail(){

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoupon() {
        return code;
    }

    public void setCoupon(String coupon) {
        this.code = coupon;
    }


}
