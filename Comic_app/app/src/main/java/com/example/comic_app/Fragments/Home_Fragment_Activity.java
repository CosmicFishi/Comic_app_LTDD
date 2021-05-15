package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comic_app.ManageListViewComic;
import com.example.comic_app.R;
import com.example.comic_app.adapter.AdapterCategoryBook;
import com.example.comic_app.adapter.AdapterNewComicBook;
import com.example.comic_app.model.Category;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Home_Fragment_Activity extends ManageListViewComic {
    FirebaseFirestore firestore;
    RecyclerView rv_list_cates;
    TextView userName;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeView = inflater.inflate( R.layout.comic_home_page, container, false );

        bindUI(homeView);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser().getDisplayName().length() > 0)
            userName.setText("Hi " + mAuth.getCurrentUser().getDisplayName());

        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("comic_book").orderBy("view", Query.Direction.DESCENDING).limit(10);
        setCategoryAdapterRV();
        setResultData();
        return homeView;
    }

    private void bindUI(View view) {
        listView = (ListView) view.findViewById( R.id.comic_list );
        rv_list_cates = (RecyclerView)view.findViewById(R.id.rv_list_cates);
        userName = (TextView)view.findViewById(R.id.txt_u_name);
    }

    @Override
    protected ListAdapter setListAdapter(List<ComicBook> list) {
        return new AdapterNewComicBook(getActivity(), R.layout.comic, list);
    }

    public void setCategoryAdapterRV(){
        CollectionReference cateRef = firestore.collection("category");
        //set horizontal layout
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_list_cates.setLayoutManager(mLayoutManager);
        rv_list_cates.setItemAnimator(new DefaultItemAnimator());
        //set list adapter
        List<Category> listCategory = new ArrayList<>();
        AdapterCategoryBook adapterCategoryBook = new AdapterCategoryBook(listCategory);
        rv_list_cates.setAdapter(adapterCategoryBook);
        cateRef.limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(getActivity() == null) {
                            return;
                        } else {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    listCategory.add(document.toObject(Category.class));
                                }
                                adapterCategoryBook.notifyDataSetChanged();
                            } else {
                                Log.i("Error", "onComplete: Failed to get documents");
                            }
                        }
                    }
                });

    }
}