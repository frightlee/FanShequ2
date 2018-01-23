package com.fanhong.cn.user_page

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fanhong.cn.App
import com.fanhong.cn.HomeActivity
import com.fanhong.cn.R
import com.fanhong.cn.login_pages.LoginActivity
import com.fanhong.cn.tools.JsonSyncUtils
import com.fanhong.cn.tools.MD5Util
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_reset_pwd.*
import kotlinx.android.synthetic.main.activity_top.*
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x

class ResetPwdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pwd)
        tv_title.text = getString(R.string.resetPwd)
        img_back.setOnClickListener { finish() }

        btn_confirmReset.setOnClickListener {
            val currentPwd = edt_currentPwd.text.toString().trim()
            val pref = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
            val savedPwd = pref.getString(App.PrefNames.PASSOWRD, "")
            if (currentPwd != savedPwd) {
                ToastUtil.showToast("初始密码错误！")
                return@setOnClickListener
            }
            val newPwd = edt_newPwd.text.toString().trim()
            if (savedPwd == newPwd) {
                ToastUtil.showToast("新密码与初始密码相同，无需设置！")
                return@setOnClickListener
            }
            val confirmPwd = edt_confirmPwd.text.toString().trim()
            if (newPwd != confirmPwd) {
                ToastUtil.showToast("新设密码与确认密码不同")
                return@setOnClickListener
            }
            val name = pref.getString(App.PrefNames.USERNAME, "")
            val finalPwd = MD5Util.getEncryptString(newPwd)
//            Log.e("testLog","savedPwd:$savedPwd\ncurrentPwd:$currentPwd\nnewPwd:$newPwd\nconfirmPwd:$confirmPwd\nfinalPwd:$finalPwd")
            val param = RequestParams(App.CMD)
            param.addBodyParameter("cmd", "9")
            param.addBodyParameter("name", name)
            param.addBodyParameter("password", finalPwd)
            x.http().post(param, object : Callback.CommonCallback<String> {
                override fun onSuccess(result: String) {
                    val cw = JsonSyncUtils.getJsonValue(result, "cw")
                    if ("0" == cw) {
                        ToastUtil.showToast("修改成功！请重新登录")

                        val editor = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                        editor.putInt(App.PrefNames.USERID, -1)
                        editor.putString(App.PrefNames.USERNAME, null)
                        editor.putString(App.PrefNames.TOKEN, null)
                        editor.putString(App.PrefNames.NICKNAME, null)
                        editor.putString(App.PrefNames.HEADIMG, null)
                        editor.commit()

                        val intent = Intent(this@ResetPwdActivity, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        val intent1 = Intent(applicationContext, LoginActivity::class.java)
                        startActivityForResult(intent1, HomeActivity.ACTION_LOGIN)
                    } else if ("1" == cw) ToastUtil.showToast("用户未找到！")
                    else ToastUtil.showToast("系统错误！")
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
}
