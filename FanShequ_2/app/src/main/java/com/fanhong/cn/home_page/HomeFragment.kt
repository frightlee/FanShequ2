package com.fanhong.cn.home_page


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.fanhong.cn.App
import com.fanhong.cn.R
import com.fanhong.cn.tools.DialogUtil
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var mSharedPref: SharedPreferences? = null
    private var array: JSONArray? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        mSharedPref = activity.getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        get_location.setOnClickListener {
            startActivityForResult(Intent(activity, ChooseCellActivity::class.java), 110)
        }
        getNotification()
        home_banner.setOnClickListener {
            startActivity(Intent(activity, BannerInActivity::class.java))
        }
        tv_shequ_gonggao.setOnClickListener {

        }
        tv_wuye_star.setOnClickListener {
            if (isLogined()) {
                if (choosedCell()){
                    var i = Intent(activity,StarManagerActivity::class.java)
                    i.putExtra("id",mSharedPref!!.getString(App.PrefNames.GARDENID,""))
                    i.putExtra("name",mSharedPref!!.getString(App.PrefNames.GARDENNAME,""))
                    startActivity(i)
                }else{
                    DialogUtil.showDialog(1,activity)
                }
            } else {
                DialogUtil.showDialog(0, activity)
            }
        }
    }

    private fun getNotification() {
        var params = RequestParams(App.CMD)
        params.addBodyParameter("cmd", "43")
        x.http().post(params, object : Callback.CommonCallback<String> {
            override fun onFinished() {
            }

            override fun onSuccess(result: String?) {
                try {
                    array = JSONObject(result).getJSONArray("data")
                    if (array!!.length() > 0) {
                        show_notify.text = array!![array!!.length() - 1].toString()
                        recycleShow()
                    }
                } catch (e: JSONException) {
                }
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
            }

        })
    }

    private fun recycleShow() {
        var alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.startOffset = 2000  //
        alphaAnimation.duration = 1000
        var i = 0
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                show_notify.text = array!![i].toString()
                i++
                if (i > array!!.length() - 1) {
                    i = 0
                }
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        alphaAnimation.repeatCount = Animation.INFINITE
        show_notify.startAnimation(alphaAnimation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            51 -> { //选择小区的回调
                val gardenName = data!!.getStringExtra("gardenName")
                val gardenId = data!!.getStringExtra("gardenId")
                show_cell_name.text = gardenName
                val editor = mSharedPref!!.edit()
                editor.putString(App.PrefNames.GARDENNAME, gardenName).apply()
                editor.putString(App.PrefNames.GARDENID, gardenId).apply()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        show_cell_name.text = mSharedPref!!.getString(App.PrefNames.GARDENNAME, "")
    }

    private fun isLogined(): Boolean {
        return mSharedPref!!.getString(App.PrefNames.USERID, "-1") != "-1"
    }

    private fun choosedCell(): Boolean {
        return !TextUtils.isEmpty(mSharedPref!!.getString(App.PrefNames.GARDENNAME, ""))
    }
}
