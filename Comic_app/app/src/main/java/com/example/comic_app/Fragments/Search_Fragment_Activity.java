package com.example.comic_app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.ManageListViewComic;
import com.example.comic_app.R;
import com.example.comic_app.Utils;
import com.example.comic_app.adapter.AdapterComicBook;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search_Fragment_Activity extends ManageListViewComic {
    FirebaseFirestore fireStore;
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View search_view =inflater.inflate(R.layout.comic_search_page,container,false);
        bindUI(search_view);
        fireStore = FirebaseFirestore.getInstance();

        search("");

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listView.setAdapter(null);
                    search(editText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        return search_view;
    }

    private void search(String kw) {
        kw = kw.toUpperCase();
        if(kw.length() != 0) {
            query = fireStore.collection("comic_book")
                    .orderBy("title").startAt(kw).limit(10);
            setResultData(getContext(), getActivity());
        } else {
            query = fireStore.collection("comic_book")
                    .orderBy("title", Query.Direction.DESCENDING).limit(10);
            setResultData(getContext(), getActivity());
        }
    }

    private void bindUI(View view) {
        listView =  (ListView)view.findViewById(R.id.listView);
        editText = (EditText)view.findViewById(R.id.et_search_bar);
    }

    @Override
    protected ListAdapter setListAdapter(List<ComicBook> list) {
        return new AdapterComicBook(getActivity(), R.layout.result_card, list);
    }
}