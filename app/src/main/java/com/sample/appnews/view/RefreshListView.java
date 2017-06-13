package com.sample.appnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sample.appnews.R;

/**
 * 作者：Administrator on 2017/6/6 11:17
 * 作用：
 */

public class RefreshListView extends ListView {
    //下拉刷新和顶部轮播图
    private LinearLayout headerView;


    private View ll_pull_down;
    private ImageView iv_arrow;
    private ProgressBar progress_bar;
    private TextView tv_date;
    private TextView tv_state;
    //下拉刷新控件的高
    private int pullDownRefreshHeight;

    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;

    /**
     * 手松刷新
     */
    public static final int RELEASE_REFRESH = 1;


    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;


    /**
     * 当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;
    private Animation upAnimation;
    private Animation downAnimation;


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        //下拉刷新控件
        ll_pull_down = headerView.findViewById(R.id.ll_pull_down);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        progress_bar = (ProgressBar) headerView.findViewById(R.id.progress_bar);
        tv_date = (TextView) headerView.findViewById(R.id.tv_date);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        //测量
        ll_pull_down.measure(0, 0);
        pullDownRefreshHeight = ll_pull_down.getMeasuredHeight();
        ll_pull_down.setPadding(0, -pullDownRefreshHeight, 0, 0);
        //添加list头
        addHeaderView(headerView);


    }


    private float startY = -1;

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //滑动开始的位置
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }
                if (currentStatus == REFRESHING) {
                    break;
                }
                //滑动结束的位置
                float endY = ev.getY();
                //滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    int paddingTop = (int) (-pullDownRefreshHeight + distanceY);
                    //ll_pull_down.setPadding(0, -paddingTop, 0, 0);
                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        //手松刷新状态
                        currentStatus = RELEASE_REFRESH;
                        //更新状态
                        refreshViewState();
                    }
                    //动态显示下拉刷新控件
                    ll_pull_down.setPadding(0, -paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    //完全隐藏
                    ll_pull_down.setPadding(0, -pullDownRefreshHeight, 0, 0);

                } else if (currentStatus == RELEASE_REFRESH) {
                    //设置刷新
                    currentStatus = REFRESHING;
                    //更新状态
                    refreshViewState();
                    //完全显示
                    ll_pull_down.setPadding(0, 0, 0, 0);
                    //回调接口
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshViewState() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH:
                iv_arrow.setAnimation(downAnimation);
                tv_state.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH:
                iv_arrow.setAnimation(upAnimation);
                tv_state.setText("手松刷新...");
                break;
            case REFRESHING:
                tv_state.setText("正在刷新...");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                progress_bar.setVisibility(VISIBLE);
                break;
        }
    }
}
