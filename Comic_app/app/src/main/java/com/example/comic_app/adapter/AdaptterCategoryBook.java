package com.example.comic_app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.comic_app.R;
import com.example.comic_app.model.Category;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdaptterCategoryBook extends ArrayAdapter<Category> {
    private int resourceLayout;
    private Context mContext;

    public AdaptterCategoryBook(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Category category = getItem(position);

        if (category != null) {
            Button btnCategory = (Button) v.findViewById(R.id.btnCategory);

            if (btnCategory != null) {
                btnCategory.setText(category.getCategoryName());
            }
        }

        return v;
    }
}
