package com.fanhong.cn

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_top.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_title.text = "用户登录"
        img_back.setOnClickListener {  finish() }
    }

    fun onLogin(v: View) {

    }

    fun onRegister(v: View) {
    }

    fun onResetPwd(v: View) {
    }
}
