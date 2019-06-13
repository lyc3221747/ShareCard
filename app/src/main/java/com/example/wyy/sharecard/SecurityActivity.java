package com.example.wyy.sharecard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.githang.statusbar.StatusBarCompat;

public class SecurityActivity extends CheckPermissionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
    }
}
