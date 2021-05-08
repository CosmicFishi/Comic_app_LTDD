package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comic_app.R;

import java.util.ArrayList;

public class Comic_ChapterList_Fragment_Activity extends Fragment {

    ListView lv_chapter_list;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View chapter_list_view = inflater.inflate(R.layout.comic_chapter_list,container,false);

        bundle = this.getArguments();
        bindUI(chapter_list_view);

        populateList(bundle.getStringArrayList("chapter_list"));
        return chapter_list_view;
    }

    private void populateList(ArrayList<String> chapterList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.comic_chapter_item, chapterList);
        lv_chapter_list.setAdapter(arrayAdapter);

        lv_chapter_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putString("comic_id", bundle.getString("comic_id"));
                b.putStringArrayList("chapter_list", chapterList);
                b.putInt("chapter_num", position);
                b.putInt("chapter_list_length", chapterList.size());

                Fragment read_page = new Comic_Read_Fragment_Activity();
                read_page.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, read_page).addToBackStack(null).commit();
            }
        });
    }

    private void bindUI(View view) {
        lv_chapter_list = (ListView)view.findViewById(R.id.lv_chapter_list);
    }
}