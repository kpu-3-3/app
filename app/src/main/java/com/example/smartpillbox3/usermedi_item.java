package com.example.smartpillbox3;
//리스트 뷰에 들어갈 리스트 아이템 레이아웃


public class usermedi_item {
    private String userIDStr;
    private String typeStr;
    private String a_dateStr;
    private String b_dateStr;
    private String medinameStr;
    private String dateStr;
    private String categoryStr;
    private String takeStr;


    public void setUserID(String userID){
        userIDStr = userID;
    }
    public void setType(String title) {
        typeStr = title ;
    }
    public void setA_time(String a_date) {
        a_dateStr = a_date ;
    }
    public void setB_time(String b_date) {
        b_dateStr = b_date ;
    }
    public void setMediname(String medicine) {
        medinameStr = medicine ;
    }
    public void setTake(String take) {
        takeStr = take ;
    }
    public void setDate(String date) {
        dateStr = date ;
    }
    public void setCategory(String category){
        categoryStr = category;
    }



    public String getUserID() {
        return this.userIDStr ;
    }
    public String getType(){return  this.typeStr ; }
    public String getA_time(){return  this.a_dateStr ; }
    public String getB_time(){return  this.b_dateStr ; }
    public String getMediname() {
        return this.medinameStr ;
    }
    public String getTake(){return this.takeStr ; }
    public String getDate(){return this.dateStr ; }
    public String getCategory(){return this.categoryStr ; }
}