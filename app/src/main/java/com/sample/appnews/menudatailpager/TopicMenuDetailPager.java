package com.sample.appnews.menudatailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sample.appnews.R;
import com.sample.appnews.activity.MainActivity;
import com.sample.appnews.base.MenuDetaiBasePager;
import com.sample.appnews.bean.NewsCenterPagerBean;
import com.sample.appnews.menudatailpager.tabledatailpager.TabDetailPager;
import com.sample.appnews.menudatailpager.tabledatailpager.TopicDetailPager;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：Administrator on 2017/6/3 01:03
 * 作用：专题详情页面
 */

public class TopicMenuDetailPager extends MenuDetaiBasePager {
    @ViewInject(R.id.vp_news_detail)
    private ViewPager vp_news_detail;
    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;
    /**
     * 页签页面的数据的集合-数据
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> children;
    /**
     * 页签页面的集合-页面
     */
    private ArrayList<TopicDetailPager> topicDetailPagers;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        //获取页签页面的数据的集合
        children = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topic_menu_detail, null);
        x.view().inject(TopicMenuDetailPager.this, view);
        //next点击下一页
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_news_detail.setCurrentItem(vp_news_detail.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面数据被初始化了..");
        //准备新闻详情页面的数据
        topicDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            topicDetailPagers.add(new TopicDetailPager(context, children.get(i)));

        }
        //viewpager设置适配器
        vp_news_detail.setAdapter(new MyPagerAdapter());
        //viewpagerIndicator和viewpager相关联
        indicator.setViewPager(vp_news_detail);
        //viewpagerIndicator设置监听
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //判断首个页面才能启动侧滑菜单
                if (0 == position) {
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * newMenuDetailPager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topicDetailPagers.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicDetailPager topicDetailPager = topicDetailPagers.get(position);
            View rootView = topicDetailPager.rootView;
            topicDetailPager.initData();//初始化数据
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

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
