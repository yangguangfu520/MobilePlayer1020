package com.atguigu.mobileplayer1020.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.atguigu.mobileplayer1020.IMusicPlayerService;
import com.atguigu.mobileplayer1020.bean.MediaItem;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/11 15:41
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用： 播放音乐的服务
 */
public class MusicPlayerService extends Service {

    /**
     * AIDL生成的类
     */
    IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        //把服务当成成员变量
        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();

        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getArtistName() throws RemoteException {
            return service.getArtistName();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public void next() throws RemoteException {
            service.next();

        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }
    };
    private ArrayList<MediaItem> mediaItems;
    /**
     * 音频是否加载完成
     */
    private boolean isLoaded = false;
    private MediaItem mediaItem;
    private int position;
    private MediaPlayer mediaPlayer;

    /**
     * 返回代理类
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getDataFromLocal();
    }

    /**
     * 子线程中得到音频
     */
    private void getDataFromLocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                //初始化集合
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                //sdcard 的视频路径
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//在sdcard显示的视频名称
                        MediaStore.Audio.Media.DURATION,//视频的时长,毫秒
                        MediaStore.Audio.Media.SIZE,//文件大小-byte
                        MediaStore.Audio.Media.DATA,//在sdcard的路径-播放地址
                        MediaStore.Audio.Media.ARTIST//艺术家
                };
                Cursor cusor = resolver.query(uri, objs, null, null, null);
                if (cusor != null) {

                    while (cusor.moveToNext()) {

                        MediaItem mediaItem = new MediaItem();

                        //添加到集合中
                        mediaItems.add(mediaItem);//可以

                        String name = cusor.getString(0);
                        mediaItem.setName(name);
                        long duration = cusor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size = cusor.getLong(2);
                        mediaItem.setSize(size);
                        String data = cusor.getString(3);//播放地址
                        mediaItem.setData(data);
                        String artist = cusor.getString(4);//艺术家
                        mediaItem.setArtist(artist);

                    }

                    cusor.close();
                }


                //音频加载完成
                isLoaded = true;


            }
        }.start();

    }

    /**
     * 根据位置打开一个音频并且播放
     *
     * @param position
     */
    void openAudio(int position) {
        if (mediaItems != null && mediaItems.size() > 0) {
            mediaItem = mediaItems.get(position);
            this.position = position;

            //MediaPlayer
            if (mediaPlayer != null) {
                mediaPlayer.reset();//上一曲重置
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            //设置三个监听
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
            mediaPlayer.setOnErrorListener(new MyOnErrorListener());

            //设置播放地址
            try {
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (!isLoaded) {
            Toast.makeText(this, "没有加载完成", Toast.LENGTH_SHORT).show();
        }

    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }


    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
        }
    }

    /**
     * 开始播放音频
     */
    void start() {
        mediaPlayer.start();

    }

    /**
     * 暂停
     */
    void pause() {
        mediaPlayer.pause();
    }

    /**
     * 得到歌曲的名称
     */
    String getAudioName() {
        return "";
    }

    /**
     * 得到歌曲演唱者的名字
     */
    String getArtistName() {
        return "";
    }

    /**
     * 得到歌曲的当前播放进度
     */
    int getCurrentPosition() {
        return 0;
    }

    /**
     * 得到歌曲的当前总进度
     */
    int getDuration() {
        return 0;
    }

    /**
     * 播放下一首歌曲
     */
    void next() {

    }

    /**
     * 播放上一首歌曲
     */
    void pre() {

    }

    /**
     * 得到播放模式
     */
    int getPlayMode() {
        return 0;
    }

    /**
     * 设置播放模式
     */
    void setPlayMode(int mode) {

    }


}
