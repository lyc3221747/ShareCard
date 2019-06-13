package com.example.wyy.sharecard;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.databinding.DataBindingUtil;
import android.view.MenuItem;
import android.view.View;


import com.example.wyy.sharecard.databinding.ActivityHomeBinding;
import com.githang.statusbar.StatusBarCompat;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import c.b.BP;
import cn.bmob.v3.Bmob;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class HomeActivity extends CheckPermissionsActivity {
    private ActivityHomeBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_home);


        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(getApplicationContext(), "3b2c81abf245aea9447f7a2454018cb1");
        BP.init("3b2c81abf245aea9447f7a2454018cb1");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);



        replaceFragment(new FirstFragment());
        bind.bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);





        initView();
    }






    private void initView() {




        // disable all animations
        bind.bnve.enableAnimation(true);
        bind.bnve.enableShiftingMode(false);
        bind.bnve.enableItemShiftingMode(false);


        // add badge
        //addBadgeAt(2, 1);
    }

    //添加红点标记
    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bind.bnve.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) ;
                        //标记移除后
                    }
                });
    }

    //替代碎片
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.commit();
    }

    //导航栏点击事件
    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.i_first:
                    replaceFragment(new FirstFragment());
                    return  true;
                case R.id.i_second:
                    replaceFragment(new SecondFragment());
                    return true;
                /**
                case R.id.i_third:
                    replaceFragment(new ThirdFragment());
                    return true;
                */
                case R.id.i_fourth:
                    replaceFragment(new FourthFragment());
                    return true;
            }
            return false;
        }
    };



}