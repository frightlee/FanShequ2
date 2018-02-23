package com.fanhong.cn.home_page


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fanhong.cn.R
import com.fanhong.cn.service_page.MyServiceAdapter
import kotlinx.android.synthetic.main.activity_top.*
import kotlinx.android.synthetic.main.fragment_service.*


/**
 * A simple [Fragment] subclass.
 */
class ServiceFragment : Fragment() {
    companion object {
        //定义数组来存放按钮图片
        private val mImageViewArray1 = intArrayOf(R.drawable.service_store, R.drawable.service_es,
                R.drawable.service_dai, R.drawable.service_distribution,
                R.drawable.service_fix,
                R.drawable.service_dang_1)

        //定义数组文字
        private val mTextviewArray1 = intArrayOf(R.string.service_store, R.string.service_es,
                R.string.service_dai, R.string.service_zsdl,
                R.string.service_fix,
                R.string.service_dang)

        private val mImageViewArray2 = intArrayOf(R.drawable.service_mt, R.drawable.service_dz,
                R.drawable.service_tb, R.drawable.service_jds, R.drawable.service_wph,
                R.drawable.service_yms, R.drawable.service_xc, R.drawable.service_qne,
                R.drawable.service_tn, R.drawable.service_tc)

        private val mTextviewArray2 = intArrayOf(R.string.service_mt, R.string.service_dz,
                R.string.service_tb, R.string.service_jds, R.string.service_wph, R.string.service_yms,
                R.string.service_xc, R.string.service_qne, R.string.service_tn, R.string.service_tc)

        private val mUrlArray2 = intArrayOf(R.string.url_meituan, R.string.url_dianping, R.string.url_taobao,
                R.string.url_jingdong, R.string.url_weiping, R.string.url_yamaxun, R.string.url_xiecheng,
                R.string.url_quna, R.string.url_tuniu, R.string.url_tongcheng)

        private val mImageViewArray3 = intArrayOf(R.drawable.service_hj, R.drawable.service_funhos,
                R.drawable.service_gwy, R.drawable.service_zc, R.drawable.service_my)

        private val mTextviewArray3 = intArrayOf(R.string.service_hj, R.string.service_funhos,
                R.string.service_gwy, R.string.service_zc, R.string.service_my)

        private val mUrlArray3 = intArrayOf(R.string.url_huzhang, R.string.url_quyy,
                R.string.url_gongwuyuan, R.string.url_zhichen, R.string.url_muying)
    }

    private var adapter: MyServiceAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        img_back.visibility = View.GONE
        tv_title.text = "社区服务"
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initViews() {
        adapter = MyServiceAdapter(activity, mImageViewArray1, mTextviewArray1)
        adapter!!.setItemClick(object : MyServiceAdapter.ItemClick {
            override fun itemclick(position: Int) {
                //便民服务的点击事件

            }

        })
        recycle1.adapter = adapter
        recycle1.layoutManager = GridLayoutManager(activity, 4, OrientationHelper.VERTICAL, false)

        adapter = MyServiceAdapter(activity, mImageViewArray2, mTextviewArray2)
        adapter!!.setItemClick(object : MyServiceAdapter.ItemClick {
            override fun itemclick(position: Int) {
                //衣食住行的点击事件

            }

        })
        recycle2.adapter = adapter
        recycle2.layoutManager = GridLayoutManager(activity, 4)

        adapter = MyServiceAdapter(activity, mImageViewArray3, mTextviewArray3)
        adapter!!.setItemClick(object : MyServiceAdapter.ItemClick {
            override fun itemclick(position: Int) {
                //教育医疗的点击事件

            }

        })
        recycle3.adapter = adapter
        recycle3.layoutManager = GridLayoutManager(activity, 4)
    }
}
