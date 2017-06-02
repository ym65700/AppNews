package com.sample.appnews.fragment;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.sample.appnews.base.BaseFragment;

/**
 * 作者：Administrator on 2017/5/31 20:31
 * 作用：
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView textView = new TextView(context);
        textView.setText("left_menu");
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    @Override
    public void initData() {

    }
}
