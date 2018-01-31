package com.fanhong.cn.user_page


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fanhong.cn.App
import com.fanhong.cn.R
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.fragment_user.*
import org.xutils.image.ImageOptions
import org.xutils.x


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

    override fun onResume() {
        super.onResume()
        refreshUser()
    }

    private val listener = View.OnClickListener { v ->
        when (v.id) {
            R.id.account_setting -> {//账号设置
                if (isLogged()) {
                    val intent = Intent(activity, AccountSetsActivity::class.java)
                    startActivity(intent)
                }else ToastUtil.showToast("请登录！")
            }
            R.id.news_notice -> {//消息通知
                    val intent = Intent(activity, MessagesActivity::class.java)
                    startActivity(intent)
            }
            R.id.my_order -> {//我的订单
                if (isLogged()) {
                    val intent = Intent(activity, OrderListActivity::class.java)
                    startActivity(intent)
                }else ToastUtil.showToast("请登录！")
            }
            R.id.customer_hotline -> {//客服热线
                val phoneNumber = tv_hotline.text.toString().trim()
                //判断Android版本是否大于23
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)

                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE),
                                11)
                        return@OnClickListener
                    }
                }
                callDialog(phoneNumber)
            }
            R.id.general_setup -> {//通用设置
                startActivity(Intent(activity, BasicSettingsActivity::class.java))
            }
            R.id.about_us -> {//关于我们
                startActivity(Intent(activity, AboutActivity::class.java))
            }
        }
    }

    private fun isLogged(): Boolean  {
        val pref = activity.getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        return pref.getInt(App.PrefNames.USERID, -1) != -1
    }

    private fun callDialog(phoneNumber: String) {
        AlertDialog.Builder(activity).setTitle("拨打电话")
                .setMessage(phoneNumber + "\n是否立即拨打？")
                .setPositiveButton("确认", DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent(Intent.ACTION_CALL)
                    val data = Uri.parse("tel:" + phoneNumber)
                    intent.data = data
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                .setNegativeButton("取消", null)
                .create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val phoneNumber = tv_hotline.text.toString().trim()
                callDialog(phoneNumber)
            } else
                ToastUtil.showToast("需要通话权限！")
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

    fun refreshUser() {
        val pref = activity.getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val userId = pref.getInt(App.PrefNames.USERID, -1)
        if (userId != -1) {
            mine_photo.isEnabled = false
            user_name.isEnabled = false
        } else {
            mine_photo.isEnabled = true
            user_name.isEnabled = true
        }
        val headImg = pref.getString(App.PrefNames.HEADIMG, "")
        var nickName = pref.getString(App.PrefNames.NICKNAME, "")
        val option = ImageOptions.Builder().setCircular(true)
                .setLoadingDrawableId(R.mipmap.mine_photo)
                .setFailureDrawableId(R.mipmap.mine_photo)
                .setUseMemCache(true).build()
        x.image().bind(mine_photo, headImg, option)
        if (null == nickName || nickName == "")
            nickName = pref.getString(App.PrefNames.USERNAME, getString(R.string.keylogin))
        user_name.text = nickName
    }

}
