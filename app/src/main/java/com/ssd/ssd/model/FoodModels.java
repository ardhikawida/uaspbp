package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class FoodModels {
    @SerializedName("nama")
    private String nama;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("foto")
    private String foto;
    @SerializedName("harga")
    private Integer harga;

    public FoodModels(String nama, String keterangan, String foto, Integer harga) {
        this.nama = nama;
        this.keterangan = keterangan;
        this.foto = foto;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getFoto() {
        return foto;
    }

    public Integer getHarga() {
        return harga;
    }
}