package com.example.comic_app.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class ComicBook implements Parcelable {
    private String id;
    private String slugg;
    private String author;
    private List<Integer> category = new ArrayList<>();
    private String image;
    private String status;
    private String summary;
    private String length;
    private String title;
    private Long view;
    private List<String> chapterList = new ArrayList<>();

    public ComicBook() {}

    public ComicBook(String author,List category, String image,  String summary, String title, String status, List chap,String length, String slugg){
        this.author = author;
        this.category = category;
        this.image = image;
        this.summary = summary;
        this.title = title;
        this.status = status;
        this.chapterList = chap;
        this.length = length;
        this.slugg = slugg;
        this.view = Long.valueOf(0);
    }

    public ComicBook(String author,List category, String image,  String summary, String title, String status, List chap,String length){
        this.author = author;
        this.category = category;
        this.image = image;
        this.summary = summary;
        this.title = title;
        this.status = status;
        this.chapterList = chap;
        this.length = length;
    }
    public ComicBook(String title, String image, Long view){
        this.title = title;
        this.setView(view);
        this.image = image;
    }
    public List getChapterList() {
        return chapterList;
    }

    public void setChapterList(List chapterList) {
        this.chapterList = chapterList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getView() {
        return view;
    }

    public void setView(Long view) {
        this.view = view;
    }

    public String getSlugg() {
        return slugg;
    }

    public void setSlugg(String slugg) {
        this.slugg = slugg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
