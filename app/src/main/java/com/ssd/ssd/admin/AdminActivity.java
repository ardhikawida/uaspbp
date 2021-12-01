package com.ssd.ssd.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.ssd.ssd.R;
import com.ssd.ssd.adapter.FoodAdapter;
import com.ssd.ssd.databinding.ActivityAdminBinding;
import com.ssd.ssd.model.FoodModels;
import com.ssd.ssd.model.ResponseBody;
import com.ssd.ssd.model.ResponseBodyBarang;
import com.ssd.ssd.model.ResponseBodyReq;
import com.ssd.ssd.model.UsersModel;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;

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
                Snackbar.make(view, "gagal get api" , Snackbar.LENGTH_LONG).show();
                Log.d("sandy", "onFailure: " + t.getMessage());
            }
        });

    }

    private void generateDataList(List<FoodModels> photoList){
        adapter = new FoodAdapter(photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminActivity.this);

        binding.rvFood.setLayoutManager(layoutManager);

        binding.rvFood.setAdapter(adapter);

    }
}