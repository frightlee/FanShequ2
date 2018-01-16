package com.fanhong.cn.tools

import android.widget.Toast
import org.xutils.x

/**
 * Created by Administrator on 2018/1/16.
 */
object ToastUtil{
    fun showToast(s:String){
        Toast.makeText(x.app(),s,Toast.LENGTH_LONG).show()
    }
}