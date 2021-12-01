package com.ssd.ssd.adapter;

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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    private ArrayList<FoodModels> dataList;
    private Dialog dialog;


    public interface Dialog {
        void onClick(int position);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }


    public FoodAdapter(ArrayList<FoodModels> dataList) {
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
        Picasso.get().load(dataList.get(position).getGambar()).into(holder.imgMakanan);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });

        }
    }
}