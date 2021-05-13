package com.example.comic_app.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.comic_app.Utils;
import com.example.comic_app.model.Category;
import com.example.comic_app.model.ComicBook;
import com.example.comic_app.model.ComicChapter;
import com.example.comic_app.model.ListCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Add_Book_Fragment_Activity extends Fragment {
    EditText edtNameComic, edt_summary, editTextChapterName, edt_comic_content;
    Spinner spnChapter;
    Button  btnCreateComic, btn_category;
    private Bundle bundle;

    FirebaseFirestore db;
    FirebaseStorage storage;
    private FirebaseUser currentUser;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_write_page,container,false);
        blindUI(view);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        spnChapter = (Spinner) view.findViewById(R.id.spn_Chapter);
        bundle = this.getArguments();

        db.collection("category")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ListCategory category = new ListCategory();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category cate = document.toObject(Category.class);
                                category.addItem(cate);
                            }
                            listItems = category.getListNameCategory().toArray(new String[0]);
                            checkedItems = new boolean[listItems.length];
                        } else {

                        }
                    }
                });
        List<String> chapterList = new ArrayList<>();
        chapterList.add("Mới");
        ArrayAdapter adapter =new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, chapterList);
        spnChapter.setAdapter(adapter);

        //thêm sách vào database - chua sữ lý Image
        //Add thành công -> chuyển View qua list truyện đang viết của người dùng -> người dùng Chọn Truyện -> Chương
        // -> chọn chương mới và nhập nội dung, update.
        btnCreateComic.setOnClickListener(new View.OnClickListener() {
            String author =  currentUser.getDisplayName();
            String nameComic = edtNameComic.getText().toString();
            String chapterName = editTextChapterName.getText().toString();
            @Override
            public void onClick(View v) {
                ComicBook comicBook = new ComicBook(author, mUserItems, null,
                        edt_summary.getText().toString(),
                        nameComic,
                        "Đang ra", Arrays.asList(chapterName),
                        String.format("%d chương", chapterList.size()),
                        Utils.toSlug(nameComic));
                db.collection("comic_book")
                        .document(Utils.toSlug(comicBook.getTitle()))
                        .set(comicBook);
                int chapter;
                if (spnChapter.getSelectedItem().toString().equals("Mới"))
                    chapter = chapterList.size() - 1;
                else
                    chapter = spnChapter.getSelectedItemPosition();
                db.collection("comic_chapter")
                        .document(Utils.toSlug(comicBook.getTitle()))
                        .collection("chapter")
                        .document(String.valueOf(chapter))
                        .set(new ComicChapter(edt_comic_content.getText().toString()));
                Fragment listComicofUser = new Write_Fragment_Activity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, listComicofUser).addToBackStack(null).commit();
            }
        });

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Chọn tập");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        return view;
    }
    private void blindUI(View view) {
        edtNameComic = view.findViewById(R.id.edt_namecomic);
        edt_summary = view.findViewById(R.id.edt_summary);
        btnCreateComic = view.findViewById(R.id.btn_createComic);
        btn_category = view.findViewById(R.id.btn_category);
        editTextChapterName = view.findViewById(R.id.editTextChapterName);
        edt_comic_content = view.findViewById(R.id.edt_comic_content);
    }
}
