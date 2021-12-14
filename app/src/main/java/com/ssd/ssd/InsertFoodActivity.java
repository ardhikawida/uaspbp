package com.ssd.ssd;

import static com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.ssd.ssd.admin.AdminActivity;
import com.ssd.ssd.auth.RegisterActivity;
import com.ssd.ssd.auth.VerifikasiActivity;
import com.ssd.ssd.databinding.ActivityInsertFoodBinding;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.utils.FileUtils;
import com.vincent.filepicker.activity.ImagePickActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertFoodActivity extends AppCompatActivity {
    Uri imageuri;
    Bitmap photo;
    private static final int IMAGE_CODE_CAPTURE = 10;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final String TYPE_1 = "multipart";
    ActivityInsertFoodBinding binding;
    private Uri uri;
    View view;
    ProgressDialog progressDialog;
    InputStream inputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_insert_food);
        binding.getLifecycleOwner();
        progressDialog = new ProgressDialog(InsertFoodActivity.this);

        binding.btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(InsertFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    openGallery();

                }else {
                    EasyPermissions.requestPermissions(InsertFoodActivity.this,"This application need your permission to access photo gallery",PERMISSION_REQUEST_STORAGE
                    ,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        binding.btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(InsertFoodActivity.this, Manifest.permission.CAMERA)) {
                    bukakamera();

                } else {
                    EasyPermissions.requestPermissions(InsertFoodActivity.this, "This application need your permission to access photo gallery", PERMISSION_REQUEST_STORAGE
                            , Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

//
//                if (EasyPermissions.hasPermissions(InsertFoodActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
//                    startActivityForResult(cameraIntent, IMAGE_CODE_CAPTURE);
//
//                } else {
//                    EasyPermissions.requestPermissions(InsertFoodActivity.this, "This application need your permission to access photo gallery", PERMISSION_REQUEST_STORAGE
//                            , Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                }
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Sedang upload ....");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                String nama = binding.edtNama.getText().toString().trim();
                String harga = binding.edtHarga.getText().toString().trim();
                String keterangan = binding.edtKeterangan.getText().toString().trim();

                if (!nama.isEmpty() && !harga.isEmpty() && !keterangan.isEmpty()){
                    if (inputStream!=null || photo!=null){
                        try {
                            uploadImage(nama,harga,keterangan);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(InsertFoodActivity.this, "Pilih Foto terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progressDialog.dismiss();
                    Snackbar.make(view,  "Jangan kosongi kolom", Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }

    private void bukakamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(cameraIntent, IMAGE_CODE_CAPTURE);
    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            photo = null;
            if(data != null) {
                try {
                    photo = null;
                    uri = data.getData();
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    Picasso.get().load(uri).centerCrop().fit().into(binding.gambarMakanan);
                    inputStream = is;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == IMAGE_CODE_CAPTURE && resultCode == RESULT_OK) {
            inputStream = null;
            imageuri = data.getData();
            photo = (Bitmap) data.getExtras().get("data");
            Picasso.get().load(createTempFile(photo)).centerCrop().fit().into(binding.gambarMakanan);
        }

    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + "_image.webp");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void uploadImage(String nama, String harga, String keterangan) throws IOException {

        MultipartBody.Part body = null;
        if (inputStream != null) {
            byte[] byt = getBytes(inputStream);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byt);
            body = MultipartBody.Part.createFormData("foto", "" + System.currentTimeMillis(), requestFile);

        } else if (photo != null) {
            File file = createTempFile(photo);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);
        }
        if (body!=null){
            ApiInterface insertbarang = ServiceGenerator.createService(ApiInterface.class);
            Call<ResponseBodyReq> call = insertbarang.insertbarang(body,nama,Integer.parseInt(harga),keterangan);
            call.enqueue(new Callback<ResponseBodyReq>() {
                @Override
                public void onResponse(Call<ResponseBodyReq> call, Response<ResponseBodyReq> response) {
                    if (response.isSuccessful()){
                        progressDialog.dismiss();
                        Intent intent = new Intent(InsertFoodActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Snackbar.make(view,  "gagal upload", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBodyReq> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar.make(view,  "gagal req server", Snackbar.LENGTH_LONG).show();
                    Log.d("sandy", "onFailure: "+t.getMessage());
                }
            });
        }

    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    openGallery();
//                }
//
//                return;
//            }
//        }
//    }

}