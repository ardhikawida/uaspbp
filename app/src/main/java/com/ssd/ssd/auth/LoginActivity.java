package com.ssd.ssd.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ssd.ssd.MainActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.admin.AdminActivity;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.dao.UsersDao;
import com.ssd.ssd.database.entity.UserEntity;
import com.ssd.ssd.databinding.ActivityLoginBinding;
import com.ssd.ssd.session.Preferences;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AppDatabase database;
    private List<UserEntity> list = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.getLifecycleOwner();

        database = AppDatabase.getInstance(this);
        firebaseAuth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(LoginActivity.this,"","Loading ...");
                progressDialog.setCanceledOnTouchOutside(false);

                String edtmail = binding.edtemail.getText().toString();
                String edtpassword = binding.edtpassword.getText().toString();

                if (!edtmail.isEmpty() && !edtpassword.isEmpty()){
                    if (edtmail.equals("admin") && edtpassword.equals("admin")){
                        Preferences.setIsLogin(getBaseContext(),"admin");
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        firebaseAuth.signInWithEmailAndPassword(edtmail,edtpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                        Preferences.setIsLogin(getBaseContext(),"sukses");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Preferences.setIsLogin(getBaseContext(),"verifikasi");
                                        Intent intent = new Intent(LoginActivity.this, VerifikasiActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }else {
                                    Snackbar.make(v, "cek email dan password" , Snackbar.LENGTH_LONG).show();
                                }

                            }
                        });

                    }

                }else {
                    progressDialog.dismiss();
                    Snackbar.make(v, "isi semua kolom" , Snackbar.LENGTH_LONG).show();
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getIsLogin(getBaseContext()).equals("sukses")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (Preferences.getIsLogin(getBaseContext()).equals("verifikasi")){
            Intent intent = new Intent(LoginActivity.this, VerifikasiActivity.class);
            startActivity(intent);
            finish();
        }else if (Preferences.getIsLogin(getBaseContext()).equals("admin")){
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        }
    }
}