package com.example.comic_app.model;

import java.util.List;



public class Profile {
    private List comicHistory;
    private List favouriteComic;
    private String name;
    private String phone;

    public interface ProfileCallback {
        void onCallback (Profile profile);
    }

    public Profile(){}

    public Profile(String name, String phone, List comicHisList, List favouriteComic){
        this.comicHistory = comicHisList;
        this.favouriteComic = favouriteComic;
        this.name = name;
        this.phone = phone;
    }

    public List getComicHistory() {
        return comicHistory;
    }

    public void setComicHistory(List comicHistory) {
        this.comicHistory = comicHistory;
    }

    public List getFavouriteComic() {
        return favouriteComic;
    }

    public void setFavouriteComic(List favouriteComic) {
        this.favouriteComic = favouriteComic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
