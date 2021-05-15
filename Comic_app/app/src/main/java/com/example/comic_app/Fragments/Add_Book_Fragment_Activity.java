package com.example.comic_app.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
    Button  btnCreateComic, btn_category, btn_deleteChapterComic, btn_deleteComic;
    private Bundle bundle;

    FirebaseFirestore db;
    FirebaseStorage storage;
    private FirebaseUser currentUser;

    ComicBook comicBook;
    List<String> chapterList;
    List<ComicChapter> contentList;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_write_page,container,false);
        bundle = this.getArguments();
        blindUI(view);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        chapterList = new ArrayList<>();
        chapterList.add("Mới");
        btn_deleteChapterComic.setVisibility(View.GONE);

        if (bundle != null) {
            comicBook = bundle.getParcelable("comic_book");
            editComic();
        } else{
            comicBook = new ComicBook();
            initNew();
        }

        ArrayAdapter adapter =new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, chapterList);
        spnChapter.setAdapter(adapter);

        spnChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Mới")) {
                    edt_comic_content.setText("");
                    editTextChapterName.setText("");
                    btnCreateComic.setText("Tạo");
                    btn_deleteChapterComic.setVisibility(View.GONE);
                } else {
                    btnCreateComic.setText("Cập nhật");
                    edt_comic_content.setText(contentList.get(position-1).getContent());
                    editTextChapterName.setText(comicBook.getChapterList().get(position-1).toString());
                    btn_deleteChapterComic.setVisibility(View.VISIBLE);

                    if (spnChapter.getSelectedItemPosition()+1 == chapterList.size()){
                        btn_deleteChapterComic.setVisibility(View.VISIBLE);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btn_deleteChapterComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("comic_chapter")
                        .document(Utils.toSlug(comicBook.getSlugg()))
                        .collection("chapter")
                        .document(String.valueOf(spnChapter.getSelectedItemPosition()-1))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Document successfully deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error deleting document", Toast.LENGTH_SHORT).show();
                            }
                        });
                comicBook.getChapterList().remove(spnChapter.getSelectedItemPosition()-1);
                db.collection("comic_book").document(comicBook.getSlugg())
                        .update(
                                "length", String.format("%d chương", chapterList.size() - 2),
                                "chapterList", comicBook.getChapterList()
                        );
                changeFragment();
            }
        });

        btnCreateComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String author =  currentUser.getDisplayName();
                String nameComic = edtNameComic.getText().toString();
                String chapterName = editTextChapterName.getText().toString();

                if(editTextChapterName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Tên truyện không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                int chapter;
                if (spnChapter.getSelectedItem().equals("Mới")) {
                    chapter = chapterList.size() - 1;
                    comicBook.setLength(String.format("%d chương", chapterList.size()));
                    comicBook.getChapterList().add(editTextChapterName.getText().toString());
                    if(comicBook.getChapterList().contains(chapterName)) {
                        Toast.makeText(getContext(), "Không thể thêm vì tên tập truyện bị trùng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    chapter = spnChapter.getSelectedItemPosition() - 1;
                    comicBook.getChapterList().remove(spnChapter.getSelectedItemPosition() - 1);
                    comicBook.getChapterList().add(spnChapter.getSelectedItemPosition() - 1, chapterName);
                }
                ComicBook c = new ComicBook(author, mUserItems, null,
                        edt_summary.getText().toString(),
                        nameComic,
                        "Đang ra", comicBook.getChapterList(),
                        comicBook.getLength(),
                        Utils.toSlug(nameComic));
                db.collection("comic_book")
                        .document(Utils.toSlug(c.getTitle()))
                        .set(c);
                db.collection("comic_chapter")
                        .document(Utils.toSlug(c.getTitle()))
                        .collection("chapter")
                        .document(String.valueOf(chapter))
                        .set(new ComicChapter(edt_comic_content.getText().toString()));
                changeFragment();

            }
        });
        btn_deleteComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("comic_chapter")
                        .document(Utils.toSlug(comicBook.getSlugg()))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Comic successfully deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error deleting comic", Toast.LENGTH_SHORT).show();
                            }
                        });
                db.collection("comic_book").document(comicBook.getSlugg())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Comic successfully deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error deleting comic", Toast.LENGTH_SHORT).show();
                            }
                        });

                db.collection("comic_book").document(comicBook.getSlugg())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Comic successfully deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error deleting comic", Toast.LENGTH_SHORT).show();
                            }
                        });

                changeFragment();
            }
        });
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Chọn thể loại");
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
    private void editComic(){
        edtNameComic.setText(comicBook.getTitle());
        edt_summary.setText(comicBook.getSummary());
        editTextChapterName.setText(comicBook.getChapterList().get(0).toString());
        edt_comic_content.setText("");


        edtNameComic.setKeyListener(null);

        getChapterList();
        List<Integer> listInt = new ArrayList<>();
        createCategory(comicBook.getCategory());
    }

    private void initNew(){
        createCategory(null);
    }
    private void getChapterList(){
        db.collection("comic_chapter")
                .document(Utils.toSlug(comicBook.getSlugg()))
                .collection("chapter")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    contentList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ComicChapter chapter = document.toObject(ComicChapter.class);
                        contentList.add(chapter);
                        chapterList.add(document.getId());
                    }
                }
            }
        });
    }

    private void createCategory(List<Long> list){
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
                    if (list != null) {
                        for (int i=0; i<list.size(); i++){
                            checkedItems[list.get(i).intValue()] = true;
                            mUserItems.add(list.get(i).intValue());
                        }
                    }
                }
            }
        });
    }

    private void blindUI(View view) {
        edtNameComic = view.findViewById(R.id.edt_namecomic);
        edt_summary = view.findViewById(R.id.edt_summary);
        btnCreateComic = view.findViewById(R.id.btn_createComic);
        btn_category = view.findViewById(R.id.btn_category);
        btn_deleteComic = view.findViewById(R.id.btn_deleteComic);
        btn_deleteChapterComic = view.findViewById(R.id.btn_deleteChapterComic);
        editTextChapterName = view.findViewById(R.id.editTextChapterName);
        edt_comic_content = view.findViewById(R.id.edt_comic_content);
        spnChapter = view.findViewById(R.id.spn_Chapter);
        btn_deleteChapterComic.setVisibility(View.GONE);
    }
    public void changeFragment(){
        Fragment listComicofUser = new Write_Fragment_Activity();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, listComicofUser).addToBackStack(null).commit();
    }
}
