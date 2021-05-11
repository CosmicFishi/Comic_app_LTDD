package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;
import com.example.comic_app.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Update_Profile_Fragment_Activity extends Fragment {

    TextView txt_u_phone, txt_pass, txt_confirm_pass, txt_pass_new;
    Button btn_reset, btn_update_profile;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String phone;
    private final String passRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View setting_view = inflater.inflate(R.layout.comic_update_profile_page,container,false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        bindUI(setting_view);
        setData();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_u_phone.setText(phone);
            }
        });

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        return setting_view;
    }

    private void updateUser() {
        AuthCredential credential;
        phone = txt_u_phone.getText().toString();
        String pass = txt_pass.getText().toString();
        String new_pass = txt_pass_new.getText().toString();
        String confirm_pass = txt_confirm_pass.getText().toString();

        if(new_pass.isEmpty() && confirm_pass.isEmpty() && phone.length() != 0) {
            if(pass.length() == 0) {
                Toast.makeText(getContext(),"Vui lòng nhập mật khẩu để cập nhật thông tin",Toast.LENGTH_SHORT).show();
                return;
            }
            credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), pass);
            mAuth.getCurrentUser().reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(),"Đã đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                updatePhone();
                            }
                            else
                                Toast.makeText(getContext(),"Đã đăng nhập thất bại, vui lòng nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            if(pass.length()!= 0 && confirm_pass.length()!=0 && new_pass.length()!=0 && phone.length() != 0) {
                if(!new_pass.equals(confirm_pass)) {
                    Toast.makeText(getContext(),"Không trùng mật khẩu mới, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Utils.checkValidRegex(new_pass, passRegex)) {
                    credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), pass);
                    mAuth.getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getContext(),"Đã đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                        updatePhone();
                                        mAuth.getCurrentUser().updatePassword(new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(getContext(),"Cập nhật mật khẩu thành công",Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(),"Cập nhật mật khẩu thất bại, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else
                                        Toast.makeText(getContext(),"Đã đăng nhập thất bại, vui lòng nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(),"Vui lòng nhập mật khẩu mạnh, tối thiểu 8 ký tự",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updatePhone() {
        db.collection("user").document(mAuth.getCurrentUser().getUid())
                .update("phone",phone).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(),"Đã cập nhật sdt thành công",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"Đã cập nhật sdt thất bại, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setData() {
        db.collection("user").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if(getActivity() == null) {
                    return;
                } else {
                    phone = ds.get("phone").toString();
                    txt_u_phone.setText(phone);
                }
            }
        });
    }

    private void bindUI(View view) {
        btn_reset = (Button)view.findViewById(R.id.btn_reset_phone);
        btn_update_profile = (Button)view.findViewById(R.id.btn_update);
        txt_u_phone = (TextView)view.findViewById(R.id.txt_u_phone_2);
        txt_pass = (TextView)view.findViewById(R.id.txt_u_pass);
        txt_pass_new = (TextView)view.findViewById(R.id.txt_u_pass_new);
        txt_confirm_pass = (TextView)view.findViewById(R.id.txt_u_confirm_pass);
    }
}