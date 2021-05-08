package com.example.comic_app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.comic_app.R;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdapterNewComicBook extends ArrayAdapter<ComicBook> {
    private int resourceLayout;
    private Context mContext;
    private FirebaseStorage storage;

    public AdapterNewComicBook(@NonNull Context context, int resource, @NonNull List<ComicBook> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
        this.storage = FirebaseStorage.getInstance();
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
        //lấy ref tới storage
        StorageReference sr = storage.getReference();

        ComicBook comicBook = getItem(position);

        if (comicBook != null) {
            ImageView imgComic = (ImageView) v.findViewById(R.id.img_comic);
            TextView textViewComicName = (TextView) v.findViewById(R.id.textViewComicName);
            TextView textViewView = (TextView) v.findViewById(R.id.textViewView);

            if (imgComic != null) {
                sr.child("images/" + comicBook.getImage()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri).into(imgComic);
                            }
                        });
            }

            if (textViewComicName != null) {
                textViewComicName.setText(comicBook.getTitle());
            }
            if (textViewView != null) {
                textViewView.setText("View "+String.valueOf(comicBook.getView()));
            }
        }

        return v;
    }
}
