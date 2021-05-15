package com.example.comic_app.model;

import com.example.comic_app.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategory {
    private List<Category> listCate = new ArrayList<>();


    public ListCategory(List<Category> list){
        this.listCate = list;
    }
    public ListCategory(){}
    public List<String> getListNameCategory(){
        List<String> list = new ArrayList<>();

        for (Category c : listCate){
            list.add(c.getCategoryName().substring(0, 1).toUpperCase() + c.getCategoryName().substring(1));
        }

        return list;
    }
    public void addItem(Category c){
        this.listCate.add(c);
    }
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
