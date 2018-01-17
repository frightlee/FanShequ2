package com.fanhong.cn

import android.app.Application

import org.xutils.x

/**
 * Created by Administrator on 2017/12/26.
 */

class App : Application() {
    companion object {
        val PREFERENCES_NAME = "mSettings"  //全局缓存统一名称
        val FIRST_START = "first_start"  //首次登录检测
    }

    override fun onCreate() {
        super.onCreate()
        x.Ext.init(this)
    }
}
