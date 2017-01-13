// IMusicPlayerService.aidl
package com.atguigu.mobileplayer1020;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

             /**
                 * 根据位置打开一个音频并且播放
                 * @param position
                 */
                void openAudio(int position);
                /**
                 * 开始播放音频
                 */
                void start();

                /**
                 * 暂停
                 */
                void pause();

                /**
                 * 得到歌曲的名称
                 */
                String getAudioName();
                /**
                 * 得到歌曲演唱者的名字
                 */
                String getArtistName();
                /**
                 * 得到歌曲的当前播放进度
                 */
                int getCurrentPosition();
                /**
                 * 得到歌曲的当前总进度
                 */
                int getDuration();

                /**
                 * 播放下一首歌曲
                 */
                void next();
                /**
                 * 播放上一首歌曲
                 */
                void pre();
                /**
                 * 得到播放模式
                 */
                int getPlayMode();
                /**
                 * 设置播放模式
                 */
                void setPlayMode(int mode);

                /**
                 是否在播放
                */
                boolean isPlaying();

                /**
                 根据传入的位置，播放
                */
                void seekTo(int postion);

                String getAudioPath();
}
