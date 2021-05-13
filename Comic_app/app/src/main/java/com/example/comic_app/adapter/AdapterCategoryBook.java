package com.example.comic_app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comic_app.Fragments.Search_category_Fragment_Activity;
import com.example.comic_app.MainActivity;
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
            holder.btnCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    if(v.getContext() != null) {
                        MainActivity mainActivity = (MainActivity) v.getContext();
                        b.putInt("cate_id", category.getId());
                        b.putString("cate_name", category.getCategoryName());
                        Fragment category_list = new Search_category_Fragment_Activity();
                        category_list.setArguments(b);
                        mainActivity.changeToCategoryPage(category_list);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

//    public AdapterCategoryBook(@NonNull Context context, int resource, @NonNull List<Category> objects) {
//        super(context, resource, objects);
//        this.resourceLayout = resource;
//        this.mContext = context;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View v = convertView;
//
//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(mContext);
//            v = vi.inflate(resourceLayout, null);
//        }
//
//        Category category = getItem(position);
//
//        if (category != null) {
//            Button btnCategory = (Button) v.findViewById(R.id.btnCategory);
//
//            if (btnCategory != null) {
//                btnCategory.setText(category.getCategoryName());
//            }
//        }
//
//        return v;
//    }

    public AdapterCategoryBook(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
