package com.ssd.ssd.network;

import com.ssd.ssd.model.Response;
import com.ssd.ssd.model.ResponseBody;
import com.ssd.ssd.model.ResponseBodyBarang;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.model.UsersModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //========Autentifikasi========
    @GET("read")
    Call<ResponseBody> getAllPhotos();

    @GET("auth")
    Call<ResponseBodyReq> getdetailakun(@Query("uid") String uid);

    @POST("register")
    @FormUrlEncoded
    Call<ResponseBodyReq> registerusers(
            @Field("uid") String uid,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("nohp") String nohp,
            @Field("email") String email,
            @Field("alamat") String alamat,
            @Field("kota") String kota,
            @Field("password") String password

    );

    @POST("updateauth")
    @FormUrlEncoded
    Call<Response> updateusers(
            @Field("uid") String uid,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("nohp") String nohp,
            @Field("email") String email,
            @Field("alamat") String alamat,
            @Field("kota") String kota,
            @Field("password") String password
    );
    //========END Autentifikasi========

    //========Barang========
    @GET("barang")
    Call<ResponseBodyBarang> getallbarang();

    @Multipart
    @POST("barang")
    Call<ResponseBodyReq> insertbarang(
            @Part MultipartBody.Part foto,
            @Part("nama") String nama,
            @Part("harga") Integer harga,
            @Part("keterangan") String keterangan

    );

    @POST("editbarang")
    @FormUrlEncoded
    Call<Response> editbarangtanpafoto(
            @Field("id") Integer id,
            @Field("nama") String nama,
            @Field("harga") Integer harga,
            @Field("keterangan") String keterangan
    );

    @Multipart
    @POST("editbarang")
    Call<Response> editbarang(
            @Part MultipartBody.Part foto,
            @Part("id") Integer id,
            @Part("nama") String nama,
            @Part("harga") Integer harga,
            @Part("keterangan") String keterangan

    );

    @POST("barang/{id}")
    Call<Response> deletebarang(
            @Path("id") Integer id
    );

    //========END Barang========
}