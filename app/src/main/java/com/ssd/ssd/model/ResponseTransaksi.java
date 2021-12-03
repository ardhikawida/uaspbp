package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTransaksi {
    @SerializedName("message")
     String message;
    @SerializedName("data")
    List<BarangModel> data;

    public ResponseTransaksi(String message, List<BarangModel> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<BarangModel> getData() {
        return data;
    }
}
