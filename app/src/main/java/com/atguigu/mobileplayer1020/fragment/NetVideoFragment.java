package com.atguigu.mobileplayer1020.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.mobileplayer1020.base.BaseFragment;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/6 16:46
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：网络视频
 */
public class NetVideoFragment extends BaseFragment {
    private TextView textView;
    @Override
    public View initView() {
        Log.e("TAG","网络视频ui初始化了。。");
        textView = new TextView(mContext);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","网络视频数据初始化了。。");
        textView.setText("网络视频");
    }
}
