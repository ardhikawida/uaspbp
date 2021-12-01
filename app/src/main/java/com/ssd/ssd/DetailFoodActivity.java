package com.ssd.ssd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.ssd.ssd.database.AppDatabase;
import com.ssd.ssd.database.entity.TransaksiEntity;
import com.ssd.ssd.databinding.ActivityDetailFoodBinding;
import com.ssd.ssd.session.Preferences;

public class DetailFoodActivity extends AppCompatActivity {

    ActivityDetailFoodBinding binding;
    private Integer jumlah_counter = 0;
    private String nama,foto,deskripsi;
    private Integer harga,id_users;

    int hargatotal = 0;


    private AppDatabase database;

    String CHANNEL_ID="10001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail_food);
        binding.getLifecycleOwner();
        database = AppDatabase.getInstance(getApplicationContext());


        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        foto = intent.getStringExtra("foto");
        deskripsi = intent.getStringExtra("deskripsi");
        harga = intent.getIntExtra("harga",0);
        id_users = Preferences.getIsIdUser(this);

        binding.txtnama.setText(nama);
        binding.txtharga.setText("Harga : Rp. "+harga.toString());
        binding.txtdeskripsi.setText("Deskripsi : "+deskripsi);

        Picasso.get().load(foto).fit().into(binding.imageView);

        binding.btnmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah_counter > 0) {
                    jumlah_counter -= 1;
                    hargatotal = (jumlah_counter * harga);
                    binding.txtcounter.setText(jumlah_counter.toString());
                    binding.txtjumlah.setText("Jumlah : " +jumlah_counter.toString());


                }
            }
        });

        binding.btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah_counter >= 0) {
                    jumlah_counter += 1;
                    hargatotal = (jumlah_counter * harga);
                    binding.txtcounter.setText(jumlah_counter.toString());
                    binding.txtjumlah.setText("Jumlah : " +jumlah_counter.toString());

                }
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransaksiEntity checkfood = database.transaksiDao().checkfood(Preferences.getIsEmail(getBaseContext()),nama,"proses");
                if (checkfood == null){
                    database.transaksiDao().insertcart(Preferences.getIsEmail(getBaseContext()), nama,harga,hargatotal,jumlah_counter,"proses");
                    notifikasi(nama + "telah ditambahkan di keranjang", "SSD Restaurant");
                    Toast.makeText(getBaseContext(), "Tambah Makanan berhasil", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    database.transaksiDao().update(Preferences.getIsEmail(getBaseContext()),jumlah_counter,hargatotal,nama,"proses");
                    Toast.makeText(getBaseContext(), "Update Berhasil", Toast.LENGTH_LONG).show();
                    finish();
                }


            }
        });


    }

    public void notifikasi(String pesan, String pengirim)
    {
        String notification_title = pengirim;
        String notification_message = pesan;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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