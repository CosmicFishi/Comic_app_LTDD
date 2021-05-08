package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Home_Fragment_Activity extends Fragment {
    ListView listView;
    FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeView = inflater.inflate( R.layout.comic_home_page, container, false );
        bindUI(homeView);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("comic_book")
                .orderBy("view")
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(getActivity() == null) return;
                        if (task.isSuccessful()) {
                            List<ComicBook> listComicBooks = new ArrayList<>();
                            QuerySnapshot documents = task.getResult();

                            for (QueryDocumentSnapshot document : documents) {
                                ComicBook comicBook = new ComicBook();
                                comicBook.setId(document.getId());
                                comicBook.setView((Long)document.get("view"));
                                comicBook.setImage((String)document.get("image"));
                                comicBook.setTitle((String)document.get("title"));

                                listComicBooks.add(comicBook);
                            }

                            listView.setAdapter(new AdapterNewComicBook(getActivity(), R.layout.comic, listComicBooks));
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
                });
        return homeView;
    }

    private ListView bindUI(View view) {
        listView = (ListView) view.findViewById( R.id.comic_list );
        return listView;
    }
}