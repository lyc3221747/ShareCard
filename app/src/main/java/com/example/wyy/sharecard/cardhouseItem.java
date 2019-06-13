package com.example.wyy.sharecard;

/**
 * Created by wyy on 17-7-7.
 */

public class cardhouseItem {
    private String id;
    private  String image_url;
    cardhouseItem(String id,String image_url){this.id=id;this.image_url=image_url;}

    public String getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }
}
