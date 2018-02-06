package com.fanhong.cn.tools

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import com.fanhong.cn.home_page.ChooseCellActivity
import com.fanhong.cn.login_pages.LoginActivity

/**
 * Created by Administrator on 2018/1/31.
 */
object DialogUtil {
    fun showDialog(i:Int,activity: Activity){
        val builder = AlertDialog.Builder(activity)
        when(i){
            0->{  //未登录
                builder.setTitle("你还未登录")
                        .setMessage("是否立即登录？")
                        .setPositiveButton("确定"){
                            dialog, which ->
                            activity.startActivityForResult(Intent(activity,LoginActivity::class.java),100)}
                        .setNegativeButton("取消",null)
                        .show()
            }
            1->{//未选择小区
                builder.setTitle("你还未选择小区")
                        .setMessage("是否立即去选择小区？")
                        .setPositiveButton("确定"){
                            dialog, which ->
                            activity.startActivityForResult(Intent(activity,ChooseCellActivity::class.java),110)}
                        .setNegativeButton("取消",null)
                        .show()
            }
        }
    }
}