package com.sample.appnews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.sample.appnews.R;

public class NewsDetailActivity extends Activity implements View.OnClickListener {
    private WebView webview;
    private ProgressBar pbLoading;
    private ImageButton imgbtnBack;
    private ImageButton imgbtnTextsize;
    private ImageButton imgbtnShare;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
        getData();
    }

    private void getData() {
        //获取网页详情url
        String url = getIntent().getStringExtra("url");
        webSettings = webview.getSettings();
        //设置支持javascript
        webSettings.setJavaScriptEnabled(true);
        //设置支持双击变大小
        webSettings.setUseWideViewPort(true);
        //设置缩放按钮
        // webSettings.setBuiltInZoomControls(true);
        //设置文字打下
        // webSettings.setTextZoom(100);
        //不让跳转到浏览器
        webview.setWebViewClient(new WebViewClient() {
            //当加载页面完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载进度条隐藏
                pbLoading.setVisibility(View.GONE);
            }
        });
        //加载地址
        webview.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.imgbtn_textsize:
                showChangTextSizeDialog();
                break;
            case R.id.imgbtn_share:
                break;
        }
    }

    private String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangTextSizeDialog() {
        //创建dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                //根据单选框位置 设置字体大小
                changTextSize(realSize);
            }
        });
        dialog.show();
    }

    private void changTextSize(int realSize) {
        switch (realSize) {
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
        }

    }


    private void findViews() {
        webview = (WebView) findViewById(R.id.webview);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        imgbtnBack = (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtnTextsize = (ImageButton) findViewById(R.id.imgbtn_textsize);
        imgbtnShare = (ImageButton) findViewById(R.id.imgbtn_share);
        imgbtnBack.setOnClickListener(this);
        imgbtnTextsize.setOnClickListener(this);
        imgbtnShare.setOnClickListener(this);
        imgbtnBack.setVisibility(View.VISIBLE);
        imgbtnTextsize.setVisibility(View.VISIBLE);
        imgbtnShare.setVisibility(View.VISIBLE);
    }
}
