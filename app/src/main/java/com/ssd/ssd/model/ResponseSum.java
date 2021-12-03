package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

public class ResponseSum {
    @SerializedName("message")
    String message;
    @SerializedName("data")
    Integer data;

    public ResponseSum(String message, Integer data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getData() {
        return data;
    }
}
