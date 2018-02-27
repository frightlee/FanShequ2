package com.fanhong.cn.service_page.shop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_goods_details.*

class GoodsDetailsActivity : AppCompatActivity() {

    private var id = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_details)
        img_back.setOnClickListener { finish() }
        id = intent.getStringExtra("id")
        countBox.onCountChange { count, oldCount ->
            Log.e("TestLog", "count = $count , oldCount = $oldCount")
        }
    }
}
