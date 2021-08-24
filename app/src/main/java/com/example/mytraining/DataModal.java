package com.example.mytraining;

import java.io.Serializable;

class DataModal implements Serializable {


    private String Title;
    private String Content;
    private String imgurl;
    private String date;

    public DataModal() {

    }


    public DataModal(String trim, String imageUrl, String fordate) {

        Title = trim;
        imgurl = imageUrl;
        date = fordate;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String imgurl) {
        this.date = imgurl;
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
}
