package com.sample.appnews;

import android.app.Application;

import org.xutils.x;

/**
 * 作者：Administrator on 2017/6/1 16:46
 * 作用：代表整个软件  在manifest里配置
 */

public class AppNewsApplication extends Application {
    /**
     * 所有组建被创建之前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化utils
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.

    }
}
