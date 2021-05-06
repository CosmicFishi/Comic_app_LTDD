package com.example.comic_app.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.comic_app.LoginSignupActivity;
import com.example.comic_app.MainActivity;
import com.example.comic_app.R;
import com.example.comic_app.Utils;
import com.example.comic_app.data.DownloadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class Profile_Fragment_Activity extends Fragment {
    private FirebaseUser currentUser;
    FirebaseFirestore fireStore;

    TextView txt_email, txt_name, txt_num_fav, txt_u_phone;
    Button btn_settings_page, btn_log_out;
    ImageView img_thumbnail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        View home_view = inflater.inflate(R.layout.comic_user_profile_page,container,false);
        bindUI(home_view);


        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = Utils.getAlertBox(getContext(), "Đăng xuất"
                        ,"Bạn có muốn đăng xuất ra khỏi tài khoản này? ");

                alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), LoginSignupActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

        btn_settings_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment setting_page = new Setting_Fragment_Activity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,setting_page).commit();
            }
        });

        img_thumbnail.setImageURI(currentUser.getPhotoUrl());
        new DownloadImageTask(img_thumbnail).execute(currentUser.getPhotoUrl().toString());
        txt_email.setText(currentUser.getEmail());
        txt_name.setText(currentUser.getDisplayName());
        if (currentUser.getPhoneNumber() != "")
            txt_u_phone.setText(currentUser.getPhoneNumber());



        return home_view;
    }

    private void bindUI(View view) {
        btn_settings_page = (Button)view.findViewById(R.id.btn_move_settings);
        btn_log_out = (Button)view.findViewById(R.id.btn_log_out);
        txt_email = (TextView)view.findViewById(R.id.txt_u_email);
        txt_name = (TextView)view.findViewById(R.id.txt_u_name);
        txt_num_fav = (TextView)view.findViewById(R.id.txt_u_num_fav);
        txt_u_phone = (TextView)view.findViewById(R.id.txt_u_phone);
        img_thumbnail = (ImageView)view.findViewById(R.id.img_thumbnail);
    }

}