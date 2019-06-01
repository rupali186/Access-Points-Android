package com.multilingual.rupali.accesspoints.models;

public class EditCoupon {
    boolean used;

    public EditCoupon(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
