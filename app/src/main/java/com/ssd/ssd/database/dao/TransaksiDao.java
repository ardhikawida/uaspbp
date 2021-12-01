package com.ssd.ssd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;


import com.ssd.ssd.database.entity.TransaksiEntity;
import com.ssd.ssd.database.entity.UserEntity;

import java.util.List;

@Dao
public interface TransaksiDao {

    //Table Transaksi
    @Query("SELECT * FROM transaksi")
    List<TransaksiEntity> getAllTransaksi();

    @Query("SELECT * FROM transaksi WHERE email=:email AND status=:proses")
    List<TransaksiEntity> getcart(String email, String proses);

    @Query("SELECT * FROM transaksi WHERE email=:email AND status=:selesai")
    List<TransaksiEntity> getorders(String email, String selesai);


    @Query("SELECT * FROM transaksi where email=(:email) and nama=(:nama) and status=:status")
    TransaksiEntity checkfood(String email, String nama, String status);

    @Query("UPDATE transaksi SET  jumlah=:jumlah,harga_total=:harga_total WHERE email=:email AND nama=:nama and status=:status")
    void update(String email, Integer jumlah, Integer harga_total , String nama, String status);

    @Query("UPDATE transaksi SET  status=:status WHERE email=:email")
    void checkout(String status, String email);


    @Query("INSERT INTO transaksi (email, nama,harga,harga_total,jumlah,status) VALUES (:email,:nama,:harga,:harga_total,:jumlah,:status)")
    void insertcart(String email, String  nama, Integer  harga, Integer harga_total, Integer jumlah, String status);

    @Query("SELECT * FROM transaksi WHERE nama=:nama AND email=:email")
    TransaksiEntity getdetailfood(String nama,String  email);

    @Query("SELECT SUM(harga_total) as total from transaksi WHERE email=:email AND status=:status")
    Integer getsum(String email, String status);

    @Query("SELECT SUM(harga_total) as total from transaksi WHERE email=:email AND status=:status")
    Integer getsumorder(String email, String status);

    //update jumla

    //delete orders
    @Query("DELETE FROM transaksi WHERE email=:email AND status=:status")
    void deleteorder(String email, String status);

    @Delete
    void delete(TransaksiEntity transaksi);




}
