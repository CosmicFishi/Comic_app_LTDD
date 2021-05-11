package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;

public class Setting_Fragment_Activity extends Fragment {

    Button btn_return_profile, btn_move_update_profile_page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View setting_view = inflater.inflate(R.layout.comic_settings_page,container,false);
        bindUI(setting_view);

        btn_return_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment profile_page = new Profile_Fragment_Activity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, profile_page).commit();
            }
        });
        btn_move_update_profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment update_profile_page = new Update_Profile_Fragment_Activity();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, update_profile_page).addToBackStack(null).commit();
            }
        });

        return setting_view;
    }

    private void bindUI(View view) {
        btn_return_profile = (Button)view.findViewById(R.id.btn_return_profile_page);
        btn_move_update_profile_page = (Button)view.findViewById(R.id.btn_move_change_profile);
    }
}