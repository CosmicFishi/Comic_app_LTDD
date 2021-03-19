package com.example.demo_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn;
    FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment();
            }
        });

//        getFragment();
    }

    private void getFragment() {
        Fragment fragment = fm.findFragmentById(R.id.fl_fragment);
        if(fragment instanceof FirstFragment){
            fragment = new SecondFragment();
        } else {
            fragment = new FirstFragment();
        }

        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.fl_fragment, fragment);
        ft.replace(R.id.fl_fragment, fragment);
//        ft.addToBackStack("abc");
        ft.commit();
    }
}