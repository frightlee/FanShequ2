package com.fanhong.cn.myviews

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView

import com.fanhong.cn.R
import com.zhy.autolayout.utils.AutoUtils
import org.xutils.view.annotation.ViewInject
import org.xutils.x


/**
 * create PopupWindow  just like spinner ListView
 *
 * @param <T>
 * @param <T>
 * @author xuqiang
 * @create time 2018-1-29
</T></T> */
class SpinerPopWindow<T>(context: Context, private val list: List<T>, clickListener: OnItemClickListener, per: String) : PopupWindow(context) {
    private var inflater: LayoutInflater? = null
    private var mListView: ListView? = null
    private var mAdapter: MyAdapter? = null
    private var per = ""

    init {
        inflater = LayoutInflater.from(context)
        init(clickListener)
        this.per = per
    }

    private fun init(clickListener: OnItemClickListener) {
        val view = inflater!!.inflate(R.layout.spiner_window_layout, null)
        contentView = view
        width = LayoutParams.WRAP_CONTENT
        height = LayoutParams.WRAP_CONTENT
        isFocusable = true
        val dw = ColorDrawable(0x00)
        setBackgroundDrawable(dw)
        mListView = view.findViewById(R.id.lv_spinner_window)
        mListView!!.adapter = MyAdapter()
        mListView!!.onItemClickListener = clickListener
    }

    fun notifyDataChanged() {
        mAdapter!!.notifyDataSetChanged()
    }

    private inner class MyAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Any {
            return list[position]!!
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view: View
            var holder: ViewHolder
            if (convertView == null) {
                view = inflater!!.inflate(R.layout.spiner_item_layout, null)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.tvName!!.text = getItem(position).toString() + per
            return view
        }
    }
    private class ViewHolder(view: View) {
        @ViewInject(R.id.tv_item_text)
        internal var tvName: TextView? = null

        init {
            x.view().inject(this, view)
            AutoUtils.autoSize(view)
        }
    }
}
