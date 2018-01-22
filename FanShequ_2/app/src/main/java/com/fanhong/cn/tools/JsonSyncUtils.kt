package com.fanhong.cn.tools

import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray





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
     * @param data
     * @param key
     * @return
     */
    @Synchronized
    fun getStringList(data: String, key: String): List<String> {
        val list:MutableList<String> = ArrayList()
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
}