package com.fanhong.cn.home_page


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_top.*


/**
 * A simple [Fragment] subclass.
 */
class ServiceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        img_back.visibility=View.GONE
        tv_title.text="服务"
        super.onViewCreated(view, savedInstanceState)
    }

}
