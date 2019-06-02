package com.multilingual.rupali.accesspoints.models;

public class Coupon {
    String _id;
    String code;
    int discount;
    String user_email;
    String expiry_date;
    String creation_date;
    Boolean used;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }



    public Coupon(int discount, String user_email, String expiry_date) {
        this.discount = discount;
        this.user_email = user_email;
        this.expiry_date = expiry_date;
    }




    public Coupon() {
    }

    public Coupon(Boolean used,String code,  String user_email, String expiry_date,  int discount,String creation_date) {
        this.code = code;
        this.discount = discount;
        this.user_email = user_email;
        this.expiry_date = expiry_date;
        this.creation_date = creation_date;
        this.used = used;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }




    public String getCode() {
        return code;
    }

    public int getDiscount() {
        return discount;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public Boolean getUsed() {
        return used;
    }





}
