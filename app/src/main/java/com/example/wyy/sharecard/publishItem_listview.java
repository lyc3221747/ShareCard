package com.example.wyy.sharecard;

/**
 * Created by z on 2019/3/27.
 */

public class publishItem_listview {
    private String card_name;
    private String state;
    private String card_id;

    publishItem_listview(String card_id, String card_name, String state) {
        this.card_id = card_id;
        this.card_name = card_name;
        this.state = state;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
