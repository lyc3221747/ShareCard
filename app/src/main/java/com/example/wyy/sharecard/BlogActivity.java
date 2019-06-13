package com.example.wyy.sharecard;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.githang.statusbar.StatusBarCompat;
import com.luck.indicator.EasyIndicator;

public class BlogActivity extends CheckPermissionsActivity {

    private EasyIndicator easy_indicator;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        vp = (ViewPager) findViewById(R.id.vp);

        easy_indicator = (EasyIndicator) findViewById(R.id.easy_indicator);

        easy_indicator.setTabTitles(new String[]{" 我的 ", " 刷刷 ", " 卡窝 ", " 评价 "});

        easy_indicator.setViewPage(vp, new FragmentAdapter(getSupportFragmentManager(),
                new Fragment[]{new FragmentTab1(), new FragmentTab2(),
                        new FragmentTab3(), new FragmentTab4()}));
    }

}
