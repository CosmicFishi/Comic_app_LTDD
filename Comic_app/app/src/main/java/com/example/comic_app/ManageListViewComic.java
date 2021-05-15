package com.example.comic_app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.comic_app.Fragments.Comic_Introduction_Fragment_Activity;
import com.example.comic_app.adapter.AdapterComicBook;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class ManageListViewComic extends Fragment{
    protected Query query;
    protected ListAdapter listAdapter;
    protected ListView listView;
    protected TextView txt_error;


    public void setResultData(){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(getActivity() == null) {
                    return;
                }
                if (task.isSuccessful()) {
                    List<ComicBook> listComicBooks = new ArrayList<>();

                    QuerySnapshot documents = task.getResult();

                    if(documents.size() == 0) {
                        Toast.makeText(getContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (QueryDocumentSnapshot document : documents) {
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
                    listAdapter = setListAdapter(listComicBooks);
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
        });
    }

    protected abstract ListAdapter setListAdapter(List<ComicBook> list);
}
