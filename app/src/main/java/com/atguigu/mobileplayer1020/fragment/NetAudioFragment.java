package com.atguigu.mobileplayer1020.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.mobileplayer1020.R;
import com.atguigu.mobileplayer1020.base.BaseFragment;
import com.atguigu.mobileplayer1020.bean.NetAudioBean;
import com.atguigu.mobileplayer1020.utils.Constant;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/6 16:46
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：网络音频
 */
public class NetAudioFragment extends BaseFragment {

    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.tv_nomedia)
    TextView tvNomedia;
    /**
     * 数据集合
     */
    private List<NetAudioBean.ListBean> listDatas;

    @Override
    public View initView() {
        Log.e("TAG", "网络音频ui初始化了。。");
        View view = View.inflate(mContext, R.layout.fragment_net_audio, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络音频数据初始化了。。");
        tvNomedia.setVisibility(View.VISIBLE);
        tvNomedia.setText("hh");
    }


    @Override
    public void onRefrshData() {
        super.onRefrshData();
//        Log.e("TAG","onHiddenChanged。。"+this.toString());
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constant.NET_AUDIO);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e("TAG", "网络音乐请求成功" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "网络音乐请求失败" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        NetAudioBean netAudioBean = new Gson().fromJson(json, NetAudioBean.class);
        listDatas = netAudioBean.getList();


        Log.e("TAG","解决成功=="+listDatas.get(0).getText());

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
