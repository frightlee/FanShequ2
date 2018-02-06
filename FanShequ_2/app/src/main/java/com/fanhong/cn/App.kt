package com.fanhong.cn

import android.app.Application
import android.content.SharedPreferences
import cn.finalteam.galleryfinal.CoreConfig
import cn.finalteam.galleryfinal.FunctionConfig
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.ThemeConfig
import com.fanhong.cn.tools.XImageLoader

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
        val HEAD_UPLOAD = "http://m.wuyebest.com/index.php/App/index/newupapp"//头像上传路径

        var lastCodeMsgTime = 0L

    }

    /**
     *全局缓存数据键集
     */
    object PrefNames {

        val UPDATEIGNORE = "checkDate"//忽略更新日期 String
        val FIRST_START = "first_start"  //首次登录检测 Boolean
        val USERNAME = "Name"  //用户名 String
        val PASSOWRD = "Password"  //密码（加密前）String
        val NICKNAME = "Nick"//用户昵称 String
        val USERID = "userId"  //用户Id String
        val HEADIMG = "Logo"//用户头像地址 String
        val TOKEN = "token"//融云Token String
        val ISNOTIFY = "notifyMsg"//是否开启通知 Boolean
        val GARDENNAME = "gardenName" //小区名字 String
        val GARDENID = "gardenId" //小区id  String

        val LASTYEAR = "fx_last_year" //招商代理查询缓存最后一个年份
        val LASTMONTH = "fx_last_month" //                      月份
    }

    override fun onCreate() {
        super.onCreate()
        x.Ext.init(this)

        val theme=ThemeConfig.Builder()
                .setTitleBarBgColor(resources.getColor(R.color.skyblue))
                .setCheckSelectedColor(resources.getColor(R.color.skyblue))
                .setCropControlColor(resources.getColor(R.color.skyblue))
                .setIconCamera(R.mipmap.camera)
                .build()
        val functionCfg=FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build()
        val imgLoader=XImageLoader()
        val coreCfg=CoreConfig.Builder(this,imgLoader,theme)
                .setFunctionConfig(functionCfg)
                .build()
        GalleryFinal.init(coreCfg)
    }
}
