package com.ssd.ssd.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssd.ssd.R;
import com.ssd.ssd.model.FoodModels;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    private List<FoodModels> dataList;
    private Dialog dialog;


    public interface Dialog {
        void onClick(int position, String nama,Integer harga, String foto, String keterangan, Integer id);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }


    public FoodAdapter(List<FoodModels> dataList) {
        this.dataList = dataList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_transaksi, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtHarga.setText("Rp. " + dataList.get(position).getHarga().toString());
        Log.d("sandy", "onBindViewHolder: "+ dataList.get(position).getFoto());
        Picasso.get().load("http://192.168.1.7/"+dataList.get(position).getFoto()).centerCrop().fit().into(holder.imgMakanan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.onClick(holder.getLayoutPosition(),dataList.get(position).getNama(),dataList.get(position).getHarga(),dataList.get(position).getFoto(),dataList.get(position).getKeterangan(),dataList.get(position).getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga;
        private ImageView imgMakanan;

        public FoodViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama);
            txtHarga = (TextView) itemView.findViewById(R.id.txt_harga);
            imgMakanan = (ImageView) itemView.findViewById(R.id.img_makanan);



        }
    }
}