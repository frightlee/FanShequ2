package com.fanhong.cn.door_page


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.fanhong.cn.R
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_top.*


/**
 * A simple [Fragment] subclass.
 */
class DoorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_door, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        tv_title.text = "门禁钥匙"
        img_back.setImageResource(R.mipmap.refresh)
        img_back.setOnClickListener { refreshKeys() }
        top_extra.text = "添加"
        top_extra.visibility=View.VISIBLE
        top_extra.setOnClickListener { addKeys() }
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * 添加钥匙
     */
    private fun addKeys() {
        ToastUtil.showToast("refresh clicked")
    }

    /**
     * 刷新钥匙
     */
    private fun refreshKeys() {
        ToastUtil.showToast("add clicked")
    }
}
