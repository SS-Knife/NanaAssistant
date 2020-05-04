package com.example.nanaassistant.acount;

public class Bill {
    private String title;
    private String detail;
    private String time;
    private String month;
    private double money;
    //i = 0, o = 1
    private String io;
    private String ant;

    public Bill(){

    }

    public Bill(String title, String detail, String time, String month, double money, String io, String ant) {
        this.title = title;
        this.detail = detail;
        this.time = time;
        this.month = month;
        this.money = money;
        this.io = io;
        this.ant = ant;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getIo() {
        return io;
    }

    public void setIo(String io) {
        this.io = io;
    }

    public String getAnt() {
        return ant;
    }

    public void setAnt(String ant) {
        this.ant = ant;
    }
}
