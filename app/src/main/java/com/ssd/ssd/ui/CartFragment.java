package com.ssd.ssd.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ssd.ssd.MainActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.adapter.CartAdapter;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.entity.TransaksiEntity;
import com.ssd.ssd.databinding.FragmentCartBinding;
import com.ssd.ssd.session.Preferences;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;


public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    private AppDatabase database;

    private CartAdapter cartAdapter;
    private List<TransaksiEntity> list = new ArrayList<>();
    String CHANNEL_ID="10001";

    private Integer harga_total = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        binding.getLifecycleOwner();

        database = AppDatabase.getInstance(requireContext().getApplicationContext());
        list.clear();
        list.addAll(database.transaksiDao().getcart(Preferences.getIsEmail(requireContext()),"proses"));

        cartAdapter = new CartAdapter(requireContext().getApplicationContext(), list);
        cartAdapter.setDialog(new CartAdapter.Dialog() {

            @Override
            public void onHapus(int position) {
                //untuk menghapus data
                TransaksiEntity transaksi = list.get(position);
                database.transaksiDao().delete(transaksi);
                onStart();

            }

            @Override
            public void onMines(int position, Integer jumlah, Integer harga, String nama) {
                if (jumlah > 0) {
                    jumlah -= 1;
                    harga_total = (jumlah * harga);
                    database.transaksiDao().update(Preferences.getIsEmail(requireContext()),jumlah,harga_total,nama,"proses");
                    onStart();

                }
                TransaksiEntity transaksi = list.get(position);
                database.transaksiDao().update(Preferences.getIsEmail(requireContext()),jumlah,harga_total,nama,"proses");
                    onStart();
            }

            @Override
            public void onPlus(int position, Integer jumlah, Integer harga, String nama) {
                if (jumlah >= 0) {
                    jumlah += 1;
                    harga_total = (jumlah * harga);
                    database.transaksiDao().update(Preferences.getIsEmail(requireContext()),jumlah,harga_total,nama,"proses");
                    onStart();

                }

            }


        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.rvcart.setLayoutManager(layoutManager);
        binding.rvcart.setAdapter(cartAdapter);

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.transaksiDao().deleteorder(Preferences.getIsEmail(requireContext()),"selesai");
                database.transaksiDao().checkout("selesai",Preferences.getIsEmail(requireContext()));
                notifikasi("checkout berhasil", "SSD Restaurant");
                Snackbar.make(v, "Checkout berhasil", Snackbar.LENGTH_LONG).show();
                onStart();
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        list.clear();
        list.addAll(database.transaksiDao().getcart(Preferences.getIsEmail(requireContext()),"proses"));
        Log.d("tester",Preferences.getIsIdUser(requireContext()).toString());
        cartAdapter.notifyDataSetChanged();
        Integer sum = database.transaksiDao().getsum(Preferences.getIsEmail(requireContext()),"proses");
        if (sum == null){
            binding.txthargacart.setText("Jumlah : Rp. 0" );
        }else {
            binding.txthargacart.setText("Jumlah : Rp. " + sum);
        }
    }

    public void notifikasi(String pesan, String pengirim)
    {
        String notification_title = pengirim;
        String notification_message = pesan;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(requireActivity())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, 0);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        requireActivity(),
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) requireActivity().getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}