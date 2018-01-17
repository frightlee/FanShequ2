package com.fanhong.cn.login_pages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_top.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_title.text = "用户登录"
        img_back.setOnClickListener { finish() }
    }

    fun onLogin(v: View) {

    }

    fun onRegister(v: View) {
        val i = Intent(this, RegisterActivity::class.java)
        i.putExtra("type","register")
        startActivityForResult(i, 21)
    }

    fun onResetPwd(v: View) {
        val i = Intent(this, RegisterActivity::class.java)
        i.putExtra("type","reset")
        startActivityForResult(i, 21)
    }
}
