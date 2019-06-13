package com.example.wyy.sharecard;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 振丿Love on 2017/5/27.
 */

public class MyUser extends BmobUser {
    private BmobFile head_pic;
    private String name;
    private String pass;
    private String realName;
    private String address;
    private String reciever;
    private Boolean isEmpty = false;
    private String Sore_Inf;
    private String phone_num;

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setSore_Inf(String sore_Inf) {
        Sore_Inf = sore_Inf;
    }

    public String getSore_Inf() {
        return Sore_Inf;
    }

    public BmobFile getHead_pic() {
        return head_pic;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public Boolean getIsEmpty() {
        return isEmpty;
    }

    public String getReciever() {
        return reciever;
    }

    public String getPass() {
        return pass;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHead_pic(BmobFile head_pic) {
        this.head_pic = head_pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setIsEmpty(Boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
