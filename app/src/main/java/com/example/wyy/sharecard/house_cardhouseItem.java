package com.example.wyy.sharecard;

/**
 * Created by wyy on 17-7-8.
 */

public class house_cardhouseItem {
    private String url;
    private String id;
    private String name;
    private String text;
    private double distance;

    house_cardhouseItem(String id,String url,String name,double distance, String text){
        this.id=id;
        this.url=url;
        this.name=name;
        this.distance=distance;
        this.text=text;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public String getText(){return text;}
}
