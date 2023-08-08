package com.example.shoponline.model;

public class Category {
    String cid, title, image;

    public Category(String cid, String title, String image) {
        this.cid = cid;
        this.title = title;
        this.image = image;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
