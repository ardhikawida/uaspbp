package com.ssd.ssd.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssd.ssd.DetailFoodActivity;
import com.ssd.ssd.MainActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.auth.LoginActivity;
import com.ssd.ssd.auth.VerifikasiActivity;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.entity.UserEntity;
import com.ssd.ssd.databinding.FragmentProfileBinding;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.model.UsersModel;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.session.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AppDatabase database;
    ProgressDialog progressDialog;
    String uid;
    FirebaseUser userAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.getLifecycleOwner();

        progressDialog = ProgressDialog.show(getActivity(), "", "loading ....");
        progressDialog.setCanceledOnTouchOutside(false);

        database = AppDatabase.getInstance(requireContext().getApplicationContext());

        uid = FirebaseAuth.getInstance().getUid();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        getdataapi(uid);
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Preferences.setIsLogin(getContext(), "");
                Intent intent = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getActivity(), "", "Update data");
                progressDialog.setCanceledOnTouchOutside(false);
                String firstname = binding.txtfirstname.getText().toString().trim();
                String lastname = binding.txtnamabelakang.getText().toString().trim();
                String nohp = binding.txtnotelp.getText().toString().trim();
                String email = binding.txtmail.getText().toString().trim();
                String password = binding.txtpassword.getText().toString().trim();
                String alamat = binding.txtalamat.getText().toString().trim();
                String kota = binding.txtkota.getText().toString().trim();


                if (!firstname.isEmpty() && !lastname.isEmpty() && !nohp.isEmpty() && !email.isEmpty()
                        && !password.isEmpty() && !alamat.isEmpty() && !kota.isEmpty()) {
                    //cek password
                    ApiInterface detailakun = ServiceGenerator.createService(ApiInterface.class);
                    Call<ResponseBodyReq> call = detailakun.getdetailakun(uid);
                    call.enqueue(new Callback<ResponseBodyReq>() {
                        @Override
                        public void onResponse(Call<ResponseBodyReq> call, Response<ResponseBodyReq> response) {
                            if (response.isSuccessful()) {
                                ResponseBodyReq user = response.body();
                                if (user.getUsersmodel().getPassword().equals(password)){
                                    updateakun(firstname,lastname,nohp,email,alamat,kota,password,v);
                                }else {
                                    //ganti password firebase
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(userAuth.getEmail(),user.getUsersmodel().getPassword());
                                    userAuth.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                userAuth.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            updateakun(firstname,lastname,nohp,email,alamat,kota,password,v);
                                                        }else {
                                                            Snackbar.make(v, "Error", Snackbar.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }else {
                                                Snackbar.make(v, "Auth gagal", Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    //--------endgantipassword========
                                }
                            }else {
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBodyReq> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.d("sandy", "onFailure: "+t.getMessage());
                        }
                    });
                    //========endcek password


                }
            }
        });
        return binding.getRoot();
    }

    private void getdataapi(String uid) {
        ApiInterface detailakun = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBodyReq> call = detailakun.getdetailakun(uid);
        call.enqueue(new Callback<ResponseBodyReq>() {
            @Override
            public void onResponse(Call<ResponseBodyReq> call, Response<ResponseBodyReq> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    ResponseBodyReq user = response.body();
                    binding.txtfirstname.setText(user.getUsersmodel().getFirstname());
                    binding.txtnamabelakang.setText(user.getUsersmodel().getLastname());
                    binding.txtnotelp.setText(user.getUsersmodel().getNohp());
                    binding.txtmail.setText(user.getUsersmodel().getEmail());
                    binding.txtpassword.setText(user.getUsersmodel().getPassword());
                    binding.txtalamat.setText(user.getUsersmodel().getAlamat());
                    binding.txtkota.setText(user.getUsersmodel().getKota());
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyReq> call, Throwable t) {

            }
        });
    }

    private void updateakun(String firstname,String lastname, String nohp, String email, String alamat, String kota, String password, View v){
        //update data akun mysql
        ApiInterface updateakun = ServiceGenerator.createService(ApiInterface.class);
        Call<com.ssd.ssd.model.Response> call = updateakun.updateusers(FirebaseAuth.getInstance().getUid(), firstname, lastname,
                nohp, email, alamat, kota, password);

        call.enqueue(new Callback<com.ssd.ssd.model.Response>() {
            @Override
            public void onResponse(Call<com.ssd.ssd.model.Response> call, Response<com.ssd.ssd.model.Response> response) {
                progressDialog.dismiss();
                Snackbar.make(v, "Update berhasil", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<com.ssd.ssd.model.Response> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(v, "Update gagal", Snackbar.LENGTH_LONG).show();
                Log.d("sandy", "onFailure: "+t.getMessage());
            }
        });
        //=========end data akun mysql
    }
}