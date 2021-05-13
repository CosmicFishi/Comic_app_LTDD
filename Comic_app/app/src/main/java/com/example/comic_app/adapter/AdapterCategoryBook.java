package com.example.comic_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comic_app.R;
import com.example.comic_app.model.Category;

import java.util.List;

public class AdapterCategoryBook extends RecyclerView.Adapter<AdapterCategoryBook.cateViewHolder> {
    private int resourceLayout;
    private Context mContext;

    private List<Category> categoryList;

    class cateViewHolder extends RecyclerView.ViewHolder {
        private Button btnCategory;
        public cateViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCategory = (Button) itemView.findViewById(R.id.btnCategory);
        }
    }

    @NonNull
    @Override
    public cateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_category,parent,false);
        return new cateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cateViewHolder holder, int position) {
        Category category = categoryList.get(position);

        if (category != null) {
            holder.btnCategory.setText(category.getCategoryName());
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public AdapterCategoryBook(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
