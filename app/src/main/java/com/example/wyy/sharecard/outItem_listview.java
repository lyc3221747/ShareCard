package com.example.wyy.sharecard;

/**
 * Created by z on 2019/3/28.
 */

public class outItem_listview {
    private String card_name;   //包括名字和金额
    private String user_name;   //购买者名字
    private String time;        //成交时间
    private String user_num;    //购买者电话

    public outItem_listview(String card_name, String user_name, String time, String user_num) {
        this.card_name = card_name;
        this.user_name = user_name;
        this.time = time;
        this.user_num = user_num;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_num() {
        return user_num;
    }

    public void setUser_num(String user_num) {
        this.user_num = user_num;
    }
}
