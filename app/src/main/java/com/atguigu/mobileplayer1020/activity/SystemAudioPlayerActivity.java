package com.atguigu.mobileplayer1020.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.mobileplayer1020.IMusicPlayerService;
import com.atguigu.mobileplayer1020.R;
import com.atguigu.mobileplayer1020.service.MusicPlayerService;

public class SystemAudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnSwichLyric;
    private int position;


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-01-11 15:19:08 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_audio_player);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnSwichLyric = (Button) findViewById(R.id.btn_swich_lyric);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);

        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable drawable = (AnimationDrawable) ivIcon.getBackground();
        drawable.start();

        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnSwichLyric.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAudioPlaymode) {
            // Handle clicks for btnAudioPlaymode
        } else if (v == btnAudioPre) {
            // Handle clicks for btnAudioPre
        } else if (v == btnAudioStartPause) {
            // Handle clicks for btnAudioStartPause

            try {
                if(service.isPlaying()){
                    //暂停
                    service.pause();
                    //按钮状态-设置播放
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                }else{
                    //播放
                    service.start();
                    //按钮状态-设置暂停
                    btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }


        } else if (v == btnAudioNext) {
            // Handle clicks for btnAudioNext
        } else if (v == btnSwichLyric) {
            // Handle clicks for btnSwichLyric
        }
    }

    private IMusicPlayerService service;

    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 当连接服务成功后回调
         * @param name
         * @param iBdinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBdinder) {
            service = IMusicPlayerService.Stub.asInterface(iBdinder);

            if (service != null) {
                try {
                    //开始播放
                    service.openAudio(position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开的时候回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        getData();
        //绑定方式启动服务
        startAndBindServide();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn != null){
            unbindService(conn);
            conn = null;
        }

    }

    private void startAndBindServide() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        //绑定服务
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        //启动服务
        startService(intent);//防止服务多次创建
    }

    private void getData() {
        /**
         * 得到播放位置
         */
        position = getIntent().getIntExtra("position", 0);
    }
}
