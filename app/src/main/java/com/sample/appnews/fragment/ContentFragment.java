package com.sample.appnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sample.appnews.R;
import com.sample.appnews.activity.MainActivity;
import com.sample.appnews.adapter.ContentFragmentAdpter;
import com.sample.appnews.base.BaseFragment;
import com.sample.appnews.base.BasePager;
import com.sample.appnews.pager.GovaffairPager;
import com.sample.appnews.pager.HomePager;
import com.sample.appnews.pager.NewsCenterPager;
import com.sample.appnews.pager.SettingPager;
import com.sample.appnews.pager.SmartServicePager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者：Administrator on 2017/5/31 22:11
 * 作用：
 */

public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.vp_content)
    private ViewPager vp_content;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    //五个页面的集合
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        //实例化控件
        View view = View.inflate(context, R.layout.content_fragment, null);
        //把视图注入到框架中 让contentfragment与view关联起来
        x.view().inject(ContentFragment.this, view);
        return view;
    }

    @Override
    public void initData() {
        //初始化五个页面，并且放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));//主页面
        basePagers.add(new NewsCenterPager(context));//新闻中心页面
        basePagers.add(new SmartServicePager(context));//智慧服务页面
        basePagers.add(new GovaffairPager(context));//政要指南页面
        basePagers.add(new SettingPager(context));//设置中心面
        //viewpager设置适配器 传入集合
        vp_content.setAdapter(new ContentFragmentAdpter(basePagers));
        //RadioGroup设置点击监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //viewpager设置页面监听
        vp_content.addOnPageChangeListener(new MyOnPageChangeListener());
        //默认设置首页
        rg_main.check(R.id.rb_home);
        //默认初始化数据
        basePagers.get(0).initData();
        //设置模式SlidingMenu不可以滑动
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
    }

    /**
     * 得到新闻中心
     *
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        /**
         * @param group RadioGroup
         * @param checkedId 被选中的RadioButton的id
         */
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home://主页radioButton的id
                    vp_content.setCurrentItem(0, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter://新闻中心radioButton的id
                    vp_content.setCurrentItem(1, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smartservice://智慧服务radioButton的id
                    vp_content.setCurrentItem(2, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair://政要指南的RadioButton的id
                    vp_content.setCurrentItem(3, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting://设置中心RadioButton的id
                    vp_content.setCurrentItem(4, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }

        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调这个方法
         *
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            //BasePager basePager = basePagers.get(position);
            //调用被选中的页面的initData方法
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 根据传人的参数设置是否让SlidingMenu可以滑动
     */
    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);

    }
}
