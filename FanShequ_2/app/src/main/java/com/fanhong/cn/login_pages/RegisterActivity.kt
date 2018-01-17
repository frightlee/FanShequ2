package com.fanhong.cn.login_pages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fanhong.cn.R
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_top.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews(intent.getStringExtra("type"))
    }

    private fun initViews(type: String?) {
        when (type) {
            "register" -> {
                tv_title.text = "用户注册"
                btn_register_commit.text = "注册"
                checkbox_agree.setOnCheckedChangeListener { buttonView, isChecked ->
                    btn_register_commit.isEnabled = isChecked
                }
            }
            "reset" -> {
                tv_title.text = "重置密码"
                btn_register_commit.text = "确定"
                layout_agreement.visibility = View.GONE
            }
        }
        img_back.setOnClickListener {
            setResult(-1)
            finish()
        }

    }

    fun onReadAgreement(v: View) {
        startActivityForResult(Intent(this, AgreementSheetActivity::class.java), 22)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            22 -> {//Agree
                checkbox_agree.isChecked = true
            }
            23 -> {//Decline
                checkbox_agree.isChecked = false
            }
        }
    }
}
