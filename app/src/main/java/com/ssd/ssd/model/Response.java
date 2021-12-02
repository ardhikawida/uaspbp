package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    @SerializedName("message")
    String message;
    @SerializedName("data")
    Integer data;

    public Response(String message, Integer data) {
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
