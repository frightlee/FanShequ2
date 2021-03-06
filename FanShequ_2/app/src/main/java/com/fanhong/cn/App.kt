package com.fanhong.cn

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import cn.finalteam.galleryfinal.CoreConfig
import cn.finalteam.galleryfinal.FunctionConfig
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.ThemeConfig
import com.fanhong.cn.tools.XImageLoader
import io.rong.imlib.RongIMClient
import org.xutils.DbManager
import org.xutils.x
import java.io.File
import java.util.*


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

val  sqpath = /*Environment.getExternalStorageDirectory().path+*/"/data/data/com.fanhong.cn/database"
//        Log.e("TestLog",sqpath)
        var daoConfig: DbManager.DaoConfig = DbManager.DaoConfig()
                .setDbName(DB_NAME)
                .setDbDir(File(sqpath))
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
        val SHAREDRAFT = "gov_share_draft" //党功能分享草稿
    }

    /**
     * 支付相关参数
     */
    object PayConfig {
        /**
         * AliPay支付宝
         */
        //APPID
        val alipay_APPID = "2017082508372012"
        //商户收款账号
        val alipay_SELLER = "18725732573@139.com"
        // 支付宝公钥
        val alipay_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApdHlve2U3JjPDeXv+30PkA5pCkwtdPOodAHF6qCXwcyeS7/BVtx9GVbZhdI+inOY2oJI4ll3METeGmeGw99962V7YkAJu7+r9SVpDdoXz1jo8zATq/vVi7mCRSxhsfPmJ3YZfZUSWOf/ECfrkh6t+LROvBIa8VHhyaoLp5/zbCyFhFdfyk4/EWee+McNxtnehVlMknvjm6rCQ1A2Eyy+NyryA/nShclJL6wr5l/N4tSaH5dsSBHexoGXswE+5JQ6J+GQ/hNpiU4bUWLVDRd5OsnqYsS4xSqmVVwGG4Ts3xO1/skNORAFQ7DYMti1U8uxu1z5tPUljqbpYCWB3N8BOQIDAQAB"
        // 支付宝私钥
        var alipay_RSA_PRIVATE = ""
        //后台回调通知地址(商城)
        val alipay_SERVICE_CALLBACK = "http://m.wuyebest.com/library/zhifubao/notify_url.php"
        //后台回调通知地址(车审)
        val alipay_SERVICE_CALLBACK1 = "http://m.wuyebest.com/library/zhifubao/notify.php"

        /**
         * 微信
         */
        //appid 微信分配的app应用ID
        val WX_APPID = "wxea49e10e35c4b1ea"
        //商户号——微信分配的公众账号ID
        val WX_MCH_ID = "1488497082"
        //支付回调广播
        val WX_ACTION_RESULT = "com.fanhong.cn.wxapi.PAY_RESULT"
        //服务器回调接口
        val WX_notifyUrl = ""// 用于微信支付成功的回调（后台已设置）
        //商城下单签名接口
        val WX_getOrderUrl = "http://m.wuyebest.com/public/newWeiPay/index.php"
        //车审下单签名接口
        val WX_getOrderUrl1 = "http://m.wuyebest.com/public/WeiPay/index.php"
    }

    override fun onCreate() {
        super.onCreate()
        x.Ext.init(this)

        val theme = ThemeConfig.Builder()
                .setTitleBarBgColor(ContextCompat.getColor(this, R.color.skyblue))
                .setCheckSelectedColor(ContextCompat.getColor(this, R.color.skyblue))
                .setCropControlColor(ContextCompat.getColor(this, R.color.skyblue))
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
