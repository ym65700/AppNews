package com.sample.appnews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 作者：Administrator on 2017/5/31 15:07
 * 作用：引导页适配器
 */
public class GuidePageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ImageView> imageViews;

    public GuidePageAdapter(Context context, ArrayList<ImageView> imageViews) {
        this.context = context;
        this.imageViews = imageViews;
    }

    /**
     * 返回数据总个数
     *
     * @return
     */
    @Override
    public int getCount() {
        return imageViews.size();
    }

    /**
     * 判断
     *
     * @param view   当前视图
     * @param object instantiateItem返回的结果
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 销毁页面
     *
     * @param container viewPage
     * @param position  要销毁页面的位置
     * @param object    要销毁的页面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 获取视图
     *
     * @param container viewpage
     * @param position  要创建页面的位置
     * @return  返回和创建当前页面有关系的值
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        container.addView(imageView);
        return imageView;
    }
}
