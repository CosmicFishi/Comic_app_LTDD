package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.ManageListViewComic;
import com.example.comic_app.R;
import com.example.comic_app.adapter.AdapterComicBook;
import com.example.comic_app.adapter.AdapterNewComicBook;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Home_Fragment_Activity extends ManageListViewComic {
    FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeView = inflater.inflate( R.layout.comic_home_page, container, false );
        bindUI(homeView);

        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("comic_book").orderBy("view").limit(10);
        setResultData(getContext(), getActivity());
        return homeView;
    }

    private ListView bindUI(View view) {
        listView = (ListView) view.findViewById( R.id.comic_list );
        return listView;
    }

    @Override
    protected ListAdapter setListAdapter(List<ComicBook> list) {
        return new AdapterNewComicBook(getActivity(), R.layout.comic, list);
    }
}