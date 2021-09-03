package com.example.mytraining;

import java.io.Serializable;

class DataModal implements Serializable {

    private String Title, imgurl, date, userKey;

    public DataModal() {

    }

    public DataModal(String trim, String imageUrl, String fordate, String userkey) {

        Title = trim;
        imgurl = imageUrl;
        date = fordate;
        userKey = userkey;
    }


    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getimgurl() {
        return imgurl;
    }

    public void setimgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getuserKey() {
        return userKey;
    }

    public void setuserKey(String userKey) {
        this.userKey = userKey;
    }
}

class DataModelchild implements Serializable {
    private String notetitle, notedate;

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedate() {
        return notedate;
    }

    public void setNotedate(String notedate) {
        this.notedate = notedate;
    }
}
