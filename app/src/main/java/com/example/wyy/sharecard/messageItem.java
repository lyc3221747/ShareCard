package com.example.wyy.sharecard;

/**
 * Created by sir on 17-7-8.
 */

public class messageItem {
    private String user_image_url;
    private  String card_image_url;
    private String user;
    private String message;

    public messageItem(String user_image_url,String card_image_url,String user,String message) {
        this.user_image_url = user_image_url;
        this.card_image_url = card_image_url;
        this.user = user;
        this.message =message;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public String getCard_image_url() {
        return card_image_url;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
