package com.example.NARK;

public class Employ_Data {
    String BLNG_INST_NM;    //소속기관명
    String BRDI_SJ;         //제목
    String BRDI_CN;         //내용
    String RDT;             //작성일자
    String HOME_URL;    //바로가기 URL
    private int viewType;

    public Employ_Data(String BLNG_INST_NM, String BRDI_SJ, String BRDI_CN, String RDT, String HOME_URL, int viewType) {
        this.BLNG_INST_NM = BLNG_INST_NM;
        this.BRDI_SJ= BRDI_SJ;
        this.BRDI_CN = BRDI_CN;
        this.RDT = RDT;
        this.HOME_URL = HOME_URL;
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getBLNG_INST_NM() {
        return BLNG_INST_NM;
    }

    public String getBRDI_SJ() {
        return BRDI_SJ;
    }

    public String getBRDI_CN() {
        return BRDI_CN;
    }

    public String getRDT() {
        return RDT;
    }

    public String getHOME_URL() {
        return HOME_URL;
    }


    public void setBLNG_INST_NM(String BLNG_INST_NM) {
        this.BLNG_INST_NM = BLNG_INST_NM;
    }

    public void setBRDI_SJ(String BRDI_SJ) {
        this.BRDI_SJ = BRDI_SJ;
    }

    public void setBRDI_CN(String BRDI_CN) {
        this.BRDI_CN = BRDI_CN;
    }

    public void setRDT(String RDT) {
        this.RDT = RDT;
    }

    public void setHOME_URL(String HOME_URL) {
        this.HOME_URL = HOME_URL;
    }
}