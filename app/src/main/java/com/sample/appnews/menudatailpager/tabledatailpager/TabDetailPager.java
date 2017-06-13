package com.sample.appnews.menudatailpager.tabledatailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sample.appnews.R;
import com.sample.appnews.activity.NewsDetailActivity;
import com.sample.appnews.base.MenuDetaiBasePager;
import com.sample.appnews.bean.NewsCenterPagerBean;
import com.sample.appnews.bean.TabDetailPagerBean;
import com.sample.appnews.view.HorizontalScrollViewPager;
import com.sample.appnews.utils.CacheUtils;
import com.sample.appnews.utils.Constants;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 作者：Administrator on 2017/6/4 11:51
 * 作用：
 */
public class TabDetailPager extends MenuDetaiBasePager {
    public static final String REAL_ARRAY_ID = "real_array_id";
    private NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    private String url;
    private HorizontalScrollViewPager vp_tabdetail;
    private TextView tv_tabdetail;
    private LinearLayout ll_tabdetail;
    private PullToRefreshListView mPullToRefreshListView;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private MyListViewAdapter listViewAdapter;
    private ListView listView;
    /**
     * 下一页的联网路径
     */

    private String moreUrl;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;
    private InternalHandler internalHandler;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        //获取下拉刷新
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        //获取listview
        listView = mPullToRefreshListView.getRefreshableView();
        //顶部轮播布局
        View topnewsViwe = View.inflate(context, R.layout.topnews, null);
        tv_tabdetail = (TextView) topnewsViwe.findViewById(R.id.tv_tabdetail);
        ll_tabdetail = (LinearLayout) topnewsViwe.findViewById(R.id.ll_tabdetail);
        vp_tabdetail = (HorizontalScrollViewPager) topnewsViwe.findViewById(R.id.vp_tabdetail);
        //把顶部轮播图部分视图，以头的方式添加到ListView中
        listView.addHeaderView(topnewsViwe);
        // Set a listener to be invoked when the list should be refreshed.
        //下拉刷新设置监听
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(context, "Pull Down!", Toast.LENGTH_SHORT).show();
                getDataFromNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(context, "Pull Up!", Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(moreUrl)) {
                    //没有更多数据
                    Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                    mPullToRefreshListView.onRefreshComplete();
                } else {
                    getMoreDataFromNet();
                }
            }
        });
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }


    /**
     * 初始化数据
     *
     * @return
     */
    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        //把之前缓存的数据提取出来
        String saveJson = CacheUtils.getString(context, "tabDetailpager");
        if (!TextUtils.isEmpty(saveJson)) {
            //解析显示数据
            processData(saveJson);
        }
        //网络请求
        getDataFromNet();
    }

    //网络请求
    private void getDataFromNet() {
        preposition = 0;
        LogUtil.e("url地址===" + url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e(childrenBean.getTitle() + "页面请求成功====" + result);
                //缓存数据
                CacheUtils.putString(context, "tabDetailpager", result);
                processData(result);
                //隐藏下拉刷新控件-重写显示数据，更新时间
//                listview.onRefreshFinish(true);
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenBean.getTitle() + "使用xUtils3联网请求失败==" + ex.getMessage());
                //隐藏下拉刷新控件 - 不更新时间，只是隐藏
//                listview.onRefreshFinish(false);
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenBean.getTitle() + "使用xUtils3-onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenBean.getTitle() + "使用xUtils3-onFinished");
            }
        });
    }

    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("加载更多联网成功==" + result);
//                listview.onRefreshFinish(false);
                //刷新完成
                mPullToRefreshListView.onRefreshComplete();
                //把这个放在前面
                isLoadMore = true;
                //解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败onError==" + ex.getMessage());
//                listview.onRefreshFinish(false);
                //刷新完成
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多联网onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网onFinished");
            }
        });
    }

    /**
     * 解析显示数据
     *
     * @param json
     */

    //之前高亮点的位置
    private int preposition;

    private void processData(String json) {
        //解析json数据结果
        TabDetailPagerBean tabDetailPagerBean = processJson(json);
        moreUrl = "";
        if (TextUtils.isEmpty(tabDetailPagerBean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + tabDetailPagerBean.getData().getMore();
        }
        LogUtil.e("加载更多的地址===" + moreUrl);
        //默认和加载更多
        if (!isLoadMore) {
            //默认 获取头部新闻数据
            topnews = tabDetailPagerBean.getData().getTopnews();
            //头部viewpager设置adapter
            vp_tabdetail.setAdapter(new TabDetailPagerAdapter());
            //添加红点
            addPoint();
            //viewpager添加监听事件
            vp_tabdetail.addOnPageChangeListener(new MyPageChangeListener());
            tv_tabdetail.setText(topnews.get(preposition).getTitle());

            //获取新闻数据
            news = tabDetailPagerBean.getData().getNews();
            //listview设置适配器
            listViewAdapter = new MyListViewAdapter();
            listView.setAdapter(listViewAdapter);
        } else {
            //加载更多
            isLoadMore = false;
            //添加到原来的集合中
            news.addAll(tabDetailPagerBean.getData().getNews());
            //刷新适配器
            listViewAdapter.notifyDataSetChanged();
        }
        //发送消息4秒切换一次viewpager界面
        if (internalHandler == null) {
            internalHandler = new InternalHandler();

        }
        //把所有的消息列队了消息移除
        internalHandler.removeCallbacksAndMessages(null);
        //延迟4秒发送请求
        internalHandler.postDelayed(new MyRunnable(), 4000);

    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = (vp_tabdetail.getCurrentItem() + 1) % topnews.size();
            vp_tabdetail.setCurrentItem(item);
            internalHandler.postDelayed(new MyRunnable(), 4000);
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            //发送空消息
            internalHandler.sendEmptyMessage(0);

        }
    }


    /**
     * 添加红点
     */
    private void addPoint() {
        ll_tabdetail.removeAllViews();//移除所有的红点
        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            //设置背景选择器
            imageView.setBackgroundResource(R.drawable.point_seletor);
            LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));
            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
                prarms.leftMargin = DensityUtil.dip2px(8);
            }
            imageView.setLayoutParams(prarms);
            ll_tabdetail.addView(imageView);
        }
    }

    /**
     * 解析json数据
     *
     * @param json
     * @return
     */
    private TabDetailPagerBean processJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, TabDetailPagerBean.class);
    }

    /**
     * TabDetailPager适配器
     */
    private class TabDetailPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //设置图片默认北京
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            //获取轮播图片地址
            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String imageUrl = Constants.BASE_URL + topnewsBean.getTopimage();
            //联网请求图片
            //x.image().bind(imageView, imageUrl, imageOptions);
            //请求图片使用glide
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(imageView);
            //头部轮播图片设置触摸监听
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            LogUtil.e("按下");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            LogUtil.e("松开");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            internalHandler.postDelayed(new MyRunnable(), 4000);
                            break;
                    }
                    return true;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    //viewpager改变监听
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //设置文本
            tv_tabdetail.setText(topnews.get(position).getTitle());
            //对应点高亮点 之前灰色 现在高亮
            ll_tabdetail.getChildAt(preposition).setEnabled(false);
            ll_tabdetail.getChildAt(position).setEnabled(true);
            preposition = position;
        }

        private boolean isDragging = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                LogUtil.e("拖拽");
                isDragging = true;
                //拖拽要移除消息
                internalHandler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                LogUtil.e("惯性");
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(), 4000);

            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                LogUtil.e("静止");
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(), 4000);

            }
        }
    }

    /**
     * listView适配器
     */
    private class MyListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item_tabdetail = (ImageView) convertView.findViewById(R.id.iv_item_tabdetail);
                viewHolder.tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
                viewHolder.tv_item_date = (TextView) convertView.findViewById(R.id.tv_item_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //获取新闻信息
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            //获取图片utl
            String imageUrl = Constants.BASE_URL + newsBean.getListimage();
            String title = newsBean.getTitle();
            String date = newsBean.getPubdate();

            // x.image().bind(viewHoder.iv_item_tabdetail, imageUrl, imageOptions);
            //请求图片使用glide
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.iv_item_tabdetail);
            //设置时间
            viewHolder.tv_item_date.setText(date);
            //设置标题
            viewHolder.tv_item_title.setText(title);

            String idArray = CacheUtils.getString(context, REAL_ARRAY_ID);
            if (idArray.contains(newsBean.getId() + "")) {
                viewHolder.tv_item_title.setTextColor(Color.GRAY);
            } else {
                viewHolder.tv_item_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_item_tabdetail;
        TextView tv_item_title;
        TextView tv_item_date;


    }

    //listView点击监听
    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int realPosition = position - 2;
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(realPosition);
            //Toast.makeText(context, "newsData==id==" + newsBean.getId() + ",newsData_title==" + newsBean.getTitle(), Toast.LENGTH_SHORT).show();
            //取出保存的id集合
            String idArray = CacheUtils.getString(context, REAL_ARRAY_ID);
            //判断是否存在 不存在保存 刷新适配器
            if (!idArray.contains(newsBean.getId() + "")) {
                CacheUtils.putString(context, REAL_ARRAY_ID, newsBean.getId() + ",");
                //刷新适配器
                listViewAdapter.notifyDataSetChanged();
            }
            //跳转到新闻详情
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constants.BASE_URL + newsBean.getUrl());
            context.startActivity(intent);


        }
    }


}
