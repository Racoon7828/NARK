package com.example.NARK;

public class Bill_Data {

    private String bName; // -> 법안명
    private Integer bNum; // -> 법안번호 -> Integer로 변경
    private String bDate; // -> 제안일
    private String bPrp; // -> 제안자
    private String bPrep; // -> 제안자구분
    private String bRa; // -> 소관위(담당기관)
    private String bDae; // -> 대
    private String bCrd; // -> 위원회 회부일

    //추가한곳 -> 리사이클러뷰를 하나로 묶기위해 만들었음
    public Bill_Data(String bName, Integer bNum, String bDate, String bPrp, String bPrep, String bRa, String bDae,
                     String bCrd, String bProResult, String bProDate, String bUrl, int viewType) {
        this.bName = bName;
        this.bNum = bNum;
        this.bDate = bDate;
        this.bPrp = bPrp;
        this.bPrep = bPrep;
        this.bRa = bRa;
        this.bDae = bDae;
        this.bCrd = bCrd;
        this.bProResult = bProResult;
        this.bProDate = bProDate;
        this.bUrl = bUrl;
        this.viewType = viewType;
    }
    
    // 이거는 Bill_Popup에서 사용함 이게없으면 무조건 바로 위에껄로적용되서 매개변수를 다입력해줘야하는 상황이 발생
    // 따라서 밑에 오버라이드해서 매개변수 3개만 받아도 되도록 했음
    public Bill_Data(String bName, String bDate, String bPrp, String bUrl) {
        this.bName = bName;
        this.bDate = bDate;
        this.bPrp = bPrp;
        this.bUrl = bUrl;
    }

    private String bProResult; // -> 처리결과
    private String bProDate; // -> 처리일
    private String bUrl; // -> Url
    private int viewType;

    public void setViewType(int viewType) {this.viewType = viewType;}

    public int getViewType() {return viewType;}

    public void setbName(String bName) {this.bName = bName;}

    public void setbNum(Integer bNum) {this.bNum = bNum;}

    public void setbDate(String bDate) {this.bDate = bDate;}

    public void setbPrp(String bPrp) {this.bPrp = bPrp;}

    public void setbPrep(String bPrep) {this.bPrep = bPrep;}

    public void setbRa(String bRa) {this.bRa = bRa;}

    public void setbDae(String bDae) {this.bDae = bDae;}

    public void setbCrd(String bCrd) {this.bCrd = bCrd;}

    public void setbProResult(String bProResult) {this.bProResult = bProResult;}

    public void setbProDate(String bProDate) {this.bProDate = bProDate;}

    public void setbUrl(String bUrl) {this.bUrl = bUrl;}


    public String getbName() {return bName;}

    public Integer getbNum() {return bNum;}

    public String getbDate() {return bDate;}

    public String getbPrp() {return bPrp;}

    public String getbPrep() {return bPrep;}

    public String getbRa() {return bRa;}

    public String getbDae() {return bDae;}

    public String getbCrd() {return bCrd;}

    public String getbProResult() {return bProResult;}

    public String getbProDate() {return bProDate;}

    public String getbUrl() {return bUrl;}
}

