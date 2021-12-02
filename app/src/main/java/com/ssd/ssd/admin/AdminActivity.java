package com.ssd.ssd.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ssd.ssd.InsertFoodActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.adapter.FoodAdapter;
import com.ssd.ssd.auth.LoginActivity;
import com.ssd.ssd.auth.RegisterActivity;
import com.ssd.ssd.auth.VerifikasiActivity;
import com.ssd.ssd.databinding.ActivityAdminBinding;
import com.ssd.ssd.model.FoodModels;
import com.ssd.ssd.model.ResponseBody;
import com.ssd.ssd.model.ResponseBodyBarang;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.model.UsersModel;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.session.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    private FoodAdapter adapter;
    private List<FoodModels> foodModelsArrayList;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin);
        binding.getLifecycleOwner();


        getdata(view);

        binding.btnaddmakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, InsertFoodActivity.class);
                startActivity(intent);
            }
        });

        binding.btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.setIsLogin(getBaseContext(),"");
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    void getdata(View view) {
        ApiInterface getbarang = ServiceGenerator.createService(ApiInterface.class);
        getbarang.getallbarang().enqueue(new Callback<ResponseBodyBarang>() {
            @Override
            public void onResponse(Call<ResponseBodyBarang> call, Response<ResponseBodyBarang> response) {
                if (response.isSuccessful()) {

                    List<FoodModels> foodModels = response.body().getFoodModels();
                    generateDataList(foodModels);
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyBarang> call, Throwable t) {
                Toast.makeText(AdminActivity.this,"gagal api",Toast.LENGTH_SHORT).show();
                Log.d("sandy", "onFailure: " + t.getMessage());
            }
        });

    }

    private void generateDataList(List<FoodModels> photoList){
        adapter = new FoodAdapter(photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminActivity.this);

        binding.rvFood.setLayoutManager(layoutManager);

        binding.rvFood.setAdapter(adapter);

        adapter.setDialog(new FoodAdapter.Dialog() {
            @Override
            public void onClick(int position, String nama, Integer harga, String foto, String keterangan, Integer id) {
                Intent intent = new Intent(AdminActivity.this, DetailFoodAdminActivity.class);
                intent.putExtra("nama", nama);
                intent.putExtra("harga", harga);
                intent.putExtra("foto", foto);
                intent.putExtra("keterangan", keterangan);
                intent.putExtra("id", id);
                startActivity(intent);
            }

        });



    }
}