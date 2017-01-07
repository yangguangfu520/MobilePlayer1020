package com.atguigu.mobileplayer1020.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.atguigu.mobileplayer1020.R;

public class SystemVideoPlayerActivity extends Activity {

    private static final String TAG = SystemVideoPlayerActivity.class.getSimpleName();//"SystemVideoPlayerActivity;
    private VideoView videoview;

    /**
     * 视频播放地址
     */
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
        setContentView(R.layout.activity_system_video_player);
        videoview = (VideoView) findViewById(R.id.videoview);
        getData();
        //设置视频加载的监听
        setLinstener();
        setData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }



    private void setData() {
        //设置播放地址
        videoview.setVideoURI(uri);
    }

    private void setLinstener() {
        //设置视频播放监听：准备好的监听，播放出错监听，播放完成监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        videoview.setOnErrorListener(new MyOnErrorListener());

        videoview.setOnCompletionListener(new MyOnCompletionListener());

        //设置控制面板
        videoview.setMediaController(new MediaController(this));

    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {            //1.单个视频-退出播放器
            //2.视频列表-播放下一个
            Toast.makeText(SystemVideoPlayerActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(SystemVideoPlayerActivity.this, "播放出错了，亲", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        /**
         * 当底层加载视频准备完成的时候回调
         * @param mp
         */
        @Override
        public void onPrepared(MediaPlayer mp) {
            //开始播放
            videoview.start();

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            Intent intent = new Intent(this,TestB.class);
//            startActivity(intent);

            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 得到播放地址
     */
    private void getData() {
       uri =  getIntent().getData();
    }
}
