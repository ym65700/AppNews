package com.sample.appnews.utils;

/**
 * 作者：Administrator on 2017/6/2 23:07
 * 作用：常量类 配置联网请求地址
 */

public class Constants {
    /**
     * 联网请求的ip和端口
     */
    public static final String BASE_URL = "http://192.168.1.4:8080/web_home";
    /**
     * 新闻中心的网络地址
     */
    public static final String NEWSCENTER_PAGER_URL = BASE_URL + "/static/api/news/categories.json";
    /**
     * 商品热卖
     */
    public static final String WARES_HOT_URL = "http://112.124.22.238:8081/course_api/wares/hot?pageSize=";
}
