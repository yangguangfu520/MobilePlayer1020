package com.atguigu.mobileplayer1020.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.mobileplayer1020.R;
import com.atguigu.mobileplayer1020.bean.MediaItem;
import com.atguigu.mobileplayer1020.utils.Utils;
import com.atguigu.mobileplayer1020.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemVideoPlayerActivity extends Activity implements View.OnClickListener {

    private static final String TAG = SystemVideoPlayerActivity.class.getSimpleName();//"SystemVideoPlayerActivity;
    /**
     * 视频默认屏幕大小播放
     */
    private static final int VIDEO_TYPE_DEFAULT = 1;
    /**
     * 视频全屏播放
     */
    private static final int VIDEO_TYPE_FULL = 2;

    private VideoView videoview;
    /**
     * 进度跟新
     */
    private static final int PROGRESS = 0;
    /**
     * 隐藏控制面板
     */
    private static final int HIDE_MEDIA_CONTROLLER = 1;

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
    private MyBroadcastReceiver receiver;
    /**
     * 列表数据
     */
    private ArrayList<MediaItem> mediaItems;
    private int position;
    private GestureDetector detector;
    /**
     * 是否显示控制面板
     */
    private boolean isShowMediaController = false;
    /**
     * 视频是否全屏显示
     */
    private boolean isFullScreen = false;
    private int screenWidth = 0;
    private int screeHeight = 0;
    private int videoWidth = 0;
    private int videoHeight =0;

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

        hideMediaController();//隐藏控制面板
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

        //注册监听电量广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        //监听电量变化
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        //初始化手势识别器
        detector = new GestureDetector(this, new MySimpleOnGestureListener());



        //得到屏幕的宽和高
        DisplayMetrics outMetrics =new DisplayMetrics() ;
        //得到屏幕参数类
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        //屏幕的宽和高
        screenWidth =outMetrics.widthPixels;
        screeHeight = outMetrics.heightPixels;

    }

    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
//                Toast.makeText(SystemVideoPlayerActivity.this, "我被长按了", Toast.LENGTH_SHORT).show();
            startAndPause();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
//            Toast.makeText(SystemVideoPlayerActivity.this, "我被双击了", Toast.LENGTH_SHORT).show();
            if(isFullScreen){
                //设置默认
                setVideoType(VIDEO_TYPE_DEFAULT);
            }else{
                //全屏显示
                setVideoType(VIDEO_TYPE_FULL);
            }
            return super.onDoubleTap(e);

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Toast.makeText(SystemVideoPlayerActivity.this, "我被单击", Toast.LENGTH_SHORT).show();
            if (isShowMediaController) {
                //隐藏
                hideMediaController();
                //把消息移除
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            } else {
                //显示
                showMediaController();
                //重新发消息-4秒隐藏
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void setVideoType(int videoTypeDefault) {
        switch (videoTypeDefault){
            case VIDEO_TYPE_FULL:
                isFullScreen = true;
                videoview.setViewSize(screenWidth,screeHeight);

                //把按钮设置-默认
                btnSwichScreen.setBackgroundResource(R.drawable.btn_screen_default_selector);

                break;
            case VIDEO_TYPE_DEFAULT://视频画面的默认
                isFullScreen = false;

                //视频原始的画面大小
                int  mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                /**
                 * 计算后的值
                 */
                int width = screenWidth;
                int height = screeHeight;

                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                //把计算好的视频大小传递过去
                videoview.setViewSize(width,height);
                //把按钮设置-全屏
                btnSwichScreen.setBackgroundResource(R.drawable.btn_screen_full_selector);
                break;
        }
    }

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        isShowMediaController = true;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);

    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        isShowMediaController = false;
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_MEDIA_CONTROLLER:
                    hideMediaController();//隐藏控制面板
                    break;
                case PROGRESS://视频播放进度的更新
                    int currentPosition = videoview.getCurrentPosition();
                    //设置视频更新
                    seekbarVideo.setProgress(currentPosition);


                    //设置播放进度的时间
                    tvCurrenttime.setText(utils.stringForTime(currentPosition));


                    //得到系统的时间并且更新
                    tvSystetime.setText(getSystemTime());


                    //不断发消息
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
            }
        }
    };

    /**
     * 得到系统的时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到电量:0~100
            int level = intent.getIntExtra("level", 0);
            //主线程
            setBattery(level);
        }
    }

    /**
     * 设置电量
     *
     * @param level
     */
    private void setBattery(int level) {
        if (level <= 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }

    }

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
        } else if (v == btnPre) {//上一个的点击事件
            // Handle clicks for btnPre
            setPreVideo();
        } else if (v == btnStartPause) {
            startAndPause();
            // Handle clicks for btnStartPause
        } else if (v == btnNext) {//下一个的点击事件
            // Handle clicks for btnNext
            setNextVideo();
        } else if (v == btnSwichScreen) {
            // Handle clicks for btnSwichScreen
            if(isFullScreen){
                //设置默认
                setVideoType(VIDEO_TYPE_DEFAULT);
            }else{
                //全屏显示
                setVideoType(VIDEO_TYPE_FULL);
            }
        }

        //移除消息
        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        //重新发消息
        handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,4000);
    }

    private void startAndPause() {
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
        Log.e(TAG, "onDestroy");
        //是否资源-释放孩子的
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        //消息移除
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }


    private void setData() {

        if (mediaItems != null && mediaItems.size() > 0) {
            //根据位置获取播放视频的对象
            MediaItem mediaItem = mediaItems.get(position);
            videoview.setVideoPath(mediaItem.getData());
            tvName.setText(mediaItem.getName());
        } else if (uri != null) {
            //设置播放地址
            videoview.setVideoURI(uri);
            tvName.setText(uri.toString());
        }


        checkButtonStatus();

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
                //响应用户拖动
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
            //移除消息
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        }

        /**
         * 当手指离开的时候回调
         *
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,4000);
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            //1.单个视频-退出播放器
            //2.视频列表-播放下一个
//            Toast.makeText(SystemVideoPlayerActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
            setNextVideo();
        }
    }

    private void setPreVideo() {
        //1.判断一下列表
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {
                MediaItem mediaItem = mediaItems.get(position);
                //设置标题
                tvName.setText(mediaItem.getName());
                //设置播放地址
                videoview.setVideoPath(mediaItem.getData());
                //校验按钮状态
                checkButtonStatus();

            } else {
                //越界
                position = 0;
            }
        }


    }

    /**
     * 设置播放下一个
     */
    private void setNextVideo() {
        //1.判断一下列表
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                //设置标题
                tvName.setText(mediaItem.getName());
                //设置播放地址
                videoview.setVideoPath(mediaItem.getData());
                //专题的校验
                checkButtonStatus();

                if (position == mediaItems.size() - 1) {
                    Toast.makeText(this, "哥们播放最后一个视频了哦", Toast.LENGTH_SHORT).show();
                }

            } else {
                //越界
                position = mediaItems.size() - 1;
                finish();
            }
        }
        //2. 单个的uri
        else if (uri != null) {
            finish();
        }


    }

    private void checkButtonStatus() {
        //1.判断一下列表
        if (mediaItems != null && mediaItems.size() > 0) {
            //1.其他设置默认
            setButtonEnable(true);

            //2.播放第0个，上一个i设置灰色
            if (position == 0) {
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
            }
            //3.播放最后一个设置，下一个按钮设置灰色
            if (position == mediaItems.size() - 1) {
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }

        }
        //2. 单个的uri
        else if (uri != null) {

            //上一个和下一个都要设置灰色
            setButtonEnable(false);
        }
    }

    /***
     * 设置按钮的可点状态
     *
     * @param isEnable
     */
    private void setButtonEnable(boolean isEnable) {
        if (isEnable) {
            btnPre.setBackgroundResource(R.drawable.btn_pre_selector);
            btnNext.setBackgroundResource(R.drawable.btn_next_selector);

        } else {
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
        }
        btnPre.setEnabled(isEnable);
        btnNext.setEnabled(isEnable);
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

            //得到视频原始的大小
             videoWidth = mp.getVideoWidth();
             videoHeight = mp.getVideoHeight();

            //设置默认大小
            setVideoType(VIDEO_TYPE_DEFAULT);

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
        detector.onTouchEvent(event);//把事件传递给手势识别器
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
        //一个地址：从文件发起的单个播放请求
        uri = getIntent().getData();
        //得到视频列表
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
    }
}
