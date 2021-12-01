package com.ssd.ssd.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaksi")

public class TransaksiEntity {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "nama")
    public String nama;
    @ColumnInfo(name = "harga")
    public Integer harga;
    @ColumnInfo(name = "harga_total")
    public Integer harga_total;
    @ColumnInfo(name = "jumlah")
    public Integer jumlah;
    @ColumnInfo(name = "status")
    public String status;


}
