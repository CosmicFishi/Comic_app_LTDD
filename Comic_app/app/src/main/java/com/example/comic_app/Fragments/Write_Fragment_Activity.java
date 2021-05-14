package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;
import com.example.comic_app.adapter.AdapterComicBook;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Write_Fragment_Activity extends Fragment {
    private FirebaseUser currentUser;
    private Bundle bundle;

    FirebaseFirestore fireStore;
    ListView listView;
    Button btn_bangDieuKhien;
    ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View list_comic_of_user = inflater.inflate(R.layout.comic_of_user,container,false);

        listView = list_comic_of_user.findViewById(R.id.lv_comic_of_user);
        btn_bangDieuKhien = list_comic_of_user.findViewById(R.id.btn_bangdieukhien);
        fireStore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String author =  currentUser.getDisplayName();
        List<ComicBook> listComicBooks = new ArrayList<>();

        Query query = fireStore.collection("comic_book").whereEqualTo("author",author);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ComicBook comicBook = new ComicBook();
                        comicBook.setId(document.getId());
                        comicBook.setChapterList((List<String>)document.get("chapterList"));
                        comicBook.setAuthor((String)document.get("author"));
                        comicBook.setCategory((List<Long>)document.get("category"));
                        comicBook.setImage((String)document.get("image"));
                        comicBook.setLength((String)document.get("length"));
                        comicBook.setStatus((String)document.get("status"));
                        comicBook.setSummary((String)document.get("summary"));
                        comicBook.setTitle((String)document.get("title"));
                        comicBook.setSlugg((String)document.get("slugg"));
                        comicBook.setView((Long)document.get("view"));
                        listComicBooks.add(comicBook);
                    }
                    listAdapter = new AdapterComicBook(getActivity(), R.layout.result_card, listComicBooks);

                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            changeFragment(listComicBooks.get(position));
                        }
                    });

                } else {

                }
            }
        });

        btn_bangDieuKhien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(null);
            }
        });

        return  list_comic_of_user;
    }
    public void changeFragment(ComicBook comic){
        Fragment listchap =  new Add_Book_Fragment_Activity();

        if (comic != null){
            Bundle b = new Bundle();
            b.putParcelable("comic_book", comic);
            listchap.setArguments(b);
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container,listchap).addToBackStack(null).commit();
    }
}