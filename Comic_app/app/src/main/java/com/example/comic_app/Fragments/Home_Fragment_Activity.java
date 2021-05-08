package com.example.comic_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.comic_app.R;
import com.example.comic_app.model.ComicBook;
import java.util.ArrayList;
import android.widget.ListView;
import java.util.List;

public class Home_Fragment_Activity extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View setting_view = inflater.inflate( R.layout.comic_home_page, container, false );
        bindUI( setting_view );
        List<ComicBook> image_details = getListData();
        listView = bindUI( setting_view );
        listView.setAdapter( new Commicad( getActivity(), image_details ) );
        return setting_view;
    }
    private List<ComicBook> getListData() {
        List<ComicBook> list = new ArrayList<ComicBook >();
        ComicBook comic1 = new ComicBook ( "tôi buồn ngủ", "link", "Yến" );
        ComicBook comic2 = new ComicBook ( "Nhiều deadline dị", "link", "Yến" );
        ComicBook comic3 = new ComicBook ( "Ôi cuôc đời", "link", "Yến");
        list.add( comic1 );
        list.add( comic2);
        list.add( comic3 );
        return list;
    }
    private ListView bindUI(View view) {
        listView = (ListView) view.findViewById( R.id.comic_list );
        return listView;
    }
}