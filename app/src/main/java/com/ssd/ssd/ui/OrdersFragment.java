package com.ssd.ssd.ui;

import android.app.ProgressDialog;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.ssd.ssd.R;
import com.ssd.ssd.adapter.CartAdapter;
import com.ssd.ssd.adapter.OrderAdapter;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.entity.TransaksiEntity;
import com.ssd.ssd.databinding.FragmentOrdersBinding;
import com.ssd.ssd.model.BarangModel;
import com.ssd.ssd.model.ResponseSum;
import com.ssd.ssd.model.ResponseTransaksi;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.session.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private AppDatabase database;
    private OrderAdapter adapter;
    private ArrayList<BarangModel> list;
    private Integer harga_total = 0;
    String userId;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_orders,container,false);
        binding.getLifecycleOwner();
        binding.shimmer.startShimmer();
        progressDialog = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        //GET API
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseTransaksi> call = apiInterface.getcheckout(userId);
        call.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
            try {
                if (response.isSuccessful()) {
                    binding.rvorder.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.shimmer.stopShimmer();
                    List<BarangModel> foodModels = response.body().getData();
                    generateDataList(foodModels);
                } else {
                    Toast.makeText(requireContext().getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                return;
            }

            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.d("sandy", "onResponse: " + t.getMessage());
            }
        });
        //END GET API


//        orderAdapter.setDialog(new OrderAdapter.Dialog() {
//
//            @Override
//            public void onHapus(int position) {
//                //untuk menghapus data
//                TransaksiEntity transaksi = list.get(position);
//                database.transaksiDao().delete(transaksi);
//                onStart();
//
//            }
//
//
//        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.rvorder.setLayoutManager(layoutManager);
        binding.rvorder.setAdapter(adapter);


        return  binding.getRoot();
    }
    private void generateDataList(List<BarangModel> photoList) {
       try {
           adapter = new OrderAdapter(requireContext().getApplicationContext(), photoList);
           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext());

           binding.rvorder.setLayoutManager(layoutManager);

           binding.rvorder.setAdapter(adapter);

       }catch (Exception e){
           return;
       }

    }

    @Override
    public void onStart() {
        super.onStart();

        ApiInterface getsum = ServiceGenerator.createService(ApiInterface.class);
        getsum.getcheckoutsum(userId).enqueue(new Callback<ResponseSum>() {
            @Override
            public void onResponse(Call<ResponseSum> call, Response<ResponseSum> response) {
                if (response.isSuccessful()) {
                    try {
                        binding.txthargaorder.setText("Jumlah : Rp. " + response.body().getData());
                    }catch (Exception e){
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSum> call, Throwable t) {

            }
        });
    }
}