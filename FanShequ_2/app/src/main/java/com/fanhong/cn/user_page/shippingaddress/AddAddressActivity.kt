package com.fanhong.cn.user_page.shippingaddress

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_top.*

class AddAddressActivity : AppCompatActivity() {

    var checked = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        initViews()
    }

    private fun initViews() {
        img_back.setOnClickListener { finish() }
        tv_title.setText(R.string.addaddress)
        top_extra.setText(R.string.save)
        top_extra.visibility = View.VISIBLE

        setEnableds(false,false,false)

        address_choosecell.setOnClickListener {

        }

        input_address_edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.trim().isEmpty()) {
                    setEnableds(true, true, true)
                } else {
                    setEnableds(true, true, false)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        whether_set_default.setOnCheckedChangeListener { buttonView, isChecked ->
            checked = if (isChecked)
                1
            else
                0
        }
    }
    fun setEnableds(louClickable:Boolean,inputable:Boolean,checkable:Boolean){
        address_chooselou.isEnabled = louClickable
        input_address_edt.isEnabled = inputable
        whether_set_default.isEnabled = checkable
    }
}
