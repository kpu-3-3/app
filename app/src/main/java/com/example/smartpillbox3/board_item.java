package com.example.smartpillbox3;
//리스트 뷰에 들어갈 리스트 아이템 레이아웃


public class board_item {
    private String userIDStr;
    private String titleStr;
    private String postDateStr;
    private String medicineStr;
    private String contentStr;
    private String categoryStr;
    private String postNumStr;

    public void setUserID(String userID){
        userIDStr = userID;
    }

    public void setpostTitle(String title) {
        titleStr = title ;
    }

    public void setpostDate(String postDate) {
        postDateStr = postDate ;
    }

    public void setMedicine(String medicine) {
        medicineStr = medicine ;
    }

    public void setContent(String content) {
        contentStr = content ;
    }

    public void setCategory(String category) {
        categoryStr = category ;
    }

    public void setPostNum(String postNum) {
        postNumStr = postNum ;
    }



    public String getUserID() {
        return this.userIDStr ;
    }
    public String getTitle(){return  this.titleStr ; }
    public String getPostDate(){return  this.postDateStr ; }
    public String getMedicine() {
        return this.medicineStr ;
    }
    public String getContent() {
        return this.contentStr ;
    }
    public String getCategory() {
        return this.categoryStr ;
    }
    public String getpostNum() {
        return this.postNumStr ;
    }



}