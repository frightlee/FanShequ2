package com.fanhong.cn.home_page.fenxiao

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fanhong.cn.HomeActivity
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_post_success.*

class PostSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_success)
        tv_back_homepage.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }
}
