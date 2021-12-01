package com.ssd.ssd.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssd.ssd.DetailFoodActivity;
import com.ssd.ssd.InsertFoodActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.SplashScreenActivity;
import com.ssd.ssd.adapter.FoodAdapter;
import com.ssd.ssd.auth.LoginActivity;
import com.ssd.ssd.databinding.FragmentHomeBinding;
import com.ssd.ssd.model.FoodModels;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FoodAdapter adapter;
    private ArrayList<FoodModels> foodModelsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);


        addData();

        adapter = new FoodAdapter(foodModelsArrayList);

        adapter.setDialog(new FoodAdapter.Dialog() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(requireContext().getApplicationContext(), DetailFoodActivity.class);
                intent.putExtra("nama", String.valueOf(foodModelsArrayList.get(position).getNama()));
                intent.putExtra("harga", foodModelsArrayList.get(position).getHarga());
//                intent.putExtra("foto", String.valueOf(foodModelsArrayList.get(position).getGambar()));
//                intent.putExtra("deskripsi", String.valueOf(foodModelsArrayList.get(position).getDeskripsi()));
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());

        binding.rvFood.setLayoutManager(layoutManager);

        binding.rvFood.setAdapter(adapter);

        binding.btnaddmakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext().getApplicationContext(), InsertFoodActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }

    void addData(){
//        foodModelsArrayList = new ArrayList<>();
//        foodModelsArrayList.add(new FoodModels("Nasi Goreng Seafood", Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +R.drawable.nasigoreng).toString(),"Pedas Manis" ,20000));
//        foodModelsArrayList.add(new FoodModels("Rendang", Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +R.drawable.rendang).toString(), "Pedas Gurih",30000));
//        foodModelsArrayList.add(new FoodModels("Sate Kambing", Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +R.drawable.satekambing).toString(), "Pedas Sedap",32000));
    }
}