package com.atguigu.mobileplayer1020.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mobileplayer1020.R;
import com.atguigu.mobileplayer1020.base.BaseFragment;
import com.atguigu.mobileplayer1020.bean.MediaItem;
import com.atguigu.mobileplayer1020.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/6 16:46
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：网络视频
 */
public class NetVideoFragment extends BaseFragment {

    /**
     * 数据集合
     */
    private ArrayList<MediaItem> mediaItems;

    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.tv_no_media)
    private  TextView tv_no_media;
    @Override
    public View initView() {
        Log.e("TAG","网络视频ui初始化了。。");
        View view  = View.inflate(mContext, R.layout.fragment_net_video,null);
        //把view注入到xUtils3框中
        x.view().inject(NetVideoFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","网络视频数据初始化了。。");
//        tv_no_media.setText("呵呵");
        getDataFromNet();
    }

    /**
     * 使用xutils3联网请求数据
     */
    private void getDataFromNet() {
        //网络的路径
        RequestParams params  = new RequestParams(Constant.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
           public void onSuccess(String result) {

                Log.e("TA","xUtils3联网请求成功==");
                Log.e("TAG","线程名称=="+Thread.currentThread().getName());
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","xUtils3请求失败了=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 解析json数据：gson解析，fastjson解析和手动解析（原生的api）
     * 显示数据-设置适配器
     * @param json
     */
    private void processData(String json) {
        mediaItems = parsedJson(json);

        Log.e("TAG","mediaItems.get(0).getName()=="+mediaItems.get(0).getName());
    }

    /**
     * 使用系统的接口解析json数据
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parsedJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("trailers");


            for (int i=0;i<jsonArray.length();i++){

                MediaItem mediaItem = new MediaItem();

                mediaItems.add(mediaItem);//添加到集合中

                JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                String name = jsonObjectItem.optString("movieName");
                mediaItem.setName(name);
                String desc = jsonObjectItem.optString("videoTitle");
                mediaItem.setDesc(desc);
                String url = jsonObjectItem.optString("url");
                mediaItem.setData(url);
                String hightUrl = jsonObjectItem.optString("hightUrl");
                mediaItem.setHeightUrl(hightUrl);
                String coverImg = jsonObjectItem.optString("coverImg");
                mediaItem.setImageUrl(coverImg);
                int videoLength = jsonObjectItem.optInt("videoLength");
                mediaItem.setDuration(videoLength);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return mediaItems;
    }

    @Override
    public void onRefrshData() {
        super.onRefrshData();
//        Log.e("TAG","onHiddenChanged。。"+this.toString());
    }
}
