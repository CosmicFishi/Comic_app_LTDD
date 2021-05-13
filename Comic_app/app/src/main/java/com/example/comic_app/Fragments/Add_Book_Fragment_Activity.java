package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;
import com.example.comic_app.model.Category;
import com.example.comic_app.model.ComicBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Add_Book_Fragment_Activity extends Fragment {
    EditText edtNameComic, edt_summary;
    Spinner spnCategory;
    Button  btnCreateComic;
    private Bundle bundle;

    FirebaseFirestore db;
    FirebaseStorage storage;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_write_page,container,false);
        blindUI(view);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        spnCategory = (Spinner) view.findViewById(R.id.spn_Category);
        bundle = this.getArguments();



        // load Spinner cho nguoi dung chon category, nhung vấn đề category của 1 truyện có thể thuộc nhiều thể loại.
        // nhưng cho người dùng chỉ chọn 1.
        Query query = db.collection("category");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Category> category = new ArrayList<Category>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Category cate = new Category();

                        cate.setCategoryName((String)document.get("categoryName"));

                        category.add(cate);
                    }
                    ArrayAdapter adapter =new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, category);
                    spnCategory.setAdapter(adapter);
                } else {

                }
            }
        });


        //thêm sách vào database - chua sữ lý Image, Length
        //Add thành công -> chuyển View qua list truyện đang viết của người dùng -> người dùng Chọn Truyện -> new Chương
        // -> update Commic_chapter, update Commic_book ADD chapter.
        btnCreateComic.setOnClickListener(new View.OnClickListener() {
            String idUser =  currentUser.getEmail();

            @Override
            public void onClick(View v) {
                ComicBook comicBook = new ComicBook(idUser, Arrays.asList(1),null,edt_summary.getText().toString(),
                        edtNameComic.getText().toString() , "Đang ra",Arrays.asList(1),"1 Chương");
                // ComicBook comicBook = new ComicBook("Thanh",Arrays.asList(1, 2, 3),null,"summaryComic","titleComic");
                db.collection("comic_book")
                        .add(comicBook)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
//                                Bundle b = new Bundle();
//                                b.putString("comic_id", bundle.getString("comic_id"));

                                Fragment listComicofUser = new Write_Fragment_Activity();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, listComicofUser).addToBackStack(null).commit();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERROR==============", "AddComic:failed code=" + e.getMessage());
                            }
                        });
            }
        });



        return view;
    }
    private void blindUI(View view) {
        edtNameComic = view.findViewById(R.id.edt_namecomic);
        edt_summary = view.findViewById(R.id.edt_summary);
        btnCreateComic = view.findViewById(R.id.btn_createComic);
    }
}
