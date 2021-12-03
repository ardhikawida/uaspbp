package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

public class BarangModel {
    @SerializedName("id")
    private Integer id;
    @SerializedName("uid")
    private String uid;
    @SerializedName("id_barang")
    private Integer id_barang;
    @SerializedName("nama")
    private String nama;
    @SerializedName("harga")
    private Integer harga;
    @SerializedName("harga_total")
    private Integer harga_total;
    @SerializedName("jumlah")
    private Integer jumlah;
    @SerializedName("status")
    private String status;

    public BarangModel(Integer id, String uid, Integer id_barang, String nama, Integer harga, Integer harga_total, Integer jumlah, String status) {
        this.id = id;
        this.uid = uid;
        this.id_barang = id_barang;
        this.nama = nama;
        this.harga = harga;
        this.harga_total = harga_total;
        this.jumlah = jumlah;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public Integer getId_barang() {
        return id_barang;
    }

    public String getNama() {
        return nama;
    }

    public Integer getHarga() {
        return harga;
    }

    public Integer getHarga_total() {
        return harga_total;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public String getStatus() {
        return status;
    }
}
