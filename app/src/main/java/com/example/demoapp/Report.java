package com.example.demoapp;

public class Report {
    private String title;
    private String date;
    //private String ImageUrl;

    public  Report(String reportTitle,String reportDate/*,String imageUrl*/){
        title = reportTitle;
        date = reportDate;
        //ImageUrl = imageUrl;
    }
    public Report(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    /*public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }*/
}
