package com.example.NARK;
// 데이터를 받아오기 위한 데이터모델 클래스
public class Mp_DataModel {
   private String name;
   private String pp;
   private String phoneNum;
   private String email;
   private String bf;
   private String imgUrl;
   private int viewType;

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPp() {
        return pp;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getBf() {
        return bf;
    }

    public int getViewType() {
        return viewType;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBf(String bf) {
        this.bf = bf;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

