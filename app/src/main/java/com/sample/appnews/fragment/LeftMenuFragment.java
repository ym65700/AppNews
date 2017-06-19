package com.sample.appnews.fragment;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sample.appnews.R;
import com.sample.appnews.activity.MainActivity;
import com.sample.appnews.base.BaseFragment;
import com.sample.appnews.bean.NewsCenterPagerBean;
import com.sample.appnews.pager.NewsCenterPager;
import com.sample.appnews.utils.DensityUtil;

import org.w3c.dom.Text;
import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 作者：Administrator on 2017/5/31 20:31
 * 作用：
 */
public class LeftMenuFragment extends BaseFragment {
    private LeftMenuFragmentAdapter adapter;
    private List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    /**
     * 点击的位置
     */
    private int prePosition;

    @Override
    public View initView() {
        listView = new ListView(context);
        //设置分割线高度为0
        listView.setDividerHeight(0);
        listView.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        //设置去除listview的拖动背景色
        listView.setCacheColorHint(Color.TRANSPARENT);
        //设置按下list的item颜色不变
        listView.setSelector(android.R.color.transparent);
        //设置listview的item监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，变成红色
                prePosition = position;
                //getCount()-->getView
                adapter.notifyDataSetChanged();
                //把左侧菜单关闭
                MainActivity main = (MainActivity) context;
                main.getSlidingMenu().toggle();//关<->开


                //3.切换到对应的详情页面：新闻详情页面，专题详情页面，图组详情页面，互动详情页面
                swichPager(prePosition);
            }
        });
        return listView;
    }

    /**
     * 根据位置切换不同详情页面
     *
     * @param position
     */
    private void swichPager(int position) {
        MainActivity main = (MainActivity) context;
        //MainActivity得到contentFragment
        ContentFragment contentFragment = main.getContentFragment();
        // ContentFragment得到NewsCenterPager
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        //根据位置切换不同详情页面
        newsCenterPager.swichPager(position);

    }

    @Override
    public void initData() {
        LogUtil.e("左侧菜单数据被初始化了");
    }

    /**
     * 接收NewsCenterPager传过来的数据数据
     *
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.e("title==" + data.get(i).getTitle());
        }
        //创建listview适配器
        adapter = new LeftMenuFragmentAdapter();
        listView.setAdapter(adapter);
        //设置默认页面
        swichPager(prePosition);
    }

    private class LeftMenuFragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
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
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu, null);
            textView.setText(data.get(position).getTitle());
            textView.setEnabled(position == prePosition);
            return textView;
        }
    }


}
