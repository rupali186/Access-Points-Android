package com.multilingual.rupali.accesspoints.models;

public class AccessPointAddress {
    String A1;
    String A2;
    String A3;
    String A4;
    String Mode;
    String Type;
    String Partition;
    String range;


    public AccessPointAddress( String street,String city, String state , String country, String range) {
        this.A1 = street;
        this.A2 = state;
        this.A3 = city;
        this.A4 = country;
        this.Mode = "Online";
        this.Type = "residential";
        this.Partition = "city";
        this.range = range;

    }

    public String getStreet() {
        return A1;
    }

    public void setStreet(String street) {
        this.A1 = street;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getState() {
        return A3;
    }

    public void setState(String state) {
        this.A3 = state;
    }

    public String getCity() {
        return A2;
    }

    public void setCity(String city) {
        this.A2 = city;
    }

    public String getCountry() {
        return A4;
    }

    public void setCountry(String country) {
        this.A4 = country;
    }

}
