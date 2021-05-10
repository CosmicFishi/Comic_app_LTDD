package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;
import com.example.comic_app.model.ComicChapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firestore.v1.DocumentTransform;

import java.util.ArrayList;
import java.util.List;

public class Comic_Read_Fragment_Activity extends Fragment {

    private Bundle bundle;
    FirebaseFirestore db;
    TextView txt_title, txt_content;
    Button btn_next, btn_prev, btn_next2, btn_prev2;
    ScrollView sv_main_container;
    private static int chapter_num = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View comic_chapter_page = inflater.inflate(R.layout.comic_read_page,container,false);
        db = FirebaseFirestore.getInstance();
        bindUI(comic_chapter_page);
        bundle = this.getArguments();

        if(bundle != null ) {
            chapter_num = bundle.getInt("chapter_num");

            populateUI(bundle.getStringArrayList("chapter_list"), bundle.getString("comic_id"), chapter_num);
        } else {
            Log.e("Bundle is null", "onCreateView: Bundle is null");
        }

        bindButtons();

        return comic_chapter_page;
    }

    private void populateUI(ArrayList<String> chapter_list, String comic_id, int chapter_num) {
        DocumentReference dr = db.collection("comic_chapter")
                .document(comic_id).collection("chapter")
                .document(String.valueOf(chapter_num));

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(getActivity() == null) {
                    return;
                } else {
                    if(task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()) {
                            ComicChapter comicChapter = new ComicChapter();
                            comicChapter = doc.toObject(ComicChapter.class);
                            txt_content.setText(comicChapter.getContent());
                        } else {
                            txt_content.setText("Trang hiện tại chưa có cập nhật");
                        }
                        txt_title.setText(chapter_list.get(chapter_num));
                    }
                }
            }
        });
    }

    private void bindButtons() {
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chapter_num >=0 ) {
                    chapter_num = chapter_num - 1;
                    populateUI(bundle.getStringArrayList("chapter_list"), bundle.getString("comic_id"), chapter_num);
                    addViewToComic();
                }
            }
        });
        btn_prev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chapter_num >=0 ) {
                    chapter_num = chapter_num - 1;
                    populateUI(bundle.getStringArrayList("chapter_list"), bundle.getString("comic_id"), chapter_num);
                    addViewToComic();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chapter_list_length = bundle.getInt("chapter_list_length");
                if(chapter_num < chapter_list_length) {
                    chapter_num = chapter_num + 1;
                    populateUI(bundle.getStringArrayList("chapter_list"), bundle.getString("comic_id"), chapter_num);
                    addViewToComic();
                }
            }
        });
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chapter_list_length = bundle.getInt("chapter_list_length");
                if(chapter_num < chapter_list_length) {
                    chapter_num = chapter_num + 1;
                    populateUI(bundle.getStringArrayList("chapter_list"), bundle.getString("comic_id"), chapter_num);
                    addViewToComic();
                }
            }
        });
    }

    private void addViewToComic() {
        DocumentReference dr = db.collection("comic_book")
                .document(bundle.getString("comic_id"));
        dr.update("view", FieldValue.increment(1));
    }

    private void bindUI(View view) {
        btn_next = (Button)view.findViewById(R.id.btn_next);
        btn_next2 = (Button)view.findViewById(R.id.btn_next2);
        btn_prev = (Button)view.findViewById(R.id.btn_prev);
        btn_prev2 = (Button)view.findViewById(R.id.btn_prev2);
        txt_content = (TextView)view.findViewById(R.id.txt_c_chapter_content);
        txt_title = (TextView)view.findViewById(R.id.txt_c_chapter_title);
        sv_main_container = (ScrollView)view.findViewById(R.id.sv_main_container);
    }
}