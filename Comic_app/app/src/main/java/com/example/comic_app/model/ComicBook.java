package com.example.comic_app.model;

import java.util.ArrayList;
import java.util.List;

public class ComicBook {
    private List<String> chapterList = new ArrayList<>();
    private String author;
    private List<Integer> category = new ArrayList<>();
    private String image;
    private String status;
    private String summary;
    private String length;
    private String title;

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
}
