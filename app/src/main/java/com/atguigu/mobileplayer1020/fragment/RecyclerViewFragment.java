package com.atguigu.mobileplayer1020.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atguigu.mobileplayer1020.R;
import com.atguigu.mobileplayer1020.adapter.RecyclerFragmentAdapter;
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
public class RecyclerViewFragment extends BaseFragment {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.tv_nomedia)
    TextView tvNomedia;
    /**
     * 数据集合
     */
    private List<NetAudioBean.ListBean> listDatas;
    private RecyclerFragmentAdapter myAdapter;

    @Override
    public View initView() {
        Log.e("TAG", "网络音频ui初始化了。。");
        View view = View.inflate(mContext, R.layout.fragment_recyclerview, null);
        ButterKnife.inject(this, view);
        //设置ListView的item的点击事件
        //设置点击事件
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                NetAudioBean.ListBean listEntity = listDatas.get(position);
//                if(listEntity !=null ){
//                    //3.传递视频列表
//                    Intent intent = new Intent(mContext,PicassoSampleActivity.class);
//                    if(listEntity.getType().equals("gif")){
//                        String url = listEntity.getGif().getImages().get(0);
//                        intent.putExtra("url",url);
//                        mContext.startActivity(intent);
//                    }else if(listEntity.getType().equals("image")){
//                        String url = listEntity.getImage().getThumbnail_small().get(0);
//                        intent.putExtra("url",url);
//                        mContext.startActivity(intent);
//                    }
//                }
//
//
//            }
//        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络音频数据初始化了。。");
//        tvNomedia.setVisibility(View.VISIBLE);
//        tvNomedia.setText("hh");
        getDataFromNet();
    }


    @Override
    public void onRefrshData() {
        super.onRefrshData();
//        Log.e("TAG","onHiddenChanged。。"+this.toString());

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
        if(listDatas != null && listDatas.size() >0){
            //有视频
            tvNomedia.setVisibility(View.GONE);
            //设置适配器
            myAdapter = new RecyclerFragmentAdapter(mContext,listDatas);
            recyclerview.setAdapter(myAdapter);

            recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        }else{
            //没有视频
            tvNomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
