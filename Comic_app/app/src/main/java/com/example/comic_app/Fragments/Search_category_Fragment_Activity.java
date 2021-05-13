package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comic_app.ManageListViewComic;
import com.example.comic_app.R;
import com.example.comic_app.adapter.AdapterComicBook;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search_category_Fragment_Activity extends ManageListViewComic {
    FirebaseFirestore fireStore;
    TextView txt_cate_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View category_list_view = inflater.inflate(R.layout.comic_search_category_page,container,false);

        bindUI(category_list_view);
        Bundle bundle = this.getArguments();
        fireStore = FirebaseFirestore.getInstance();
        txt_cate_name.setText(bundle.getString("cate_name"));

        CollectionReference comic_book = fireStore.collection("comic_book");

        query = comic_book.whereArrayContains("category", bundle.getInt("cate_id"));
        setResultData(true);
        return category_list_view;
    }

    private void bindUI(View view) {
        txt_error = (TextView)view.findViewById(R.id.txt_error);
        listView =  (ListView)view.findViewById(R.id.listViewCategory);
        txt_cate_name = (TextView)view.findViewById(R.id.txt_cate_name);
    }

    @Override
    protected ListAdapter setListAdapter(List<ComicBook> list) {
        return new AdapterComicBook(getActivity(), R.layout.result_card, list);
    }
}