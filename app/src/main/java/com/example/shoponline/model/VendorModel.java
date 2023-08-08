package com.example.shoponline.model;

import java.util.List;

public class VendorModel {
    boolean success;
    String message;
    List<Vendor> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Vendor> getData() {
        return data;
    }

    public void setData(List<Vendor> data) {
        this.data = data;
    }
}
