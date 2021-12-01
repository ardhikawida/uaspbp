package com.ssd.ssd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssd.ssd.R;
import com.ssd.ssd.database.entity.TransaksiEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewAdapter> {
    private List<TransaksiEntity> list;
    private Context context;
    private Dialog dialog;
    private Dialog btnhapus;
    private ArrayList<TransaksiEntity> TransaksiList;

    public interface Dialog{
        void onHapus(int position);
    }


    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public OrderAdapter(Context context, List<TransaksiEntity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart, parent, false);
        return new ViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewAdapter holder, int position) {
        Integer jumlah = list.get(position).jumlah;
        Integer hargatotal = list.get(position).harga_total;
        Integer harga = list.get(position).harga;
        String nama = list.get(position).nama;

        holder.nama.setText(list.get(position).nama);
        holder.jumlah.setText(jumlah.toString());
        holder.harga.setText("Harga : " + list.get(position).harga + "x" + list.get(position).jumlah );
        holder.total.setText(String.format(context.getString(R.string.total)) + " : Rp. " + hargatotal);

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null){
                    dialog.onHapus(holder.getLayoutPosition());
                }
            }
        });


    }

    public void filterList(ArrayList<TransaksiEntity> filteredList) {
        TransaksiList = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewAdapter extends RecyclerView.ViewHolder {

        TextView nama, jumlah, harga, total,txtmin,txtplus;
        RelativeLayout btndelete;

        public ViewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txt_nama);
            jumlah = itemView.findViewById(R.id.txtcounter);
            harga = itemView.findViewById(R.id.txthargacart);
            total = itemView.findViewById(R.id.txttotal);
            btndelete = itemView.findViewById(R.id.btndelete);
            txtmin = itemView.findViewById(R.id.txtmin);
            txtplus = itemView.findViewById(R.id.txtplus);

        }
    }
}
