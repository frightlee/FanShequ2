package com.fanhong.cn.user_page

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fanhong.cn.App
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_account_sets.*
import kotlinx.android.synthetic.main.activity_top.*
import org.xutils.image.ImageOptions
import org.xutils.x

class AccountSetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_sets)
        tv_title.text = getString(R.string.usersettings)
        img_back.setOnClickListener { finish() }

        initViews()
    }

    private fun initViews() {
        val pref = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val headUrl = pref.getString(App.PrefNames.HEADIMG, "")
        val number = pref.getString(App.PrefNames.USERNAME, "")
        val option = ImageOptions.Builder().setCircular(true)
                .setLoadingDrawableId(R.mipmap.ico_tx)
                .setFailureDrawableId(R.mipmap.ico_tx)
                .setUseMemCache(true).build()
        x.image().bind(img_head, headUrl, option)
        tv_phone.text = number
    }

    fun onHeadImg(v: View) {}
    fun onNickName(v: View) {}
    fun onAddress(v: View) {}
    fun onResetPwd(v: View) {
        startActivity(Intent(this, ResetPwdActivity::class.java))
    }
}
