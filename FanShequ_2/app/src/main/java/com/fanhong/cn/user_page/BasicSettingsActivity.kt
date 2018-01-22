package com.fanhong.cn.user_page

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.fanhong.cn.App
import com.fanhong.cn.R
import com.fanhong.cn.tools.AppCacheManager
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_basic_settings.*
import kotlinx.android.synthetic.main.activity_top.*
import org.xutils.x
import java.util.zip.Inflater

class BasicSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_settings)

        tv_title.text = "通用设置"
        img_back.setOnClickListener {
            setResult(-1)
            finish()
        }
        calcCacheSize()
        checkbox_notifyMsg.setOnCheckedChangeListener { _, isChecked ->
            val editor = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
            editor.putBoolean(App.PrefNames.ISNOTIFY, isChecked)
            editor.apply()
        }
    }

    private fun calcCacheSize() {
        var cacheSize = "0KB"
        try {
            cacheSize = AppCacheManager.getTotalCacheSize()
        } catch (e: Exception) {
        }
        tv_existCache.text = cacheSize
    }

    fun onClearCache(v: View) {
        AlertDialog.Builder(this).setMessage("是否清除缓存？")
                .setPositiveButton("确定", { _, _ ->
                    Thread {
                        val msg = try {
                            AppCacheManager.clearAllCache()
                            x.image().clearMemCache()
                            x.image().clearCacheFiles()
                            1
                        } catch (e: Exception) {
                            0
                        }
                        runOnUiThread {
                            calcCacheSize()
                            if (msg == 1)
                                ToastUtil.showToast("清除成功！")
                            else
                                ToastUtil.showToast("清除失败！")
                        }
                    }.start()

                })
                .setNegativeButton("取消", null).show()
    }

    fun onLogout(v: View) {
        val pref = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val id = pref.getInt(App.PrefNames.USERID, -1)
        if (id == -1)
            ToastUtil.showToast("当前未处于登录状态！")
        else AlertDialog.Builder(this).setMessage("是否确定退出此账号？")
                .setPositiveButton("确定", { _, _ -> doLogout() })
                .setNegativeButton("取消", null).show()

    }

    @SuppressLint("ApplySharedPref")
    private fun doLogout() {
        val editor = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        editor.putInt(App.PrefNames.USERID, -1)
        editor.putString(App.PrefNames.USERNAME, null)
        editor.putString(App.PrefNames.TOKEN, null)
        editor.putString(App.PrefNames.NICKNAME, null)
        editor.putString(App.PrefNames.HEADIMG, null)
        editor.commit()
        setResult(11)
        finish()
    }
}
