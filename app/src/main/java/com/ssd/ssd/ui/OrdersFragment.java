package com.ssd.ssd.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.ssd.ssd.R;
import com.ssd.ssd.adapter.OrderAdapter;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.entity.TransaksiEntity;
import com.ssd.ssd.databinding.FragmentOrdersBinding;
import com.ssd.ssd.session.Preferences;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private AppDatabase database;
    private OrderAdapter orderAdapter;
    private List<TransaksiEntity> list = new ArrayList<>();

    private Integer harga_total = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_orders,container,false);
        binding.getLifecycleOwner();


        database = AppDatabase.getInstance(requireContext().getApplicationContext());
        list.clear();
        list.addAll(database.transaksiDao().getorders(Preferences.getIsEmail(requireContext()),"selesai"));

        orderAdapter = new OrderAdapter(requireContext().getApplicationContext(), list);
        orderAdapter.setDialog(new OrderAdapter.Dialog() {

            @Override
            public void onHapus(int position) {
                //untuk menghapus data
                TransaksiEntity transaksi = list.get(position);
                database.transaksiDao().delete(transaksi);
                onStart();

            }


        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.rvorder.setLayoutManager(layoutManager);
        binding.rvorder.setAdapter(orderAdapter);


        return  binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Integer sum = database.transaksiDao().getsumorder(Preferences.getIsEmail(requireContext()),"selesai");
        if (sum == null){
            binding.txthargaorder.setText("Dibayar : Rp. 0" );
        }else {
            binding.txthargaorder.setText("Dibayar : Rp. " + sum);
        }
    }
}