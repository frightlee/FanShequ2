package com.fanhong.cn

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.TextView
import com.fanhong.cn.door_page.DoorFragment
import com.fanhong.cn.home_page.CommunityFragment
import com.fanhong.cn.home_page.HomeFragment
import com.fanhong.cn.home_page.ServiceFragment
import com.fanhong.cn.login_pages.LoginActivity
import com.fanhong.cn.tools.AppCacheManager
import com.fanhong.cn.tools.JsonSyncUtils
import com.fanhong.cn.tools.ToastUtil
import com.fanhong.cn.user_page.UserFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    companion object {
        val ACTION_LOGIN: Int = 21
    }

    private val fragments: MutableList<Fragment> = ArrayList()

    private var apkPath = ""
    private var apkName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()

        val today = SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())
        if (today != getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE).getString(App.PrefNames.UPDATEIGNORE, ""))
            checkUpdate()

    }

    private fun initViews() {
        fragments.add(HomeFragment())
        fragments.add(ServiceFragment())
        fragments.add(DoorFragment())
        fragments.add(CommunityFragment())
        fragments.add(UserFragment())
        val pagerAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }
        home_viewpager.adapter = pagerAdapter
        home_viewpager.offscreenPageLimit = 4 //设置向左和向右都缓存limit个页面
        home_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        tab_home.isChecked = true
                        setFloatIconsVisible(0)
                    }
                    1 -> {
                        tab_service.isChecked = true
                        setFloatIconsVisible(1)
                    }
                    2 -> {
                        tab_door.isChecked = true
                        setFloatIconsVisible(2)
                    }
                    3 -> {
                        tab_interaction.isChecked = true
                        setFloatIconsVisible(3)
                    }
                    4 -> {
                        tab_user.isChecked = true
                        setFloatIconsVisible(4)
                    }
                }
            }
        })
        rg_bottom.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.tab_home -> {
                    home_viewpager.currentItem = 0
                    setFloatIconsVisible(0)
                }
                R.id.tab_service -> {
                    home_viewpager.currentItem = 1
                    setFloatIconsVisible(1)
                }
                R.id.tab_door -> {
                    home_viewpager.currentItem = 2
                    setFloatIconsVisible(2)
                }
                R.id.tab_interaction -> {
                    home_viewpager.currentItem = 3
                    setFloatIconsVisible(3)
                }
                R.id.tab_user -> {
                    home_viewpager.currentItem = 4
                    setFloatIconsVisible(4)
                }
            }
        }

    }

    private fun setFloatIconsVisible(o: Int) {
        val icons: Array<ImageView> = arrayOf(img_tab_home, img_tab_service, img_tab_door, img_tab_interaction, img_tab_user)
        for (i in 0 until icons.size) {
            if (i == o)
                icons[i].visibility = View.VISIBLE
            else
                icons[i].visibility = View.GONE
        }
    }

    private fun checkUpdate() {
        val param = RequestParams(App.UPDATE_CHECK)
        param.addParameter("id", 1)
        x.http().post(param, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String) {
                val targetCode = JsonSyncUtils.getJsonValue(result, "number").toInt()
                if (targetCode > BuildConfig.VERSION_CODE) {
                    val targetName = JsonSyncUtils.getJsonValue(result, "bbname")
                    val targetIntroduce = JsonSyncUtils.getJsonValue(result, "gxsm")
                    val layout = LinearLayout(this@HomeActivity)
                    layout.orientation = LinearLayout.VERTICAL
                    layout.setPadding(50, 2, 2, 2)
//                    val scrollView = ScrollView(this@HomeActivity)
                    val tv1 = TextView(this@HomeActivity)
                    tv1.text = "更新说明："
                    tv1.textSize = 15.0f
                    tv1.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.text_3))
                    val tv2 = TextView(this@HomeActivity)
                    tv2.text = targetIntroduce
                    tv2.textSize = 15.0f
                    tv2.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.skyblue))
                    layout.addView(tv1)
                    layout.addView(tv2)
                    AlertDialog.Builder(this@HomeActivity)
                            .setTitle("发现新版本：v$targetName").setView(layout)
                            .setPositiveButton("立即更新", { _, _ ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    val checkCallPhonePermission = ContextCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                        apkName = targetName
                                        ActivityCompat.requestPermissions(this@HomeActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                                11)
                                        return@setPositiveButton
                                    }
                                }
                                startUpdating(targetName)
                            })
                            .setNegativeButton("暂不更新", { _, _ ->
                                val editor = getSharedPreferences(App.PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                                val date = SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())
                                editor.putString(App.PrefNames.UPDATEIGNORE, date)
                                editor.apply()
                            })
                            .show()
                }
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onFinished() {
            }

        })
    }

    private fun startUpdating(targetName: String) {
        val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifycation: Notification = Notification()
        val intent = Intent(Intent.ACTION_VIEW)
        notifycation.icon = R.mipmap.ic_launcher
        notifycation.tickerText = "更新通知"
        notifycation.contentView = RemoteViews(this@HomeActivity.packageName, R.layout.softupdate_progress)

        var p = 0
        val param = RequestParams(App.APP_DOWNLOAD)
        param.saveFilePath = getApkPath(targetName)
        x.http().get(param, object : Callback.ProgressCallback<File> {
            override fun onWaiting() {
            }

            override fun onStarted() {
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onLoading(total: Long, current: Long, isDownloading: Boolean) {
                if (isDownloading) {
                    val progress = AppCacheManager.getFormatSize(current.toDouble())
                    val max = AppCacheManager.getFormatSize(total.toDouble())
                    val percent = (current * 100 / total).toInt()
                    Log.e("TestLog", "$percent%($progress/$max)")
                    if (percent > p) {
                        notifycation.contentView.setTextViewText(R.id.content_view_text1, progress + "/$max")
                        notifycation.contentView.setProgressBar(R.id.content_view_progress, 100, p, false)
                        manager.notify(0, notifycation)
                        p = percent
                    }
                }
            }

            override fun onSuccess(result: File?) {
                notifycation.contentView.setTextViewText(R.id.content_view_text1, "下载完成")
                notifycation.contentView.setProgressBar(R.id.content_view_progress, 100, 100, false)
                manager.notify(0, notifycation)

                val i = Intent(Intent.ACTION_VIEW)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                i.setDataAndType(Uri.fromFile(File(apkPath)), "application/vnd.android.package-archive")
                startActivity(i)
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                Log.e("TestLog", "onError" + ex.toString())
            }

            override fun onFinished() {
                manager.cancel(0)
            }
        })
    }

    private fun getApkPath(code: String): String {
        val basePath = Environment.getExternalStorageDirectory().path
        val file = File(basePath + "/FanShequ")
        if (!file.exists())
            file.mkdir()
        apkPath = basePath + "/FanShequ/FanShequ$code.apk"
        Log.e("TestLog", apkPath)
        return apkPath
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startUpdating(apkName)
            } else
                ToastUtil.showToastL("需要文件读写权限！")
    }

    /**
     * 用户登录方法
     */
    fun onLogin(v: View) {
        val i = Intent(this, LoginActivity::class.java)
        startActivityForResult(i, ACTION_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ACTION_LOGIN -> {
//                (fragments[4] as UserFragment).refreshUser()
            }
        }
    }
}
