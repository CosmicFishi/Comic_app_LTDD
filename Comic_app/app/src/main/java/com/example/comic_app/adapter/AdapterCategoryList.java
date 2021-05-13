package com.example.comic_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comic_app.R;
import com.example.comic_app.model.Category;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterCategoryList extends ArrayAdapter<Category> {
    private int resourceLayout;
    public AdapterCategoryList(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    resourceLayout, parent, false);
        }

        Category cate = getItem(position);

        TextView cateName = (TextView) convertView.findViewById(R.id.text_view_cate_name);
        cateName.setText(cate.getCategoryName());

        return convertView;
    }
    
//    public AdapterCategoryList(Context context, List<Category> categoryList) {
//        super(context, 0, categoryList);
//    }
}
