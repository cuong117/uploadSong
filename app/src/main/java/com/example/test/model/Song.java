package com.example.test.model;

public class Song {
    private String title;
    private String link;
    private String subTitle;

    public Song(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public Song(){}

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
