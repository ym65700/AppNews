package com.sample.appnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.sample.appnews.base.BasePager;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 作者：Administrator on 2017/6/1 22:10
 * 作用：
 */
public class ContentFragmentAdpter extends PagerAdapter {
    private final ArrayList<BasePager> basePagers;

    public ContentFragmentAdpter(ArrayList<BasePager> basePagers) {
        this.basePagers = basePagers;
    }

    @Override
    public int getCount() {
        return basePagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //各个页面实例
        BasePager basePager = basePagers.get(position);
        //各个子页面
        View rootView = basePager.rootView;
        container.addView(rootView);
        return rootView;
    }
}
