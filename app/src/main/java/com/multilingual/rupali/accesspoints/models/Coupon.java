package com.multilingual.rupali.accesspoints.models;

public class Coupon {
    String code;
    int type;
    String user_email;

    public Coupon() {
    }

    public Coupon(int type, String user_email) {
        this.type = type;
        this.user_email = user_email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
