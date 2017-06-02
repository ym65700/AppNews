package com.sample.appnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sample.appnews.R;
import com.sample.appnews.SplashActivity;
import com.sample.appnews.adapter.GuidePageAdapter;
import com.sample.appnews.utils.CacheUtils;
import com.sample.appnews.utils.DensityUtil;

import java.util.ArrayList;

/**
 * 引导页面
 */

public class GuideActivity extends Activity {
    private ViewPager vp;
    private ArrayList<ImageView> imageViews;
    private Button btn_welcome;
    //引导页图片
    int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    //两点间的距离
    private int leftmax;
    private int widthdp;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vp = (ViewPager) findViewById(R.id.vp_guide);
        btn_welcome = (Button) findViewById(R.id.btn_guide_welcome);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);

        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {
            ImageView iv = new ImageView(this);
            //设置背景
            iv.setBackgroundResource(ids[i]);
            //添加到集合
            imageViews.add(iv);
            //创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.dot_normal);
            //把单位当成dp转成对应的像素
            widthdp = DensityUtil.dip2px(this, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdp, widthdp);
            if (i != 0) {
                //不是第0个，所有的点距离左边有10个像数
                params.leftMargin = widthdp;
            }
            point.setLayoutParams(params);
            //添加到线性布局
            ll_point_group.addView(point);

        }
        //设置viewpage适配器
        vp.setAdapter(new GuidePageAdapter(this, imageViews));
        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距都有了
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //得到屏幕滑动的百分比
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置点击事件
        btn_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存进入主页面
                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.FIRST_MAIN, true);
                //跳转主页面
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                //关闭引导页面
                finish();
            }
        });

    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            //执行不止一次
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);
            // 间距  = 第1个点距离左边的距离 - 第0个点距离左边的距
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();


        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面回调了会回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //两点间滑动距离对应的坐标 = 原来的起始位置 +  两点间移动的距离
            int leftmargin = (int) (position * leftmax + (positionOffset * leftmax));
            //params.leftMargin = 两点间滑动距离对应的坐标
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param position 被选中页面的对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                btn_welcome.setVisibility(View.VISIBLE);
            } else {
                btn_welcome.setVisibility(View.GONE);
            }
        }

        /**
         * 当ViewPager页面滑动状态发生变化的时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
