package com.ssd.ssd.model;

public class FoodModels {

    private String nama, gambar,deskripsi;
    private Integer harga;

    public FoodModels(String nama, String gambar, String deskripsi, Integer harga) {
        this.nama = nama;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }
}