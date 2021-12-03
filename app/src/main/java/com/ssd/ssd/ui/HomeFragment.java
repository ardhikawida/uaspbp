package com.ssd.ssd.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ssd.ssd.DetailFoodActivity;
import com.ssd.ssd.InsertFoodActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.SplashScreenActivity;
import com.ssd.ssd.adapter.FoodAdapter;
import com.ssd.ssd.admin.AdminActivity;
import com.ssd.ssd.admin.DetailFoodAdminActivity;
import com.ssd.ssd.auth.LoginActivity;
import com.ssd.ssd.databinding.FragmentHomeBinding;
import com.ssd.ssd.model.FoodModels;
import com.ssd.ssd.model.ResponseBodyBarang;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FoodAdapter adapter;
    private ArrayList<FoodModels> foodModelsArrayList;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        binding.shimmer.startShimmer();

        getdata(view);


        return binding.getRoot();
    }

    void getdata(View view) {
        ApiInterface getbarang = ServiceGenerator.createService(ApiInterface.class);
        getbarang.getallbarang().enqueue(new Callback<ResponseBodyBarang>() {
            @Override
            public void onResponse(Call<ResponseBodyBarang> call, Response<ResponseBodyBarang> response) {
                if (response.isSuccessful()) {
                    binding.rvFood.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.shimmer.stopShimmer();
                    List<FoodModels> foodModels = response.body().getFoodModels();
                    generateDataList(foodModels);
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyBarang> call, Throwable t) {
                Toast.makeText(requireContext().getApplicationContext(),"gagal api",Toast.LENGTH_SHORT).show();
                Log.d("sandy", "onFailure: " + t.getMessage());
            }
        });

    }

    private void generateDataList(List<FoodModels> photoList){
        try {
            adapter = new FoodAdapter(photoList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext());

            binding.rvFood.setLayoutManager(layoutManager);

            binding.rvFood.setAdapter(adapter);

        }catch (Exception e){
            return;
        }

        adapter.setDialog(new FoodAdapter.Dialog() {
            @Override
            public void onClick(int position, String nama, Integer harga, String foto, String keterangan, Integer id) {
                Intent intent = new Intent(requireContext().getApplicationContext(), DetailFoodActivity.class);
                intent.putExtra("nama", nama);
                intent.putExtra("harga", harga);
                intent.putExtra("foto", foto);
                intent.putExtra("keterangan", keterangan);
                intent.putExtra("id_barang", id);
                startActivity(intent);
            }

        });



    }

}