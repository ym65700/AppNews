package com.sample.appnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：Administrator on 2017/6/2 12:31
 * 作用：不可以滑动的viewpager
 */

public class NoScrollViewPager extends ViewPager {
    //在代码中实例化用该方法
    public NoScrollViewPager(Context context) {
        super(context);
    }

    //在布局文件中使用该类 实例化该构造方法 这个方法不能少
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写触摸方法 消掉
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    //拦截滑动事件 上层不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
