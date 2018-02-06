package com.fanhong.cn.tools

import com.fanhong.cn.user_page.OrderDetailsActivity
import com.fanhong.cn.user_page.OrderListActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by Administrator on 2018/1/18.
 */
object JsonSyncUtils {
    /*
     *通用方法
     */

    /**
     *
     * @param jsonObject
     * @param key
     * @return
     */
    @Synchronized
    fun getJsonValue(jsonObject: String, key: String): String {
        var value = ""
        try {
            val obj = JSONObject(jsonObject)
            value = obj.getString(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return value
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    @Synchronized
    fun getState(jsonObject: String): Int {
        var value = ""
        try {
            val obj = JSONObject(jsonObject)
            value = obj.getString("state")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return value.toInt()
    }

    /**
     *
     * @param data
     * @param key
     * @return
     */
    @Synchronized
    fun getStringList(data: String, key: String): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        try {
            val jsonArray = JSONArray(data)
            (0 until jsonArray.length())
                    .map { jsonArray.optJSONObject(it) }
                    .mapTo(list) { it.optString(key) }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return list
    }

    fun getOrderList(data: String): MutableList<OrderListActivity.MyOrder> {
        val list: MutableList<OrderListActivity.MyOrder> = ArrayList()
        try {
            val jsonArray = JSONArray(data)
            (0 until jsonArray.length())
                    .map { jsonArray.optJSONObject(it) }
                    .mapTo(list) {
                        val id = it.optString("id")
                        val number = it.optString("ddh")
                        val time = it.optString("time")
                        val name = it.optString("goods")
                        val price = it.optString("zjje")
                        OrderListActivity.MyOrder(id, number, name, price, time)
                    }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return list
    }

    fun getOrderGoodsList(data: String, orderId: String): MutableList<OrderDetailsActivity.Goods> {
        val list: MutableList<OrderDetailsActivity.Goods> = ArrayList()
        try {
            val jsonArray = JSONArray(data)
            (0 until jsonArray.length())
                    .map { jsonArray.optJSONObject(it) }
                    .mapTo(list) {
                        val id = it.optString("id")
                        val number = it.optString("gsl")
                        val name = it.optString("name")
                        val isEvaluate = it.optString("pl") == "2"//1未评价，2已评价
                        OrderDetailsActivity.Goods(name, id, number, isEvaluate, orderId)
                    }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return list
    }
}