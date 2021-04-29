package com.example.comic_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FirebaseApi myFireStore;
    public static final String TAG = "Okei---------------------";
    public TextView header;
    public Button btnAPI;
    public EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAPI = findViewById(R.id.btnAPI);
        header = findViewById(R.id.header);
        editText = findViewById(R.id.editText);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        btnAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireStore.collection("category").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<Category> list = new ArrayList<>();
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Category cate = document.toObject(Category.class);
                                        list.add(cate);
                                    }
                                    editText.setText(list.toString());
                                }else {
                                    Log.w("ERROR======", "ERROR", task.getException());
                                }
                            }
                        });
            }
        });



    }
    
}
