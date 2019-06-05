package com.multilingual.rupali.accesspoints.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    String _id;
    String email;
    String password;
    ArrayList<Token> tokens;
    String u_name;
    String acc_creation_date;
    ArrayList<String> phone_no;
    String dob;
    String gender;
    String last_order_date;
    String last_coupon_date;
    int del_failures_no;
    int num_orders;
    ArrayList<Address> address;
    Boolean locker_used;
    boolean isExpanded;

    public User(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {

            return new User[size];
        }

    };

    public void readFromParcel(Parcel in) {
//        String _id = in.readString();
//        String email = in.readString();
//        String password = in.readString();;
//        //ArrayList<Token> tokens;
//        String u_name = in.readString();;
//        String acc_creation_date = in.readString();;
//       // ArrayList<String> phone_no;
//        String dob = in.readString();;
//        String gender = in.readString();;
//        String last_order_date = in.readString();;
//        int del_failures_no = in.readInt();;
//        int num_orders = in.readInt();
//        //ArrayList<Address> address = in.readArray(A);
//        //Boolean locker_used ;
//        //boolean isExpanded;

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
//       dest.writeString(email);
//       dest.writeString(u_name);
       // dest.writeInt();
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public User(String email, String password, String u_name, ArrayList<String> phone_no, String dob, String gender, ArrayList<Address> address) {
        this.email = email;
        this.password = password;
        this.u_name = u_name;
        this.phone_no = phone_no;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.isExpanded=false;
    }
    public User(String email,String u_name, ArrayList<String> phone_no, String dob, String gender, ArrayList<Address> address) {
        this.email = email;
        this.password = password;
        this.u_name = u_name;
        this.phone_no = phone_no;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.isExpanded=false;
    }

    public String getLast_coupon_date() {
        return last_coupon_date;
    }

    public void setLast_coupon_date(String last_coupon_date) {
        this.last_coupon_date = last_coupon_date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getAcc_creation_date() {
        return acc_creation_date;
    }

    public void setAcc_creation_date(String acc_creation_date) {
        this.acc_creation_date = acc_creation_date;
    }

    public ArrayList<String> getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(ArrayList<String> phone_no) {
        this.phone_no = phone_no;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLast_order_date() {
        return last_order_date;
    }

    public void setLast_order_date(String last_order_date) {
        this.last_order_date = last_order_date;
    }

    public int getDel_failures_no() {
        return del_failures_no;
    }

    public void setDel_failures_no(int del_failures_no) {
        this.del_failures_no = del_failures_no;
    }

    public int getNum_orders() {
        return num_orders;
    }

    public void setNum_orders(int num_orders) {
        this.num_orders = num_orders;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public Boolean getLocker_used() {
        return locker_used;
    }

    public void setLocker_used(Boolean locker_used) {
        this.locker_used = locker_used;
    }

}

