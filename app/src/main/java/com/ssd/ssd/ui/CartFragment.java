package com.ssd.ssd.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.ssd.ssd.DetailFoodActivity;
import com.ssd.ssd.MainActivity;
import com.ssd.ssd.R;
import com.ssd.ssd.adapter.CartAdapter;
import com.ssd.ssd.adapter.FoodAdapter;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.entity.TransaksiEntity;
import com.ssd.ssd.databinding.FragmentCartBinding;
import com.ssd.ssd.model.BarangModel;
import com.ssd.ssd.model.FoodModels;
import com.ssd.ssd.model.ResponseSum;
import com.ssd.ssd.model.ResponseTransaksi;
import com.ssd.ssd.network.ApiInterface;
import com.ssd.ssd.network.ServiceGenerator;
import com.ssd.ssd.session.Preferences;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    private AppDatabase database;

    private CartAdapter adapter;
    private ArrayList<BarangModel> list;
    String CHANNEL_ID = "10001";
    String userId;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    private Integer harga_total = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        binding.getLifecycleOwner();
        progressDialog = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        database = AppDatabase.getInstance(requireContext().getApplicationContext());
        binding.shimmer.startShimmer();
        //GET API
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseTransaksi> call = apiInterface.gettransaksi(userId);
        call.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                try {
                    if (response.isSuccessful()) {
                        binding.rvcart.setVisibility(View.VISIBLE);
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


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.rvcart.setLayoutManager(layoutManager);
        binding.rvcart.setAdapter(adapter);

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Checkout ...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                ApiInterface checkout = ServiceGenerator.createService(ApiInterface.class);
                checkout.checkout(userId).enqueue(new Callback<ResponseSum>() {
                    @Override
                    public void onResponse(Call<ResponseSum> call, Response<ResponseSum> response) {
                        if (response.isSuccessful()) {

                            if (response.body().getData() == 1) {
                                progressDialog.dismiss();
                                notifikasi("checkout berhasil", "SSD Restaurant");
                                Snackbar.make(v, "Checkout berhasil", Snackbar.LENGTH_LONG).show();
                                onStart();
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(v, "checkout Gagal", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseSum> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("sandy", "onFailure: " + t.getMessage());
                    }
                });
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
        //GET API

            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<ResponseTransaksi> call = apiInterface.gettransaksi(userId);
            call.enqueue(new Callback<ResponseTransaksi>() {
                @Override
                public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                  try {
                      List<BarangModel> foodModels = response.body().getData();
                      generateDataList(foodModels);
                      if (response.isSuccessful()) {
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


        ApiInterface getsum = ServiceGenerator.createService(ApiInterface.class);
        getsum.getsum(userId).enqueue(new Callback<ResponseSum>() {
            @Override
            public void onResponse(Call<ResponseSum> call, Response<ResponseSum> response) {
                if (response.isSuccessful()) {
                    try {
                        binding.txthargacart.setText("Jumlah : Rp. " + response.body().getData());
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

    private void generateDataList(List<BarangModel> photoList) {
        try {
            adapter = new CartAdapter(requireContext().getApplicationContext(), photoList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext());

            binding.rvcart.setLayoutManager(layoutManager);

            binding.rvcart.setAdapter(adapter);

            adapter.setDialog(new CartAdapter.Dialog() {

                @Override
                public void onHapus(int position, Integer id) {
                    //untuk menghapus data
                    progressDialog.setTitle("Sedang menghapus ..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    ApiInterface hapustransaksi = ServiceGenerator.createService(ApiInterface.class);
                    hapustransaksi.hapustransaksi(id).enqueue(new Callback<ResponseSum>() {
                        @Override
                        public void onResponse(Call<ResponseSum> call, Response<ResponseSum> response) {
                            if (response.isSuccessful()) {
                                progressDialog.dismiss();
                                onStart();
                            } else {
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseSum> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onMines(int position, Integer jumlah, Integer harga, Integer id_barang) {
                    progressDialog.setTitle("Update data ..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    if (jumlah > 0) {
                        jumlah -= 1;
                        harga_total = (jumlah * harga);
                        ApiInterface update = ServiceGenerator.createService(ApiInterface.class);
                        update.updatetransaksi(userId, id_barang, harga_total, jumlah).enqueue(new Callback<ResponseSum>() {
                            @Override
                            public void onResponse(Call<ResponseSum> call, Response<ResponseSum> response) {
                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if (response.body().getData() == 1) {
                                        onStart();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseSum> call, Throwable t) {
                                progressDialog.dismiss();
                            }
                        });

                    }

                }

                @Override
                public void onPlus(int position, Integer jumlah, Integer harga, Integer id_barang) {
                    progressDialog.setTitle("Update data ..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    if (jumlah >= 0) {
                        jumlah += 1;
                        harga_total = (jumlah * harga);
                        ApiInterface update = ServiceGenerator.createService(ApiInterface.class);
                        update.updatetransaksi(userId, id_barang, harga_total, jumlah).enqueue(new Callback<ResponseSum>() {
                            @Override
                            public void onResponse(Call<ResponseSum> call, Response<ResponseSum> response) {
                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if (response.body().getData() == 1) {
                                        onStart();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseSum> call, Throwable t) {
                                progressDialog.dismiss();
                            }
                        });

                    }
                }

            });

        }catch (Exception e){
            return;
        }

    }

    public void notifikasi(String pesan, String pengirim) {
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}