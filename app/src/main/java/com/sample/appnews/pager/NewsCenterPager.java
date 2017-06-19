package com.sample.appnews.pager;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sample.appnews.activity.MainActivity;
import com.sample.appnews.base.BasePager;
import com.sample.appnews.base.MenuDetaiBasePager;
import com.sample.appnews.bean.NewsCenterPagerBean;
import com.sample.appnews.bean.NewsCenterPagerBean2;
import com.sample.appnews.fragment.LeftMenuFragment;
import com.sample.appnews.menudatailpager.InteracMenuDetailPager;
import com.sample.appnews.menudatailpager.NewsMenuDetailPager;
import com.sample.appnews.menudatailpager.PhotosMenuDetailPager;
import com.sample.appnews.menudatailpager.TopicMenuDetailPager;
import com.sample.appnews.utils.CacheUtils;
import com.sample.appnews.utils.Constants;
import com.sample.appnews.volley.VolleyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static com.sample.appnews.utils.Constants.NEWSCENTER_PAGER_URL;

/**
 * 作者：Administrator on 2017/6/1 22:23
 * 作用：新闻页面
 */
public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity context) {
        super(context);
    }

    private List<NewsCenterPagerBean.DataBean> data;

    /**
     * 详情页面的集合
     */
    private ArrayList<MenuDetaiBasePager> detaiBasePagers;

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
        imgbtn_leftmenu.setVisibility(View.VISIBLE);
        //1.设置标题
        tv_title.setText("新闻中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_page_content.addView(textView);
        //4.绑定数据
        textView.setText("新闻中心内容");
        //得到缓存数据
        String saveJson = CacheUtils.getString(context, NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        //使用xUtils3联网请求数据
        //getDataFromNet();
        getDataFromNetByVolley();


    }

    /**
     * 使用Volley联网请求数据
     */
    private void getDataFromNetByVolley() {
        StringRequest request = new StringRequest(Request.Method.GET, NEWSCENTER_PAGER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用Volley联网");
                //缓存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);
                //解析数据
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

    /**
     * 使用xUtils3联网请求数据
     */
    private void getDataFromNet() {
        RequestParams rp = new RequestParams(NEWSCENTER_PAGER_URL);
        x.http().get(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);
                //解析数据
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3-onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }


    /**
     * 解析json数据和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        NewsCenterPagerBean bean = processJson(json);
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("使用Gson解析json数据成功NewsCenterPagerBean2-title2-------------------------==" + title);
        data = bean.getData();
        //给左侧菜单传递数据
        MainActivity main = (MainActivity) context;
        //得到左侧菜单
        LeftMenuFragment leftMenuFragment = main.getLeftMenuFragment();
        //添加详情页面
        detaiBasePagers = new ArrayList<>();
        detaiBasePagers.add(new NewsMenuDetailPager(context, data.get(0)));//新闻详情页面
        detaiBasePagers.add(new TopicMenuDetailPager(context, data.get(0)));//专题详情页面
        detaiBasePagers.add(new PhotosMenuDetailPager(context, data.get(2)));//图组详情页面
        detaiBasePagers.add(new InteracMenuDetailPager(context, data.get(2)));//互动详情页面
        //把数据传递给左侧菜单
        leftMenuFragment.setData(data);


    }

    /**
     * 解析json数据：1,使用系统的API解析json；2,使用第三方框架解析json数据，例如Gson,fastjson
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean processJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, NewsCenterPagerBean.class);
    }

    /**
     * 使用Android系统自带的API解析json数据
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean2 processJson2(String json) {
//        try {
//            JSONObject object = new JSONObject(json);
//            NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
//
//            int retcode = object.optInt("retcode");
//            bean2.setRetcode(retcode);
//
//            JSONArray data = object.optJSONArray("data");
//            if (data != null && data.length() > 0) {
//                ArrayList<NewsCenterPagerBean2.DataBean> dataBeans = new ArrayList<>();
//                //设置列表数据
//                bean2.setDataBeans(dataBeans);
//                //for循环，解析每条数据
//                for (int i = 0; i < data.length(); i++) {
//                    JSONObject jsonObject = (JSONObject) data.get(i);
//                    NewsCenterPagerBean2.DataBean dataBean = new NewsCenterPagerBean2.DataBean();
//                    int id = jsonObject.optInt("id");
//                    dataBean.setId(id);
//                    int type = jsonObject.optInt("type");
//                    dataBean.setType(type);
//                    String title = jsonObject.optString("title");
//                    dataBean.setTitle(title);
//                    String url = jsonObject.optString("url");
//                    dataBean.setUrl(url);
//                    String url1 = jsonObject.optString("url1");
//                    dataBean.setUrl1(url1);
//                    String dayurl = jsonObject.optString("dayurl");
//                    dataBean.setDayurl(dayurl);
//                    String excurl = jsonObject.optString("excurl");
//                    dataBean.setExcurl(excurl);
//                    String weekurl = jsonObject.optString("weekurl");
//                    dataBean.setWeekurl(weekurl);
//
//                    NewsCenterPagerBean2.ChildrenBean children  =jsonObject.optInt("ChildrenBean")
//
//                }
//
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return null;
    }

    /**
     * 根据位置切换详情页面
     *
     * @param position
     */
    public void swichPager(int position) {
        if (position < detaiBasePagers.size()) {
            //设置标题
            tv_title.setText(data.get(position).getTitle());
            //移除之前内容
            fl_page_content.removeAllViews();
            //添加新内容
            MenuDetaiBasePager menuDetaiBasePager = detaiBasePagers.get(position);
            View rootView = menuDetaiBasePager.rootView;
            //初始化数据
            menuDetaiBasePager.initData();
            fl_page_content.addView(rootView);
            if (position == 2) {
                imgbtn_swich_list_grid.setVisibility(View.VISIBLE);
                imgbtn_swich_list_grid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //1.得到图组详情页面对象
                        PhotosMenuDetailPager photosMenuDetailPager = (PhotosMenuDetailPager) detaiBasePagers.get(2);
                        //2.调用图组对象的切换ListView和GridView的方法
                        photosMenuDetailPager.swichListAndGrid(imgbtn_swich_list_grid);
                    }
                });
            } else {
                imgbtn_swich_list_grid.setVisibility(View.GONE);
            }
        }
    }
}
