package com.example.comic_app;

import java.util.ArrayList;
import java.util.List;

public class ListCategory {
    private List<Category> listCate = new ArrayList<>();


    public ListCategory(List<Category> list){
        this.listCate = list;
    }
    public ListCategory(){}

//    public Category getCategoryById(int id){
//        listCate.forEach(category -> {
//            if (category.getId() == id)
//                return category;
//        });
//        return null;
//    }

    @Override
    public String toString() {
        return "ListCategory{" +
                "listCate=" + listCate.toString() +
                '}';
    }

    public List<Category> getListCate() {
        return listCate;
    }

    public void setListCate(List<Category> listCate) {
        this.listCate = listCate;
    }
}
