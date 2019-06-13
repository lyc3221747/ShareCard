package com.example.wyy.sharecard;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by wyy on 17-7-14.
 */

public class card extends BmobObject {
    private String user_id;     //主人id
    private String name;        //名字
    private String Inf;         //详情
    private Double org_price;   //原价
    private Double now_price;   //出价
    private Integer sort;       //分类    1:电子卡  2:会员卡   3:优惠券
    private String card_house;  //卡窝
    private Double Latitude;   //纬度
    private Double Longitude;  //经度
    private String time;        //发卡时间
    private String address;     //发卡地址
    private Integer state;      //卡券状态  0:已出售   1:未出售

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public Double getNow_price() {
        return now_price;
    }

    public Double getOrg_price() {
        return org_price;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getCard_house() {
        return card_house;
    }

    public String getInf() {
        return Inf;
    }

    public Integer getSort() {
        return sort;
    }

    public void setCard_house(String card_house) {
        this.card_house = card_house;
    }

    public void setInf(String inf) {
        Inf = inf;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNow_price(Double now_price) {
        this.now_price = now_price;
    }

    public void setOrg_price(Double org_price) {
        this.org_price = org_price;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
