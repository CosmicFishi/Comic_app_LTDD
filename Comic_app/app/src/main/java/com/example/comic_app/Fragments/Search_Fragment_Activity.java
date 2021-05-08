package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;
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

public class Search_Fragment_Activity extends Fragment {
    FirebaseFirestore fireStore;
    ListView listView;
    ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.comic_search_page,container,false);

        listView = view.findViewById(R.id.listView);
        fireStore = FirebaseFirestore.getInstance();

        Query query = fireStore.collection("comic_book")
                .orderBy("title", Query.Direction.DESCENDING).limit(10);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(getActivity() == null) {
                    return;
                } else {
                    if (task.isSuccessful()) {
                        List<ComicBook> listComicBooks = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ComicBook comicBook = new ComicBook();

                            comicBook.setId(document.getId());
                            comicBook.setChapterList((List<String>)document.get("chapterList"));
                            comicBook.setAuthor((String)document.get("author"));
                            comicBook.setCategory((List<Integer>)document.get("category"));
                            comicBook.setImage((String)document.get("image"));
                            comicBook.setLength((String)document.get("length"));
                            comicBook.setStatus((String)document.get("status"));
                            comicBook.setSummary((String)document.get("summany"));
                            comicBook.setTitle((String)document.get("title"));

                            listComicBooks.add(comicBook);
                        }
                        listAdapter = new AdapterComicBook(getActivity(), R.layout.result_card, listComicBooks);
                        listView.setAdapter(listAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Bundle b = new Bundle();
                                Fragment comic_intro_page = new Comic_Introduction_Fragment_Activity();
                                b.putString("comic_id", listComicBooks.get(position).getId());

                                comic_intro_page.setArguments(b);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frag_container,comic_intro_page).addToBackStack(null).commit();
                            }
                        });

                    }
                }
            }
        });

        return view;
    }
}