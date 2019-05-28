package com.multilingual.rupali.accesspoints.models;

import java.util.ArrayList;

public class Order {
    String user_id;
    String _id;
    Size size;
    int price;
    String o_date;
    String del_date;//
    int category_id;
    int product_id;
    int weight;
    Image image;
    String status;
    String del_mode;
    String payment_status;
    boolean isExpanded;
   Address address;
    AcessPointDetail access_pt_address;

    public Order() {
        isExpanded=false;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public Order(Size size, int price,  int category_id, int product_id, String payment_status, String del_date, String del_mode, int weight, Address address) {
        this.size = size;
        this.price = price;
        this.del_date = del_date;
        this.category_id = category_id;
        this.product_id = product_id;
        this.weight = weight;
        this.del_mode = del_mode;
        this.payment_status = payment_status;
        this.address = address;
    }

    public Order(Size size, int price,  int category_id, int product_id, String payment_status, String del_date, String del_mode, int weight,Address address, AcessPointDetail access_pt_address) {
        this.size = size;
        this.price = price;
        this.del_date = del_date;
        this.category_id = category_id;
        this.product_id = product_id;
        this.weight = weight;
        this.del_mode = del_mode;
        this.payment_status = payment_status;
        this.address = address;
        this.access_pt_address = access_pt_address;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getO_date() {
        return o_date;
    }

    public void setO_date(String o_date) {
        this.o_date = o_date;
    }

    public String getDel_date() {
        return del_date;
    }

    public void setDel_date(String del_date) {
        this.del_date = del_date;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDel_mode() {
        return del_mode;
    }

    public void setDel_mode(String del_mode) {
        this.del_mode = del_mode;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AcessPointDetail getAccess_pt_address() {
        return access_pt_address;
    }

    public void setAccess_pt_address(AcessPointDetail access_pt_address) {
        this.access_pt_address = access_pt_address;
    }
}

class Image{
    String url;
}