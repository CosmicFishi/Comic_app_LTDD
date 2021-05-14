package com.example.comic_app.model;

public class ComicChapter {
    private String content;

    public ComicChapter(){}

    public ComicChapter(String content){
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
