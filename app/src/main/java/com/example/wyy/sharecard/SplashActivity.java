package com.example.wyy.sharecard;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        ImageView image = (ImageView) findViewById(R.id.splash_image);
        Glide.with(this).load(R.drawable.splash).centerCrop().into(image);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null)
            actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent it = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(it);
                SplashActivity.this.finish();
            }
        }, 300);

    }
}