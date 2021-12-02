package com.ssd.ssd.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssd.ssd.R;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.databinding.ActivityRegisterBinding;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.model.UsersModel;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.session.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private AppDatabase database;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    List<UsersModel> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.getLifecycleOwner();

        firebaseAuth = FirebaseAuth.getInstance();
        database = AppDatabase.getInstance(this);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(RegisterActivity.this,"Loading","...");
                progressDialog.setCanceledOnTouchOutside(false);
                String firstname = binding.edtfirstname.getText().toString().trim();
                String nama_belakang = binding.edtnamabelakang.getText().toString().trim();
                String no_hp = binding.edtnohp.getText().toString().trim();
                String email = binding.edtemail.getText().toString().trim();
                String password = binding.edtpassword.getText().toString().trim();
                String alamat = binding.edtalamat.getText().toString().trim();
                String kota = binding.edtkota.getText().toString().trim();

                if (!firstname.isEmpty() && !nama_belakang.isEmpty() && !no_hp.isEmpty()
                        && !email.isEmpty() && !password.isEmpty() && !alamat.isEmpty() && !kota.isEmpty()){
                        //register firebase
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((task -> {
                        if (task.isSuccessful()){
                            //send verification email
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    ApiInterface setregister = ServiceGenerator.createService(ApiInterface.class);
                                    Call<ResponseBodyReq> call = setregister.registerusers(
                                            firebaseAuth.getUid(),
                                            firstname,
                                            nama_belakang,
                                            no_hp,
                                            email,
                                            alamat,
                                            kota,
                                            password
                                    );

                                    call.enqueue(new Callback<ResponseBodyReq>() {
                                        @Override
                                        public void onResponse(Call<ResponseBodyReq> call, Response<ResponseBodyReq> response) {
                                            try {
                                                Log.d("sandy", "onResponse: "+response.body());

                                                if (response.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Preferences.setIsLogin(getBaseContext(),"verifikasi");
                                                    Toast.makeText(RegisterActivity.this,"email sudah dikirim",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, VerifikasiActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else {
                                                    progressDialog.dismiss();
                                                    Log.d("sandy", "onResponse: "+response.body());
                                                    Toast.makeText(RegisterActivity.this,"gagal",Toast.LENGTH_SHORT).show();
                                                }

                                            }catch (Exception e){
                                                Log.d("sandy" ,"error"+e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBodyReq> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Log.d("alfan", "onResponse: "+t.getMessage());
                                            Toast.makeText(RegisterActivity.this,"gagal",Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(view, "gagal" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                        //==========

                            Snackbar.make(view, "Sukses ", Snackbar.LENGTH_LONG).show();
                        }else {
                            Snackbar.make(view, "Gagal ", Snackbar.LENGTH_LONG).show();
                        }
                    }));

                    //==========
                }else {
                    Snackbar.make(view, "Isi semua kolom ", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }


}