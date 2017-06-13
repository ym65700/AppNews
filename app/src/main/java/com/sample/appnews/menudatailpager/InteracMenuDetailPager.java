package com.sample.appnews.menudatailpager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sample.appnews.base.MenuDetaiBasePager;
import com.sample.appnews.bean.NewsCenterPagerBean;

import org.xutils.common.util.LogUtil;


/**
 * 作者：Administrator on 2017/6/3 01:03
 * 作用：互动详情页面
 */

public class InteracMenuDetailPager extends MenuDetaiBasePager {

    private TextView textView;

    public InteracMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
    }

    @Override
    public View initView() {

        textView = new TextView(context);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面数据被初始化了..");
        textView.setText("互动详情页面");
    }
}
