package com.fanhong.cn.door_page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.fanhong.cn.R
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_top.*

class AddKeyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_key)
        initViews()
    }

    private fun initViews() {
        img_back.setOnClickListener {
            goBack()
        }
        tv_title.text = getString(R.string.applykey)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private var time1 = 0L
    private fun goBack() {
        var time2 = System.currentTimeMillis()
        if (time2 - time1 > 2000) {
            ToastUtil.showToastS("将会失去所有数据，再按一次返回")
            time1 = time2
        } else {
            finish()
        }
    }
}
