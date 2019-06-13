package com.example.wyy.sharecard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.githang.statusbar.StatusBarCompat;

public class WebActivity extends AppCompatActivity {

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        Intent intent = getIntent();
        String data = intent.getStringExtra("extra_data");
        String url = data;

        webview = (WebView) findViewById(R.id.webview);
        // 开启 localStorage
        webview.getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        webview.getSettings().setJavaScriptEnabled(false);
        // 启动缓存
        webview.getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //使用自定义的WebViewClient
        webview.setWebViewClient(new WebViewClient()
        {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(url);
    }


    // 覆盖onKeydown 添加处理WebView 界面内返回事件处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack())
        {
            webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}