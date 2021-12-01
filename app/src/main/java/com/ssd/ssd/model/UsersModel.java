package com.ssd.ssd.model;

import com.google.gson.annotations.SerializedName;

public class UsersModel {
    @SerializedName("uid")
    private String uid;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("nohp")
    private String nohp;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("kota")
    private String kota;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("id")
    private Integer id;

    public UsersModel(String uid, String firstname, String lastname, String nohp, String email, String password, String alamat, String kota, String updated_at, String created_at, Integer id) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nohp = nohp;
        this.email = email;
        this.password = password;
        this.alamat = alamat;
        this.kota = kota;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
