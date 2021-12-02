package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class FoodModels {
    @SerializedName("id")
    private Integer id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("foto")
    private String foto;
    @SerializedName("harga")
    private Integer harga;

    public FoodModels(Integer id, String nama, String keterangan, String foto, Integer harga) {
        this.id = id;
        this.nama = nama;
        this.keterangan = keterangan;
        this.foto = foto;
        this.harga = harga;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }
}