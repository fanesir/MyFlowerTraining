package com.example.mytraining;

import java.io.Serializable;

class DataModal implements Serializable {


    private String Title;
    private String Content;
    private String imgurl;
    private String date;
    private String userKey;

    public DataModal() {

    }


    public DataModal(String trim, String imageUrl, String fordate) {

        Title = trim;
        imgurl = imageUrl;
        date = fordate;

    }

    public DataModal(String trim, String imageUrl, String fordate,String userkey) {

        Title = trim;
        imgurl = imageUrl;
        date = fordate;
        userKey=userkey;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public String getuserKey() {
        return userKey;
    }

    public void setuserKey(String userKey) {
        this.userKey = userKey;
    }
}
