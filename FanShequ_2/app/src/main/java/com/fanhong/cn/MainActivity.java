package com.fanhong.cn;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fanhong.cn.customclass.MyFragmentPagerAdapter;
import com.fanhong.cn.mainfragments.DoorFragment;
import com.fanhong.cn.mainfragments.HomeFragment;
import com.fanhong.cn.mainfragments.IntertractFragment;
import com.fanhong.cn.mainfragments.MineFragment;
import com.fanhong.cn.mainfragments.ServiceFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.main_viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.main_radio_groups)
    private RadioGroup radiogroup;

    /**
     * 5个fragment
     */
    private HomeFragment homeFragment;
    private ServiceFragment serviceFragment;
    private DoorFragment doorFragment;
    private IntertractFragment intertractFragment;
    private MineFragment mineFragment;
    /**
     * 5个底部按钮
     */
    private RadioButton[] radioButtons = new RadioButton[5];
    private ImageView[] imageViews = new ImageView[5];

    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initViews();
    }

    private void initViews() {
        radioButtons[0] = (RadioButton) findViewById(R.id.home_page);
        radioButtons[1] = (RadioButton) findViewById(R.id.service_page);
        radioButtons[2] = (RadioButton) findViewById(R.id.door_page);
        radioButtons[3] = (RadioButton) findViewById(R.id.interact_page);
        radioButtons[4] = (RadioButton) findViewById(R.id.mine_page);
        imageViews[0] = (ImageView) findViewById(R.id.click_home);
        imageViews[1] = (ImageView) findViewById(R.id.click_serve);
        imageViews[2] = (ImageView) findViewById(R.id.click_entrance);
        imageViews[3] = (ImageView) findViewById(R.id.click_interaction);
        imageViews[4] = (ImageView) findViewById(R.id.click_user);

        getFragments();
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /**
                 * 页面选中
                 */
                for (int i = 0; i < 5; i++) {
                    radioButtons[i].setChecked(false);
                }
                for (int j = 0; j < 5; j++) {
                    imageViews[j].setVisibility(View.INVISIBLE);
                }
                switch (position) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getFragments() {
        fragmentList = new ArrayList<>();
        homeFragment = new HomeFragment();
        serviceFragment = new ServiceFragment();
        doorFragment = new DoorFragment();
        intertractFragment = new IntertractFragment();
        mineFragment = new MineFragment();

        fragmentList.add(homeFragment);
        fragmentList.add(serviceFragment);
        fragmentList.add(doorFragment);
        fragmentList.add(intertractFragment);
        fragmentList.add(mineFragment);
    }
}
