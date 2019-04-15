package com.multilingual.rupali.accesspoints.models;

public class Address{
    String h_no;
    String street;
    String state;
    String city;
    String country;
    String name;
    String landmark;
    int pincode;
    String contact_no;

    public Address(String h_no, String street, String state, String city, String country, String name, String landmark, int pincode, String contact_no) {
        this.h_no = h_no;
        this.street = street;
        this.state = state;
        this.city = city;
        this.country = country;
        this.name = name;
        this.landmark = landmark;
        this.pincode = pincode;
        this.contact_no = contact_no;
    }

    public String getH_no() {
        return h_no;
    }

    public void setH_no(String h_no) {
        this.h_no = h_no;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }


}
