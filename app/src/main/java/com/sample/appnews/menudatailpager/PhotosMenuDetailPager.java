package com.sample.appnews.menudatailpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.sample.appnews.R;
import com.sample.appnews.base.MenuDetaiBasePager;
import com.sample.appnews.bean.NewsCenterPagerBean;
import com.sample.appnews.bean.PhotosDetailpagerBean;
import com.sample.appnews.utils.CacheUtils;
import com.sample.appnews.utils.Constants;
import com.sample.appnews.volley.VolleyManager;

import org.w3c.dom.Text;
import org.xutils.common.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.sample.appnews.utils.Constants.NEWSCENTER_PAGER_URL;

/**
 * 作者：Administrator on 2017/6/3 01:16
 * 作用：图片详情页面
 */

public class PhotosMenuDetailPager extends MenuDetaiBasePager {
    private ListView listView;
    private GridView gridView;
    List<PhotosDetailpagerBean.DataBean.NewsBean> news;
    private String url;
    /**
     * 页签页面的数据的集合-数据
     */
    private NewsCenterPagerBean.DataBean dataBean;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        //获取页签页面的数据的集合
        this.dataBean = dataBean;


    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_menu_detail, null);
        listView = (ListView) view.findViewById(R.id.lv);
        gridView = (GridView) view.findViewById(R.id.gv);

        //imgbtn_swich_list_grid.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图片详情页面数据被初始化了..");
        url = Constants.BASE_URL + dataBean.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNetByVolley();

    }

    /**
     * 使用Volley联网请求数据
     */
    private void getDataFromNetByVolley() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用Volley联网");
                //缓存数据
                CacheUtils.putString(context, url, result);
                //解析显示数据
                processData(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用Volley联网请求失败==" + volleyError.getMessage());
            }
        }) {
            //解决乱码
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        //添加队列
        VolleyManager.getRequestQueue().add(request);

    }

    //解析显示数据
    private void processData(String result) {
        PhotosDetailpagerBean photosMenuDetailPager = processJson(result);
        //设置适配器
        isShowView = true;
        news = photosMenuDetailPager.getData().getNews();
        listView.setAdapter(new MySwichAdapter());
    }

    /**
     * true,显示ListView，隐藏GridView
     * false,显示GridView,隐藏ListView
     */
    private boolean isShowView = true;

    public void swichListAndGrid(ImageButton imgbtn_swich_list_grid) {
        if (isShowView) {
            isShowView = false;
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new MySwichAdapter());
            gridView.setVisibility(View.GONE);
            imgbtn_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        } else {
            isShowView = true;
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new MySwichAdapter());
            listView.setVisibility(View.GONE);
            imgbtn_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);
        }
    }


    private class MySwichAdapter extends BaseAdapter {
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
                convertView = View.inflate(context, R.layout.item_photos, null);
                viewHolder = new ViewHolder();
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置得到对应的数据
            String title = news.get(position).getTitle();
            viewHolder.tv.setText(title);
            String iamgeUrl = Constants.BASE_URL + news.get(position).getSmallimage();
            //使用Volley请求图片-设置图片了
            loaderImage(viewHolder, iamgeUrl);

            return convertView;
        }
    }

    //图片加载
    private void loaderImage(final ViewHolder viewHolder, String iamgeUrl) {
        //设置tag
        viewHolder.iv.setTag(iamgeUrl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {
                    if (viewHolder.iv != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.iv.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.iv.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(iamgeUrl, listener);
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }

    private PhotosDetailpagerBean processJson(String json) {
        return new Gson().fromJson(json, PhotosDetailpagerBean.class);
    }
}
