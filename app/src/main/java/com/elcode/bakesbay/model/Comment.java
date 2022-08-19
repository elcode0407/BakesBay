package com.elcode.bakesbay.model;

public class Comment {

    String text;
    String id;
    String date;
    String id2;
    String anonymous;


    public Comment(String anonymous,String id,String id2,String text,String date) {
        this.text = text;
        this.id = id;
        this.id2 = id2;
        this.date = date;
        this.anonymous = anonymous;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }
}
