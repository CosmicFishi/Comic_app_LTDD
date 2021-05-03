package com.example.comic_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comic_app.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    TextView header;
    Button btnAPI, btnSignOut;
    EditText editText;
    ImageView imageView;

    FirebaseFirestore fireStore;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            changeLoginActivity();
            return;
        }

        String personId = currentUser.getUid();
        Uri personAvatar = currentUser.getPhotoUrl();

        setContentView(R.layout.activity_main);
        bidingUI();

//        Error mainthreat and async
//        imageView.setImageBitmap(new Utils().getImage(personAvatar.toString()));

        fireStore = FirebaseFirestore.getInstance();

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

        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            changeLoginActivity();
        });

        header.setText("UserId: "+ personId);
    }

    public void bidingUI(){
        btnAPI = findViewById(R.id.btnAPI);
        header = findViewById(R.id.header);
        editText = findViewById(R.id.editText);
        btnSignOut = findViewById(R.id.btnSignOut);
        imageView = findViewById(R.id.imageView);
    }

    public void changeLoginActivity() {
        Intent intent = new Intent(this, LoginSignupActivity.class);
        startActivity(intent);
    }
}
