package com.example.comic_app;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseApi {
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public ListCategory getCategory(){
        List<Category> list = new ArrayList<>();

        fireStore.collection("category").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Category cate = document.toObject(Category.class);
                            list.add(cate);
                        }

                    }else {
                        Log.w("ERROR======", "ERROR", task.getException());
                    }
                }
            });

        return new ListCategory(list);
    }


}
