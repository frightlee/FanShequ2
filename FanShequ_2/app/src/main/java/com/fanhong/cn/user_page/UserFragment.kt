package com.fanhong.cn.user_page


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.fragment_user.*


/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        addClickListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private val listener = View.OnClickListener { v ->
        when (v.id) {
            R.id.account_setting -> {//账号设置
            }
            R.id.news_notice -> {//消息通知
            }
            R.id.my_order -> {//我的订单
            }
            R.id.customer_hotline -> {//客服热线
            }
            R.id.general_setup -> {//通用设置
            }
            R.id.about_us -> {//关于我们
            }
        }
    }

    private fun addClickListeners() {
        account_setting.setOnClickListener(listener)
        news_notice.setOnClickListener(listener)
        my_order.setOnClickListener(listener)
        customer_hotline.setOnClickListener(listener)
        general_setup.setOnClickListener(listener)
        about_us.setOnClickListener(listener)
    }

}
