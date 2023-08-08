package com.example.shoponline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline.HomeFragment;
import com.example.shoponline.R;
import com.example.shoponline.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.HolderCategory>{
    HomeFragment context;
    List<Category> list;

    public CategoryAdapter(HomeFragment context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new HolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        Category category = list.get(position);
        Picasso.get().load(category.getImage())
                .placeholder(R.drawable.edit_facebook)
                .into(holder.img);
        holder.title.setText(category.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderCategory extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
        }
    }
}
