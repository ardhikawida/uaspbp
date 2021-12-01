package com.ssd.ssd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import com.ssd.ssd.database.entity.UserEntity;


@Dao
public interface UsersDao {

    @Query("SELECT * FROM users where email=(:email)")
    UserEntity cekuser(String email);

    @Query("INSERT INTO users (firstname,nama_belakang,nomor_hp,email,password,alamat,kota) VALUES (:firstname,:nama_belakang,:nomor_hp,:email,:password,:alamat,:kota)")
    void insertAuth(String firstname, String nama_belakang, String nomor_hp, String email, String password, String alamat , String kota);

    @Query("UPDATE users SET  firstname=:firstname,nama_belakang=:nama_belakang,nomor_hp=:nomor_hp,password=:password,alamat=:alamat,kota=:kota  WHERE email=:email")
    void updateauth(String firstname,String nama_belakang, String nomor_hp, String password, String alamat, String kota, String email);


    @Query("SELECT * FROM users where email=(:email) and password=(:password)")
    UserEntity login(String email, String password);
}
