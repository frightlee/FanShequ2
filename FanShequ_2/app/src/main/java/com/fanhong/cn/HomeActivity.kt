package com.fanhong.cn

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.fanhong.cn.door_page.DoorFragment
import com.fanhong.cn.home_page.CommunityFragment
import com.fanhong.cn.home_page.HomeFragment
import com.fanhong.cn.home_page.ServiceFragment
import com.fanhong.cn.login_pages.LoginActivity
import com.fanhong.cn.user_page.UserFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class HomeActivity : AppCompatActivity() {

    private val ACTION_LOGIN: Int = 11

    private val fragments: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
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

    /**
     * 用户登录方法
     */
    fun onLogin(v: View) {
        val i = Intent(this, LoginActivity::class.java)
        startActivityForResult(i, ACTION_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ACTION_LOGIN->{}
        }
    }
}
