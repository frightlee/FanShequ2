package com.fanhong.cn.service_page.party

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fanhong.cn.R

/**
 * Created by Administrator on 2018/2/28.
 */
class ForumFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_party_forum,container,false)
    }
}