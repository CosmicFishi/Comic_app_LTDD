package com.example.comic_app;

public class Category {
    private String categoryName;
    private int id;

    @Override
    public String toString() {
        return "{" +
                "\tcategoryName='" + categoryName + '\'' +
                "\tid=" + id +
                '}';
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
