package com.sample.appnews.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.sample.appnews.base.BasePager;

import org.xutils.common.util.LogUtil;

/**
 * 作者：Administrator on 2017/6/1 22:23
 * 作用：
 */
public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
        //1.设置标题
        tv_title.setText("新闻中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_page_content.addView(textView);
        //4.绑定数据
        textView.setText("新闻中心内容");
    }
}
