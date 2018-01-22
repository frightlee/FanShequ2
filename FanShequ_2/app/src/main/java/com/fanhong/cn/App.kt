package com.fanhong.cn

import android.app.Application

import org.xutils.x

/**
 * Created by Administrator on 2017/12/26.
 */

class App : Application() {
    companion object {
        val PREFERENCES_NAME = "mSettings"  //全局缓存统一名称
        val CMD = "http://m.wuyebest.com/index.php/App/index"//数据接口统一访问路径
        val UPDATE_CHECK = "http://m.wuyebest.com/index.php/App/index/appnumber"//更新检查访问路径
        val APP_DOWNLOAD = "http://m.wuyebest.com/public/apk/FanShequ.apk"//app下载路径

        var lastCodeMsgTime = 0L
    }

    /**
     *全局缓存数据键集
     */
    object PrefNames {
        val UPDATEIGNORE = "checkDate"//忽略更新日期
        val FIRST_START = "first_start"  //首次登录检测
        val USERNAME = "username"  //用户名
        val NICKNAME = "nickname"//用户昵称
        val USERID = "userId"  //用户Id
        val HEADIMG = "headImg"//用户头像地址
        val TOKEN = "token"//融云Token
        val ISNOTIFY = "notifyMsg"//是否开启通知
    }

    override fun onCreate() {
        super.onCreate()
        x.Ext.init(this)
    }
}
