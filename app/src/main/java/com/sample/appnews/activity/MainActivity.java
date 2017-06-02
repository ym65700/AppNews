package com.sample.appnews.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sample.appnews.R;
import com.sample.appnews.fragment.ContentFragment;
import com.sample.appnews.fragment.LeftMenuFragment;
import com.sample.appnews.utils.DensityUtil;

/**
 * 作者：Administrator on 2017/5/30 00:21
 * 作用：主界面
 */

public class MainActivity extends SlidingFragmentActivity {
    //主页内容标记
    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    //左侧侧滑菜单标记
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        super.onCreate(savedInstanceState);
        //初始化侧滑菜单
        initSlidingMenu();
        //初始化fragment
        initFragment();

    }


    private void initSlidingMenu() {
        //设置主页面
        setContentView(R.layout.activity_main);
        //设置侧拉页面
        setBehindContentView(R.layout.activity_leftmenu);
        //获取侧滑菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置显示模式 左侧
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置滑动模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //6.设置主页占据的宽度 转化成密度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));
        //slidingMenu.setBehindOffset((int) (screeWidth*0.625));
    }

    private void initFragment() {
        //得到事物 开启事物
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //替换布局
        ft.replace(R.id.fl_main_content, new ContentFragment(), MAIN_CONTENT_TAG);//主页
        ft.replace(R.id.fl_main_leftmenu, new LeftMenuFragment(), LEFTMENU_TAG);//左侧菜单
        //提交事务
        ft.commit();
    }
}
