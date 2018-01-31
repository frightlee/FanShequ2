package com.fanhong.cn.user_page

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.fanhong.cn.App
import com.fanhong.cn.HomeActivity
import com.fanhong.cn.R
import com.fanhong.cn.login_pages.LoginActivity
import com.fanhong.cn.tools.JsonSyncUtils
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_nick_set.*
import kotlinx.android.synthetic.main.activity_top.*
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x

class NickSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_set)
        tv_title.text = "修改昵称"
        img_back.setOnClickListener { finish() }
    }

    fun onSaveNick(v: View) {
        val nick = edt_nickname.text.toString().trim()
        if (TextUtils.isEmpty(nick)) {
            ToastUtil.showToast("输入不能为空！")
            return
        }
        val pref = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val username = pref.getString(App.PrefNames.USERNAME, "")
        val param = RequestParams(App.CMD)
        param.addBodyParameter("cmd", "11")
        param.addBodyParameter("name", username)
        param.addBodyParameter("user", nick)
        x.http().post(param, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String) {
                val cw = JsonSyncUtils.getJsonValue(result, "cw")
                when (cw) {
                    "0" -> {
                        ToastUtil.showToast("修改成功！")
                        pref.edit().putString(App.PrefNames.NICKNAME, nick).apply()
                        finish()
                    }
                    "1" -> ToastUtil.showToast("用户未找到！")
                    "2" -> ToastUtil.showToast(JsonSyncUtils.getJsonValue(result, "msg"))
                    else -> ToastUtil.showToast("系统错误！")
                }
            }

            override fun onFinished() {
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                ToastUtil.showToast("访问服务器失败，请检查网络！")
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }
        })
    }

}
