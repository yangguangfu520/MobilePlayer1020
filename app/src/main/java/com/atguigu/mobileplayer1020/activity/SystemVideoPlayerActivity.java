package com.atguigu.mobileplayer1020.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.atguigu.mobileplayer1020.R;
import com.atguigu.mobileplayer1020.utils.Utils;

public class SystemVideoPlayerActivity extends Activity implements View.OnClickListener {

    private static final String TAG = SystemVideoPlayerActivity.class.getSimpleName();//"SystemVideoPlayerActivity;
    private VideoView videoview;
    /**
     * 进度跟新
     */
    private static final int PROGRESS = 0;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystetime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwichePlayer;
    private LinearLayout llBottom;
    private TextView tvCurrenttime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwichScreen;
    private Utils utils;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-01-09 09:33:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        videoview = (VideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystetime = (TextView) findViewById(R.id.tv_systetime);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwichePlayer = (Button) findViewById(R.id.btn_swiche_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrenttime = (TextView) findViewById(R.id.tv_currenttime);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwichScreen = (Button) findViewById(R.id.btn_swich_screen);

        btnVoice.setOnClickListener(this);
        btnSwichePlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwichScreen.setOnClickListener(this);
    }


    /**
     * 视频播放地址
     */
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        initData();
        findViews();
        getData();
        //设置视频加载的监听
        setLinstener();
        setData();
    }

    private void initData() {
        utils = new Utils();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS://视频播放进度的更新
                    int currentPosition = videoview.getCurrentPosition();
                    //设置视频更新
                    seekbarVideo.setProgress(currentPosition);


                    //设置播放进度的时间
                    tvCurrenttime.setText(utils.stringForTime(currentPosition));
                    //不断发消息
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
            }
        }
    };

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-01-09 09:33:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            // Handle clicks for btnVoice
        } else if (v == btnSwichePlayer) {
            // Handle clicks for btnSwichePlayer
        } else if (v == btnExit) {
            // Handle clicks for btnExit
            finish();
        } else if (v == btnPre) {
            // Handle clicks for btnPre
        } else if (v == btnStartPause) {
            if (videoview.isPlaying()) {//是否在播放
                //当前在播放要设置为暂停
                videoview.pause();
                //按钮状态-播放状态
                btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
            } else {
                //当前暂停状态要设置播放状态
                videoview.start();
                //按钮状态-暂停状态
                btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
            }
            // Handle clicks for btnStartPause
        } else if (v == btnNext) {
            // Handle clicks for btnNext
        } else if (v == btnSwichScreen) {
            // Handle clicks for btnSwichScreen
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
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
//        videoview.setMediaController(new MediaController(this));

        //设置视频的拖动监听
        seekbarVideo.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());


    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * 状态变化的时候回调
         *
         * @param seekBar
         * @param progress 当前改变的进度-要拖动到的位置
         * @param fromUser 用户导致的改变true,否则false
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoview.seekTo(progress);
            }

        }

        /**
         * 当手指一按下的时候回调
         *
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * 当手指离开的时候回调
         *
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
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
         *
         * @param mp
         */
        @Override
        public void onPrepared(MediaPlayer mp) {
            //开始播放
            videoview.start();

            //准备好的时候
            //1.视频的总播放时长和SeeKBar关联起来
            int duration = videoview.getDuration();
            seekbarVideo.setMax(duration);

            //设置总时长
            tvDuration.setText(utils.stringForTime(duration));

            //发消息
            handler.sendEmptyMessage(PROGRESS);


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
        uri = getIntent().getData();
    }
}
