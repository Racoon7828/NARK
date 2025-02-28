package com.example.NARK;

public class Budget_Data {
        String REG_DATE;    //발간일
        String DEPARTMENT_NAME;         //부서명
        String SUBJECT;         //보고서명
        String LINK_URL;    //url
        private int viewType;

        public Budget_Data(String REG_DATE, String DEPARTMENT_NAME, String SUBJECT, String LINK_URL, int viewType) {
                this.REG_DATE = REG_DATE;
                this.DEPARTMENT_NAME= DEPARTMENT_NAME;
                this.SUBJECT = SUBJECT;
                this.LINK_URL = LINK_URL;
                this.viewType = viewType;

        }

        public int getViewType() {
                return viewType;
        }

        public void setViewType(int viewType) {
                this.viewType = viewType;
        }

        public String getREG_DATE() {
                return REG_DATE;
        }

        public void setREG_DATE(String REG_DATE) {
                this.REG_DATE = REG_DATE;
        }

        public String getDEPARTMENT_NAME() {
                return DEPARTMENT_NAME;
        }

        public void setDEPARTMENT_NAME(String DEPARTMENT_NAME) {
                this.DEPARTMENT_NAME = DEPARTMENT_NAME;
        }

        public String getSUBJECT() {
                return SUBJECT;
        }

        public void setSUBJECT(String SUBJECT) {
                this.SUBJECT = SUBJECT;
        }

        public String getLINK_URL() {
                return LINK_URL;
        }

        public void setLINK_URL(String LINK_URL) {
                this.LINK_URL = LINK_URL;
        }
}
