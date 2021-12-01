package com.ssd.ssd.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssd.ssd.MainActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.session.Preferences;

public class VerifikasiActivity extends AppCompatActivity {
        FirebaseAuth auth;
        TextView status;
        Button btnverifikasi,btnlogout;
        ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);
        progressDialog = ProgressDialog.show(VerifikasiActivity.this,"","Loading ...");
        progressDialog.setCanceledOnTouchOutside(false);

        status = findViewById(R.id.txtstatus);
        btnverifikasi = findViewById(R.id.btn_verifikasi);
        btnlogout = findViewById(R.id.btn_logout);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        status.setText("Status : "+ user.isEmailVerified());

        updatestatus();

        btnverifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(VerifikasiActivity.this,"","Loading ...");
                progressDialog.setCanceledOnTouchOutside(false);
                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updatestatus();
                    }
                });
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Preferences.setIsLogin(getBaseContext(),"");
                Intent intent = new Intent(VerifikasiActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updatestatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified() == true){
            progressDialog.dismiss();
            Intent intent = new Intent(VerifikasiActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            progressDialog.dismiss();
            Toast.makeText(getBaseContext(),"silahkan verifikasi email", Toast.LENGTH_SHORT).show();
        }
    }
}