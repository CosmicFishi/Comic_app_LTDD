package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.ManageListViewComic;
import com.example.comic_app.R;
import com.example.comic_app.adapter.AdapterComicBook;
import com.example.comic_app.model.ComicBook;
import com.example.comic_app.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Favorite_Fragment_Activity extends ManageListViewComic {
    FirebaseFirestore fireStore;
    FirebaseUser currentUser;

    List<String> favoriteComic = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View favorite_view = inflater.inflate(R.layout.comic_favorite_page,container,false);

        bindUI(favorite_view);
        fireStore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        fireStore.collection("user").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(getActivity() == null) {
                            return;
                        } else {
                            List<String> listDocs = (ArrayList)documentSnapshot.get("favoriteComic");
                            if (listDocs.size() == 0) return;
                            query = fireStore.collection("comic_book").whereIn("slugg", listDocs);
                            setResultData();
                        }
                    }
                });

        return favorite_view;
    }

    private void bindUI(View view) {
        listView =  (ListView)view.findViewById(R.id.listViewFavorite);
    }

    @Override
    protected ListAdapter setListAdapter(List<ComicBook> list) {
        return new AdapterComicBook(getActivity(), R.layout.result_card, list);
    }
}