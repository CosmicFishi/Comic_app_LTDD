package com.example.comic_app.model;

import java.util.List;



public class User {
    private List<String> comicHistory;
    private List<String> favoriteComic;
    private String phone;

//    public interface ProfileCallback {
//        void onCallback (User profile);
//    }

    public User(){}

    public User(String phone, List comicHisList, List favouriteComic){
        this.comicHistory = comicHisList;
        this.favoriteComic = favouriteComic;
        this.phone = phone;
    }

    public List<String> getComicHistory() {
        return comicHistory;
    }

    public void setComicHistory(List<String> comicHistory) {
        this.comicHistory = comicHistory;
    }

    public List<String> getFavoriteComic() {
        return favoriteComic;
    }

    public void setFavoriteComic(List<String> favoriteComic) {
        this.favoriteComic = favoriteComic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
