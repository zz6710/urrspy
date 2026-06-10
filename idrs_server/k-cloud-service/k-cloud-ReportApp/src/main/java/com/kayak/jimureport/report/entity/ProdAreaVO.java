package com.kayak.jimureport.report.entity;

public class ProdAreaVO {

    private String salesArea;
    private String buyCcy;
    private String mark;
    private String subs_amt;
    private String data_date;

    public String getData_date() {
        return data_date;
    }

    public void setData_date(String data_date) {
        this.data_date = data_date;
    }

    public String getSubs_amt() {
        return subs_amt;
    }

    public void setSubs_amt(String subs_amt) {
        this.subs_amt = subs_amt;
    }

    public String getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }

    public String getBuyCcy() {
        return buyCcy;
    }

    public void setBuyCcy(String buyCcy) {
        this.buyCcy = buyCcy;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
