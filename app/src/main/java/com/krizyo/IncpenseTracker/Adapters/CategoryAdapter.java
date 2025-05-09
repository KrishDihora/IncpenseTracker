package com.krizyo.IncpenseTracker.Adapters;

import android.content.Context;
import android.icu.util.ULocale;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krizyo.IncpenseTracker.Models.CategoryModel;
import com.krizyo.IncpenseTracker.R;
import com.krizyo.IncpenseTracker.databinding.LayoutCategoryItemBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<CategoryModel> categories;
    CategoryClickListener categoryClickListener;
    public interface CategoryClickListener{
        void onCategoryClicked(CategoryModel categoryModel);
    }

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categories,CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categories = categories;
        this.categoryClickListener=categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.binding.categoryText.setText(categories.get(position).getCategoryName());
        holder.binding.categoryIcon.setImageResource(categories.get(position).getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(categories.get(position).getCategoryColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickListener.onCategoryClicked(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        LayoutCategoryItemBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = LayoutCategoryItemBinding.bind(itemView);
        }
    }
}
