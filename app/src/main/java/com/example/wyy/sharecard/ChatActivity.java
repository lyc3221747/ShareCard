package com.example.wyy.sharecard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ImageView image = (ImageView) findViewById(R.id.chat_image);
        Glide.with(this)
                .load("http://img4.imgtn.bdimg.com/it/u=3015955796,810843421&fm=214&gp=0.jpg")
                .centerCrop()
                .into(image);

        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
    }
}
