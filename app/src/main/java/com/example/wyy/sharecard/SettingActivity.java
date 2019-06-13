package com.example.wyy.sharecard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#fedb14"));
        // back 监听
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // logout 退出登录监听
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 17-7-16
                Log.d("setting", "onClick: ");
                BmobUser.logOut();
                Toast.makeText(getApplicationContext(),"退出登录",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
