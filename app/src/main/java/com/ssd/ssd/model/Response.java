package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    @SerializedName("message")
    String message;
    @SerializedName("data")
    Integer data;
}
