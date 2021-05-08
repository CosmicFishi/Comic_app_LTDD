package com.example.comic_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comic_app.Fragments.Write_Fragment_Activity;
import com.example.comic_app.Fragments.Favorite_Fragment_Activity;
import com.example.comic_app.Fragments.Home_Fragment_Activity;
import com.example.comic_app.Fragments.Profile_Fragment_Activity;
import com.example.comic_app.Fragments.Search_Fragment_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    FirebaseFirestore fireStore;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            changeLoginActivity();
            return;
        }


//        setContentView(R.layout.activity_main);
        createNarbar();

        setContentView(R.layout.comic_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);

        //Navigation between fragments
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //Change the main fragment to home fragment when first login
        Fragment Home_Fragment = new Home_Fragment_Activity();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container,Home_Fragment)
                .commit();

    }

//    public void bidingUI(){
//        btnAPI = findViewById(R.id.btnAPI);
//        header = findViewById(R.id.header);
//        editText = findViewById(R.id.editText);
//        btnSignOut = findViewById(R.id.btnSignOut);
//        imageView = findViewById(R.id.imageView);
//    }

    public void createNarbar(){
        navigationItemSelectedListener = new
                BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                selectedFragment = new Home_Fragment_Activity();
                                break;
                            case R.id.nav_fav:
                                selectedFragment = new Favorite_Fragment_Activity();
                                break;
                            case R.id.nav_profile:
                                selectedFragment = new Profile_Fragment_Activity();
                                break;
                            case R.id.nav_search:
                                selectedFragment = new Search_Fragment_Activity();
                                break;
                            case R.id.nav_write:
                                selectedFragment = new Write_Fragment_Activity();
                                break;
                        }

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container,selectedFragment)
                                .commit();

                        return true;
                    }


                };
    }

    public void changeLoginActivity() {
        Intent intent = new Intent(this, LoginSignupActivity.class);
        startActivity(intent);
    }
}
