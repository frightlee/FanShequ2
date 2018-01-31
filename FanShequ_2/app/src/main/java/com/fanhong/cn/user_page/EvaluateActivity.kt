package com.fanhong.cn.user_page

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_top.*

class EvaluateActivity : AppCompatActivity() {

    private var goodsId = ""
    private var goodsName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluate)
        tv_title.text = getString(R.string.evaluate)
        img_back.setOnClickListener { finish() }
        goodsId = intent.getStringExtra("goodsId")
        goodsName = intent.getStringExtra("goodsName")
    }
}
