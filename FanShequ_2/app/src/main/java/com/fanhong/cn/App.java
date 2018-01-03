package com.fanhong.cn;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/12/26.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
