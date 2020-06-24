package com.example.smartpillbox3;

public class mycomment_item {
    private String userIDStr;
    private String commentDateStr;
    private String commentNumStr;
    private String commentStr;
    private String postNumStr;
    private String titleStr;
    private String postDateStr;
    private String medicineStr;
    private String contentStr;
    private String categoryStr;
    private String postuserIDStr;


    public void setCommentNum(String commentNum) {
        commentNumStr = commentNum ;
    }

    public void setPostuserID(String postuserID){
        postuserIDStr = postuserID;
    }

    public void setComment(String comment) {
        commentStr = comment ;
    }

    public void setCommentDate(String commentDate) {
        commentDateStr = commentDate ;
    }

    public void setTitle(String title) {
        titleStr = title ;
    }

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
    public String getComment(){return  this.commentStr ; }
    public String getPostNum(){return  this.postNumStr ; }
    public String getCommentNum() {
        return this.commentNumStr ;
    }
    public String getCommentDate() {
        return this.commentDateStr ;
    }
    public String getPostuserID() {
        return this.postuserIDStr ;
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
