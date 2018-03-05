package com.fanhong.cn

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import cn.finalteam.galleryfinal.CoreConfig
import cn.finalteam.galleryfinal.FunctionConfig
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.ThemeConfig
import com.fanhong.cn.tools.XImageLoader
import io.rong.imlib.RongIMClient

import org.xutils.x
import java.util.HashSet
import org.xutils.DbManager
import org.xutils.db.table.TableEntity
import java.io.File


/**
 * Created by Administrator on 2017/12/26.
 */

class App : Application() {
    companion object {
        val PREFERENCES_NAME = "mSettings"  //全局缓存统一名称
        val DB_NAME = "mSettings"  //全局数据库统一名称
        val WEB_SITE = "http://m.wuyebest.com"
        val CMD = "http://m.wuyebest.com/index.php/App/index"//数据接口统一访问路径
        val UPDATE_CHECK = "http://m.wuyebest.com/index.php/App/index/appnumber"//更新检查访问路径
        val APP_DOWNLOAD = "http://m.wuyebest.com/public/apk/FanShequ.apk"//app下载路径
        val HEAD_UPLOAD = "http://m.wuyebest.com/index.php/App/index/newupapp"//头像上传路径

        //开门禁所需访问的路径
        val OPEN_URL = "http://m.wuyebest.com/index.php/App/index/yjkm"
        //开门禁结果查询路径
        val CHECK_URL = "http://m.wuyebest.com/index.php/App/index/yjkmcx"

        var lastCodeMsgTime = 0L

        var old_msg_times: MutableSet<Long> = HashSet()


        var daoConfig: DbManager.DaoConfig = DbManager.DaoConfig()
                .setDbName(DB_NAME)
                .setDbDir(File(Environment.getExternalStorageDirectory().path))
                .setDbVersion(1)
                .setDbOpenListener { db ->
                    // 开启WAL, 对写入加速提升巨大
                    db.database.enableWriteAheadLogging()
                }
                .setDbUpgradeListener { _, _, _ -> }
                .setTableCreateListener { _, table -> Log.i("JAVA", "onTableCreated：" + table.name) }
                .setAllowTransaction(true)
        var db = x.getDb(daoConfig)
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

        val theme = ThemeConfig.Builder()
                .setTitleBarBgColor(ContextCompat.getColor(this,R.color.skyblue))
                .setCheckSelectedColor(ContextCompat.getColor(this,R.color.skyblue))
                .setCropControlColor(ContextCompat.getColor(this,R.color.skyblue))
                .setIconCamera(R.mipmap.camera)
                .build()
        val functionCfg = FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build()
        val imgLoader = XImageLoader()
        val coreCfg = CoreConfig.Builder(this, imgLoader, theme)
                .setFunctionConfig(functionCfg)
                .build()
        GalleryFinal.init(coreCfg)

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (applicationInfo.packageName == getCurProcessName(applicationContext) || "io.rong.push" == getCurProcessName(applicationContext)) {
            RongIMClient.init(this)
        }
    }

    private fun getCurProcessName(context: Context): String? {

        val pid = android.os.Process.myPid()

        val activityManager = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (appProcess in activityManager
                .runningAppProcesses) {

            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }


}
