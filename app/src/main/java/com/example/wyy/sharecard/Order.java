package com.example.wyy.sharecard;

import cn.bmob.v3.BmobObject;

/**
 * Created by wyy on 17-7-15.
 */

public class Order extends BmobObject {

    private String user_id;
    private String card_id;
    private String owner_id;
    private String receiver_name;
    private String tel;
    private Integer SendMethod;     //0未选择;1同城派送,2面对面
    private String address_data;
    private Double total;
    private String PayId;

    public String getPayId() {
        return PayId;
    }

    public void setPayId(String payId) {
        PayId = payId;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getAddress_data() {
        return address_data;
    }

    public void setAddress_data(String address_data) {
        this.address_data = address_data;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getSendMethod() {
        return SendMethod;
    }

    public void setSendMethod(Integer sendMethod) {
        SendMethod = sendMethod;
    }



}
