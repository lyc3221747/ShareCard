package com.example.wyy.sharecard;

import java.io.Serializable;

/**
 * Created by wyy on 17-7-6.
 */

public class cardItem_listview implements Serializable {
    private String head_url;
    private String user_name;
    private int number;
    private double distance;
    private String card_pic;
    private double price;
    private String card_name;
    private String tele;
    private String card_id;
    private double latitude;
    private double longitude;
    private double org_price;
    private String card_inf;
    private String card_time;
    private String user_id;
    private int card_sort;

    public int getCard_sort() {
        return card_sort;
    }

    public void setCard_sort(int card_sort) {
        this.card_sort = card_sort;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getOrg_price() {
        return org_price;
    }

    public void setOrg_price(double org_price) {
        this.org_price = org_price;
    }

    public String getCard_inf() {
        return card_inf;
    }

    public void setCard_inf(String card_inf) {
        this.card_inf = card_inf;
    }

    public String getCard_time() {
        return card_time;
    }

    public void setCard_time(String card_time) {
        this.card_time = card_time;
    }






    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getNumber() {
        return number;
    }

    public double getDistance() {
        return distance;
    }

    public String getCard_pic() {
        return card_pic;
    }

    public double getPrice() {
        return price;
    }

    public String getCard_name() {
        return card_name;
    }

    public String getTele() {
        return tele;
    }

    public String getCard_id() {
        return card_id;
    }

    cardItem_listview(String head_url,String user_name,int number,double distance,
                      String card_pic,double price,String card_name,String tele,String card_id) {
        this.head_url = head_url;
        this.user_name = user_name;
        this.number = number;
        this.distance = distance;
        this.card_pic = card_pic;
        this.price = price;
        this.card_name = card_name;
        this.tele = tele;
        this.card_id = card_id;
    }
}
