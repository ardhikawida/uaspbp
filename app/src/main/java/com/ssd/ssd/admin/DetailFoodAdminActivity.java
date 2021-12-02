package com.ssd.ssd.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.ssd.ssd.InsertFoodActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.databinding.ActivityDetailFoodAdminBinding;
import com.ssd.ssd.model.Response;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.utils.Constant;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailFoodAdminActivity extends AppCompatActivity {

    ActivityDetailFoodAdminBinding binding;
    String nama,foto,keterangan;
    Integer harga,id;
    ProgressDialog progressDialog;

    InputStream inputStream;
    private Uri uri;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail_food_admin);
        binding.getLifecycleOwner();

        progressDialog = new ProgressDialog(DetailFoodAdminActivity.this);
        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        foto = intent.getStringExtra("foto");
        keterangan = intent.getStringExtra("keterangan");
        harga = intent.getIntExtra("harga",0);
        id = intent.getIntExtra("id",0);


        binding.edtNama.setText(nama);
        binding.edtKeterangan.setText(keterangan);
        binding.edtHarga.setText(harga.toString());
        Picasso.get().load(new Constant().url +foto).centerCrop().fit().into(binding.gambarMakanan);

        binding.btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(DetailFoodAdminActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    openGallery();

                }else {
                    EasyPermissions.requestPermissions(DetailFoodAdminActivity.this,"This application need your permission to access photo gallery",PERMISSION_REQUEST_STORAGE
                            ,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        binding.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Sedang update data ...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String nama = binding.edtNama.getText().toString().trim();
                String keterangan = binding.edtKeterangan.getText().toString().trim();
                String harga = binding.edtHarga.getText().toString().trim();

                if (!nama.isEmpty() && !keterangan.isEmpty() && !harga.isEmpty() && inputStream!=null){
                    try {
                        uploadImage(getBytes(inputStream),nama,harga,keterangan);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (!nama.isEmpty() && !keterangan.isEmpty() && !harga.isEmpty()){
                    ApiInterface updatedata = ServiceGenerator.createService(ApiInterface.class);
                    Call<Response> call = updatedata.editbarangtanpafoto(id,nama,10000,keterangan);
                    call.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if (response.isSuccessful()){
                                if (response.body().getData() == 1){
                                    progressDialog.dismiss();
                                    Toast.makeText(DetailFoodAdminActivity.this,"Update data berhasil",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DetailFoodAdminActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(DetailFoodAdminActivity.this,"Update data gagal",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DetailFoodAdminActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(DetailFoodAdminActivity.this,"Masalah dengan server",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(DetailFoodAdminActivity.this,"Masalah dengan server",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(DetailFoodAdminActivity.this,"Jangan kosongi kolom",Toast.LENGTH_SHORT).show();
                }


            }
        });

        binding.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("sedang dihapus ...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                ApiInterface hapusbarang = ServiceGenerator.createService(ApiInterface.class);
                Call<Response> call = hapusbarang.deletebarang(id);
                call.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData() == 1){
                                progressDialog.dismiss();
                                Toast.makeText(DetailFoodAdminActivity.this,"delete data berhasil",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailFoodAdminActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(DetailFoodAdminActivity.this,"data tidak ada",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailFoodAdminActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(DetailFoodAdminActivity.this,"gagal dapat respon",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailFoodAdminActivity.this,"gagal req api",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void uploadImage(byte[] imagebyte,String nama, String harga, String keterangan){
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagebyte);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", ""+System.currentTimeMillis(), requestFile);
        ApiInterface insertbarang = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBodyReq> call = insertbarang.insertbarang(body,nama,Integer.parseInt(harga),keterangan);
        call.enqueue(new Callback<ResponseBodyReq>() {
            @Override
            public void onResponse(Call<ResponseBodyReq> call, retrofit2.Response<ResponseBodyReq> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(DetailFoodAdminActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    progressDialog.dismiss();

                    Toast.makeText(DetailFoodAdminActivity.this,"Gagal upload",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBodyReq> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailFoodAdminActivity.this,"Gagal req server",Toast.LENGTH_SHORT).show();

                Log.d("sandy", "onFailure: "+t.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                try {
                    uri = data.getData();
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    Picasso.get().load(uri).centerCrop().fit().into(binding.gambarMakanan);
                    inputStream = is;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openGallery();
                }

                return;
            }
        }
    }
}