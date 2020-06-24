package com.example.smartpillbox3;
//리스트 뷰에 들어갈 리스트 아이템 레이아웃


public class comment_item {
    private String userIDStr;
    private String commentDateStr;
    private String commentNumStr;
    private String commentStr;
    private String postNumStr;
    private String titleStr;


    public void setCommentNum(String commentNum) {
        commentNumStr = commentNum ;
    }

    public void setUserID(String userID){
        userIDStr = userID;
    }

    public void setPostNum(String postNum) {
        postNumStr = postNum ;
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
    public String getTitle() {
        return this.titleStr ;
    }



}