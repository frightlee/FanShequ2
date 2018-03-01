package com.fanhong.cn.service_page.shop

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.fanhong.cn.App
import com.fanhong.cn.R
import com.fanhong.cn.tools.JsonSyncUtils
import com.zhy.autolayout.AutoRelativeLayout
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_goods_details.*
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.image.ImageOptions
import org.xutils.view.annotation.ViewInject
import org.xutils.x

class GoodsDetailsActivity : AppCompatActivity() {

    private var id = "-1"
    private val discusses: MutableList<DiscussInfo> = ArrayList()
    private var adapter: DiscussAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_details)
        img_back.setOnClickListener { finish() }
        id = intent.getStringExtra("id")
        countBox.onCountChange { count, oldCount ->
            Log.e("TestLog", "count = $count , oldCount = $oldCount")
        }

        getGoods()

        adapter = DiscussAdapter(this, discusses)
        lv_discuss.adapter = adapter

        tv_detail.setOnClickListener {
            tv_detail.setBackgroundResource(R.drawable.qianse)
            tv_detail.setTextColor(ContextCompat.getColor(this, R.color.lightblue))
            tv_discuss.setBackgroundColor(ContextCompat.getColor(this, R.color.transParent))
            tv_discuss.setTextColor(ContextCompat.getColor(this, R.color.white))

            iv_goodsDetails.visibility = View.VISIBLE
            lv_discuss.visibility = View.GONE
        }
        tv_discuss.setOnClickListener {
            tv_detail.setBackgroundColor(ContextCompat.getColor(this, R.color.transParent))
            tv_detail.setTextColor(ContextCompat.getColor(this, R.color.white))
            tv_discuss.setBackgroundResource(R.drawable.qianse)
            tv_discuss.setTextColor(ContextCompat.getColor(this, R.color.lightblue))

            iv_goodsDetails.visibility = View.GONE
            lv_discuss.visibility = View.VISIBLE
            refreshDiscuss(1)
        }
    }

    private fun refreshDiscuss(page: Int) {
        discusses.clear()
        val param = RequestParams(App.CMD)
        param.addBodyParameter("cmd", "1011")
        param.addBodyParameter("id", id)
        param.addBodyParameter("page", "1")
        x.http().post(param, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String) {
                when (JsonSyncUtils.getState(result)) {
                    200 -> {
                        val data = JsonSyncUtils.getJsonValue(result, "data")
                        Log.e("TestLog",result)
                        discusses += JsonSyncUtils.getDiscussList(data)
                    }
                    400 -> {
                    }
                }
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onFinished() {
                runOnUiThread { adapter?.notifyDataSetChanged() }
            }
        })
    }

    private fun getGoods() {
        val param = RequestParams(App.CMD)
        param.addBodyParameter("cmd", "1010")
        param.addBodyParameter("id", id)
        param.connectTimeout = 5000
        x.http().post(param, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String) {
                when (JsonSyncUtils.getState(result)) {
                    200 -> {
                        val option = ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default).setFailureDrawableId(R.mipmap.img_default).setUseMemCache(true).build()
                        val logoUrl = JsonSyncUtils.getJsonValue(result, "logo")
                        x.image().bind(iv_logo, logoUrl, option)
                        tv_goodsName.text = JsonSyncUtils.getJsonValue(result, "name")
                        tv_goodsDescription.text = JsonSyncUtils.getJsonValue(result, "describe")
                        tv_price.text = "￥${JsonSyncUtils.getJsonValue(result, "jg")}"
                        tv_unit.text = JsonSyncUtils.getJsonValue(result, "guige")
                        val detailsImg = JsonSyncUtils.getJsonValue(result, "tupian")
                        x.image().loadDrawable(detailsImg, option, object : Callback.CommonCallback<Drawable> {
                            override fun onSuccess(result: Drawable) {
                                val maxHeight = iv_goodsDetails.width * 10
                                val picHeight = (iv_goodsDetails.width.toFloat() / result.minimumWidth * result.minimumHeight).toInt()
                                val lp = iv_goodsDetails.layoutParams as AutoRelativeLayout.LayoutParams
                                lp.height = picHeight
                                iv_goodsDetails.layoutParams = lp
                                iv_goodsDetails.maxHeight = maxHeight
                                iv_goodsDetails.setImageDrawable(result)
                            }

                            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                            }

                            override fun onCancelled(cex: Callback.CancelledException?) {
                            }

                            override fun onFinished() {
                            }
                        })
                        btn_buyNow.isEnabled = true
                        tv_add_to_car.isEnabled = true
                    }
                    400 -> {
                    }
                }
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
            }

            override fun onFinished() {
                progressBar.visibility = View.GONE
            }
        })
    }

    data class DiscussInfo(val date: String, val name: String, val head: String, val content: String, val pic1: String, val pic2: String, val pic3: String)

    class DiscussAdapter(val context: Context, private val discusses: MutableList<DiscussInfo>) : BaseAdapter() {
        private val option = ImageOptions.Builder().setUseMemCache(true).setFailureDrawableId(R.mipmap.img_default).setFailureDrawableId(R.mipmap.img_default).build()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder
            val view: View =
                    if (null == convertView) {
                        val view = LayoutInflater.from(context).inflate(R.layout.item_discuss, parent, false)
                        holder = ViewHolder(view)
                        view.tag = holder
                        view
                    } else {
                        holder = convertView.tag as ViewHolder
                        convertView
                    }
            val data = discusses[position]
            x.image().bind(holder.head, data.head, ImageOptions.Builder().setCircular(true).setLoadingDrawableId(R.mipmap.head_default).setFailureDrawableId(R.mipmap.head_default).build())
            holder.name?.text = data.name
            holder.date?.text = data.date
            holder.content?.text = data.content
            val option = ImageOptions.Builder().setUseMemCache(true)
                    .setLoadingDrawableId(R.mipmap.img_default)
                    .setFailureDrawableId(R.mipmap.img_default)
                    .setIgnoreGif(false).build()
            if (data.pic1 == "")
                holder.evaPic1?.visibility = View.GONE
            else {
                x.image().bind(holder.evaPic1, data.pic1, option)
                holder.evaPic1?.visibility = View.VISIBLE
            }
            if (data.pic2 == "")
                holder.evaPic2?.visibility = View.GONE
            else {
                x.image().bind(holder.evaPic2, data.pic2, option)
                holder.evaPic2?.visibility = View.VISIBLE
            }
            if (data.pic3 == "")
                holder.evaPic3?.visibility = View.GONE
            else {
                x.image().bind(holder.evaPic3, data.pic3, option)
                holder.evaPic3?.visibility = View.VISIBLE
            }
            return view
        }

        override fun getItem(position: Int): Any {
            return discusses[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return discusses.size
        }

        class ViewHolder(view: View) {
            @ViewInject(R.id.iv_head)
            var head: ImageView? = null
            @ViewInject(R.id.iv_evaPic1)
            var evaPic1: ImageView? = null
            @ViewInject(R.id.iv_evaPic2)
            var evaPic2: ImageView? = null
            @ViewInject(R.id.iv_evaPic3)
            var evaPic3: ImageView? = null
            @ViewInject(R.id.tv_name)
            var name: TextView? = null
            @ViewInject(R.id.tv_date)
            var date: TextView? = null
            @ViewInject(R.id.tv_content)
            var content: TextView? = null

            init {
                x.view().inject(this, view)
                AutoUtils.autoSize(view)
            }
        }
    }
}
