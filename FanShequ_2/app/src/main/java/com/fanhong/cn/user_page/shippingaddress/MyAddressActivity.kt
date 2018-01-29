package com.fanhong.cn.user_page.shippingaddress

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fanhong.cn.App
import com.fanhong.cn.R
import kotlinx.android.synthetic.main.activity_my_address.*
import kotlinx.android.synthetic.main.activity_top.*
import org.json.JSONException
import org.json.JSONObject
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x

class MyAddressActivity : AppCompatActivity() {

    private var status: Int = 0
    private var mSharedPref: SharedPreferences? = null

    private var list: MutableList<AddressModel>? = ArrayList()
    private var adapter: AddressAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_address)
        mSharedPref = this.getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        initViews()
    }


    private fun initData() {
        list!!.clear()
        var params = RequestParams(App.CMD)
        params.addBodyParameter("cmd", "57")
        params.addBodyParameter("uid", mSharedPref!!.getString(App.PrefNames.USERID, "-1"))
        x.http().post(params, object : Callback.CommonCallback<String> {
            override fun onFinished() {
            }

            override fun onSuccess(result: String?) {
                try {
                    var array = JSONObject(result).getJSONArray("data")
                    (0 until array.length())
                            .map { array.getJSONObject(it) }
                            .forEach {
                                var model = AddressModel()
                                model.adrid = it.optInt("id")
                                model.cellId = it.optString("xid")
                                model.cellName = it.optString("xqname")
                                model.louId = it.optString("ldh")
                                model.louName = it.optString("ldname")
                                model.content = it.optString("dizhi")
                                model.address = it.optString("shdz")
                                model.name = it.optString("name")
                                model.phone = it.optString("dh")
                                model.isDefault = it.optInt("mr")
                                list!!.add(model)
                            }
                    adapter!!.notifyDataSetChanged()
                } catch (e: JSONException) {
                }
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
            }

        })
    }

    private fun initViews() {
        img_back.setOnClickListener { finish() }
        tv_title.setText(R.string.address)
        top_extra.visibility = View.VISIBLE
        top_extra.setText(R.string.control)
        top_extra.setOnClickListener {
            when (status) {
                0 -> {
                    top_extra.setText(R.string.finish)
                    adapter!!.controlable = true
                    adapter!!.notifyDataSetChanged()
                    status = 1
                }
                else -> {
                    top_extra.setText(R.string.control)
                    adapter!!.controlable = false
                    adapter!!.notifyDataSetChanged()
                    status = 0
                }
            }
        }
        add_new_address.setOnClickListener {
            startActivity(Intent(this@MyAddressActivity, AddAddressActivity::class.java))
        }
        adapter = AddressAdapter(this,list!!)
        address_list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    class AddressModel {
        internal var name: String? = null
        internal var phone: String? = null
        internal var address: String? = null
        internal var cellName: String? = null
        internal var louName: String? = null
        internal var content: String? = null
        internal var cellId: String? = null
        internal var louId: String? = null
        internal var isDefault: Int = 0
        internal var adrid: Int = 0
        override fun toString(): String {
            return "AddressModel(name=$name, phone=$phone, address=$address, cellName=$cellName, louName=$louName, content=$content, cellId=$cellId, louId=$louId, isDefault=$isDefault, adrid=$adrid)"
        }
    }
}
