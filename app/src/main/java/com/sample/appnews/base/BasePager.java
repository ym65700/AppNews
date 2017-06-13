package com.sample.appnews.base;

import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.appnews.R;
import com.sample.appnews.activity.MainActivity;


/**
 * 作者：Administrator on 2017/6/1 18:27
 * 作用：公共类
 */

public class BasePager {
    //上下文 mainactivity
    public final Context context;
    //侧滑按钮
    public ImageView imgbtn_leftmenu;
    //显示标题
    public TextView tv_title;
    //加载各个子页面
    public FrameLayout fl_page_content;
    //视图代表各个不同的页面
    public View rootView;
    //组图布局切换按钮
    public ImageButton imgbtn_swich_list_grid;


    public BasePager(Context context) {
        //构造方法一执行 视图就被初始化
        this.context = context;
        rootView = initView();
    }

    //初始化公共视图
    public View initView() {
        //基类的页面
        View view = View.inflate(context, R.layout.base_page, null);
        imgbtn_leftmenu = (ImageView) view.findViewById(R.id.imgbtn_leftmenu);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        fl_page_content = (FrameLayout) view.findViewById(R.id.fl_page_content);
        imgbtn_swich_list_grid = (ImageButton) view.findViewById(R.id.imgbtn_swich_list_grid);
        imgbtn_leftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity) context;
                main.getSlidingMenu().toggle();//关<->开
            }
        });
        return view;
    }

    //初始化公共数据 当孩子需要初始化数据 或者绑定数据 网络请求并且绑定 重写该方法
    public void initData() {

    }
}
