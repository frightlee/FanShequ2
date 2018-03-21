package com.fanhong.cn.door_page

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import cn.finalteam.galleryfinal.FunctionConfig
import cn.finalteam.galleryfinal.GalleryFinal
import cn.finalteam.galleryfinal.model.PhotoInfo
import com.fanhong.cn.App
import com.fanhong.cn.R
import com.fanhong.cn.myviews.SpinerPopWindow
import com.fanhong.cn.tools.FileUtil
import com.fanhong.cn.tools.JsonSyncUtils
import com.fanhong.cn.tools.ToastUtil
import kotlinx.android.synthetic.main.activity_add_key.*
import kotlinx.android.synthetic.main.activity_top.*
import kotlinx.android.synthetic.main.agree_sheets.*
import org.xutils.common.Callback
import org.xutils.common.util.KeyValue
import org.xutils.http.RequestParams
import org.xutils.http.body.MultipartBody
import org.xutils.x
import java.io.File

class AddKeyActivity : AppCompatActivity() {
    var cellId: String? = null
    var lastCellId:String? = null
    var cellIdList: MutableList<String>? = ArrayList()
    var cellList: MutableList<String>? = ArrayList()
    var buildingId: String? = null
    var buildingIdList: MutableList<String>? = ArrayList()
    var buildingList: MutableList<String>? = ArrayList()

    private var selectedPaths: MutableList<String> = ArrayList()
    private var selectedFiles: MutableList<File> = ArrayList()

    var mSharedPref: SharedPreferences? = null
    var uid: String? = null
    var ssp: SpinerPopWindow<String>? = null

    private val REQUEST_PERMISSION = 21
    private val START_GALLERYFINAL = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_key)
        mSharedPref = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE)
        uid = mSharedPref?.getString(App.PrefNames.USERID, "-1")
        var gardenName = mSharedPref!!.getString(App.PrefNames.GARDENNAME,"")
        if(!gardenName.isEmpty()){
            key_choosegarden.text = gardenName
            cellId = mSharedPref!!.getString(App.PrefNames.GARDENID,"")
            lastCellId = cellId
            key_choosebuilding.isEnabled = true
        }
        initViews()
    }

    private fun initViews() {
        img_back.setOnClickListener {
            goBack()
        }
        tv_title.text = getString(R.string.applykey)
        key_choosegarden.setOnClickListener {
            var params = RequestParams(App.CMD)
            params.addBodyParameter("cmd", "29")
            x.http().post(params, object : Callback.CommonCallback<String> {
                override fun onFinished() {
                }

                override fun onSuccess(result: String) {
                    if (JsonSyncUtils.getJsonValue(result, "cw") == "0") {
                        var data = JsonSyncUtils.getJsonValue(result, "data")
                        cellList = JsonSyncUtils.getStringList(data, "name")
                        cellIdList = JsonSyncUtils.getStringList(data,"id")

                        ssp = SpinerPopWindow(this@AddKeyActivity, cellList!!, AdapterView.OnItemClickListener { parent, view, position, id ->
                            key_choosegarden.text = cellList!![position]
                            cellId = cellIdList!![position]
                            if(lastCellId != cellId){
                                key_choosebuilding.text = "选择楼栋"
                                buildingId = ""
                                lastCellId = cellId
                            }
                            ssp!!.dismiss()
                        }, "")
                        ssp?.width = key_choosegarden.width
                        ssp?.showAsDropDown(key_choosegarden)
                    }
                }

                override fun onCancelled(cex: Callback.CancelledException?) {
                }

                override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                }

            })
        }
        key_choosebuilding.setOnClickListener {
            var params = RequestParams(App.CMD)
            params.addBodyParameter("cmd", "1001")
            params.addBodyParameter("xid",cellId)
            x.http().post(params, object : Callback.CommonCallback<String> {
                override fun onFinished() {
                }

                override fun onSuccess(result: String) {
                    if (JsonSyncUtils.getJsonValue(result!!, "state") == "200") {
                        var data = JsonSyncUtils.getJsonValue(result!!, "data")
                        buildingList = JsonSyncUtils.getStringList(data!!, "bname")
                        buildingIdList = JsonSyncUtils.getStringList(data!!, "id")

                        ssp = SpinerPopWindow(this@AddKeyActivity, buildingList!!, AdapterView.OnItemClickListener { parent, view, position, id ->
                            key_choosebuilding.text = buildingList!![position]
                            buildingId = buildingIdList!![position]
                            ssp!!.dismiss()
                        }, "")
                        ssp?.width = key_choosebuilding.width
                        ssp?.showAsDropDown(key_choosebuilding)
                    }
                }



                override fun onCancelled(cex: Callback.CancelledException?) {
                }

                override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                }

            })
        }
        img_key_add.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                val checkCamera = ContextCompat.checkSelfPermission(this@AddKeyActivity, Manifest.permission.CAMERA)
                val checkSD = ContextCompat.checkSelfPermission(this@AddKeyActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (checkCamera != PackageManager.PERMISSION_GRANTED || checkSD != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
                    return@setOnClickListener
                }
            }
            openPicture()
        }
        sheet_protocol.setOnClickListener{

        }
        /**
         * 上传数据
         */
        key_submit.setOnClickListener {
            if(TextUtils.isEmpty(cellId)){
                ToastUtil.showToastS("请选择小区")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(buildingId)){
                ToastUtil.showToastS("请选择楼栋")
                return@setOnClickListener
            }
            if(selectedFiles.size == 0){
                ToastUtil.showToastS("请至少上传一张人脸图片")
                return@setOnClickListener
            }
            if(!agree_sheet_protocol.isChecked){
                ToastUtil.showToastS("请阅读并同意用户协议")
                return@setOnClickListener
            }
            //压缩图片
            val kvList: MutableList<KeyValue> = ArrayList()
            kvList.add(KeyValue("cmd","1012"))
            kvList.add(KeyValue("uid",uid))
            kvList.add(KeyValue("xid",cellId))
            kvList.add(KeyValue("dizhi",buildingId))
            var filenames = ""
            for (i in 0 until selectedFiles.size) {
                val dir = File("${Environment.getExternalStorageDirectory()}/Fanshequ")
                if (!dir.exists())
                    dir.mkdir()
                val fileName = "${System.currentTimeMillis()}${(Math.random() * 99999).toInt()}_$i.jpg"
                val file: File = FileUtil.compressImage(selectedFiles[i], "${Environment.getExternalStorageDirectory()}/Fanshequ/$fileName")
                kvList.add(KeyValue("touxiang${i+1}", file))
                if (i != 0) filenames += ","
                filenames += fileName
            }
            kvList.add(KeyValue("xinxi", filenames))

            val params = RequestParams(App.CMD)
            params.requestBody = MultipartBody(kvList,"UTF-8")
            params.isMultipart = true
            x.http().post(params,object :Callback.CommonCallback<String>{
                override fun onSuccess(result: String) {
                    when(JsonSyncUtils.getState(result)){
                        200->{
                            AlertDialog.Builder(this@AddKeyActivity).setMessage("上传成功！").setPositiveButton("确定",null).show()
                            finish()
                        }
                        400->ToastUtil.showToastL("上传失败，请重试")
                    }
                }

                override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                    ToastUtil.showToastL("连接服务器失败，请检查网络！")
                }

                override fun onCancelled(cex: Callback.CancelledException?) {
                }

                override fun onFinished() {
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSION){
            openPicture()
        }
    }
    private fun openPicture() {
//        val selected: ArrayList<String> = selectedPaths as ArrayList<String>
//        selectedPaths to selected
        val funCfg = FunctionConfig.Builder()
                .setMutiSelectMaxSize(3/* - chosenSize*/)
                .setSelected(selectedPaths as ArrayList<String>)
                .setEnableCamera(true)
                .build()
        GalleryFinal.openGalleryMuti(START_GALLERYFINAL, funCfg, object : GalleryFinal.OnHanlderResultCallback {
            override fun onHanlderSuccess(reqeustCode: Int, resultList: MutableList<PhotoInfo>?) {
                if (reqeustCode == START_GALLERYFINAL) {
                    if (null != resultList) {
                        selectedPaths.clear()
                        selectedFiles.clear()
                        for (i in 0 until resultList.size) {
                            val picPath = resultList[i].photoPath
                            val picId = resultList[i].photoId
//                            Log.e("testLog", "photoId = $picId \nphotoPath = $picPath")
                            selectedPaths.add(picPath)
                            val file = File(picPath)
                            selectedFiles.add(file)
                            when (i) {
                                0 -> {
                                    img_key1.setImageURI(Uri.fromFile(file))
                                    img_key1.visibility = View.VISIBLE
                                }
                                1 -> {
                                    img_key2.setImageURI(Uri.fromFile(file))
                                    img_key2.visibility = View.VISIBLE
                                }
                                2 -> {
                                    img_key3.setImageURI(Uri.fromFile(file))
                                    img_key3.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }

            override fun onHanlderFailure(requestCode: Int, errorMsg: String?) {
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private var time1 = 0L
    private fun goBack() {
        var time2 = System.currentTimeMillis()
        if (time2 - time1 > 2000) {
            ToastUtil.showToastS("将会失去所有数据，再按一次返回")
            time1 = time2
        } else {
            finish()
        }
    }
}
