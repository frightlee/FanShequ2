package com.fanhong.cn;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/12/26.
 */

public class App extends Application {
    public static final String PREFERENCES_NAME = "mSettings";//全局缓存统一名称
    public static final String FIRST_START = "first_start";//首次登录检测

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
