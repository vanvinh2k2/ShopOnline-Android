package com.example.shoponline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline.R;
import com.example.shoponline.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductHotAdapter extends RecyclerView.Adapter<ProductHotAdapter.HolderProduct>{
    Context context;
    List<Product> list;

    public ProductHotAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_hot, parent, false);
        return new HolderProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProduct holder, int position) {
        Product product = list.get(position);
        Picasso.get().load(product.getImage())
                .placeholder(R.drawable.google_20)
                .into(holder.img);
        holder.title.setText(product.getTitle());
        holder.rating.setText("2.00");
        holder.vendor.setText("By: "+product.getVendor().getTitle());
        holder.price.setText("Price: "+product.getPrice()+"$");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderProduct extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title, rating, vendor, price;
        public HolderProduct(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rate);
            vendor = itemView.findViewById(R.id.vendor);
            price = itemView.findViewById(R.id.price);
        }
    }
}
