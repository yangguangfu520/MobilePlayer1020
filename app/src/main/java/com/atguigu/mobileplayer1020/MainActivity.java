package com.atguigu.mobileplayer1020;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.atguigu.mobileplayer1020.base.BaseFragment;
import com.atguigu.mobileplayer1020.fragment.LocalAudioFragment;
import com.atguigu.mobileplayer1020.fragment.LocalVideoFragment;
import com.atguigu.mobileplayer1020.fragment.NetAudioFragment;
import com.atguigu.mobileplayer1020.fragment.NetVideoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;

    /**
     * Fragment页面的下标位置
     */
    private int position;

    /**
     * 缓存的Fragment
     */
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);


        initFragment();

        //设置RadioGroup的监听

        initListenter();

    }

    private void initListenter() {
        //设置RadioGroup选中状态变化的监听
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_local_video:
                        position = 0;
                        break;
                    case R.id.rb_local_audio:
                        position = 1;
                        break;
                    case R.id.rb_net_audio:
                        position = 2;
                        break;
                    case R.id.rb_net_video:
                        position = 3;
                        break;
                }

                //Fragment-当前的Fragment
                Fragment currentFragment = fragments.get(position);
                switchFragment(currentFragment);
            }
        });

        //默认选中本地视频
        rg_main.check(R.id.rb_local_video);//onCheckedChanged
    }

    private void switchFragment(Fragment currentFragment) {
        if (tempFragment != currentFragment) {

            //开启事务
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //切换
            if (currentFragment != null) {
                //是否添加过
                if (!currentFragment.isAdded()) {

                    //把之前显示的给隐藏
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }
                    //如果没有添加就添加
                    ft.add(R.id.fl_mainc_content, currentFragment);

                } else {

                    //把之前的隐藏
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }
                    //如果添加了就直接显示
                    ft.show(currentFragment);

                }

                //最后一步，提交事务
                ft.commit();

            }
            tempFragment = currentFragment;

        }



    }

    /**
     * 初始化Fragment
     * 有先后顺序要求
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoFragment());//本地视频
        fragments.add(new LocalAudioFragment());//本地音乐
        fragments.add(new NetAudioFragment());//网络音乐
        fragments.add(new NetVideoFragment());//网络视频

    }
}
