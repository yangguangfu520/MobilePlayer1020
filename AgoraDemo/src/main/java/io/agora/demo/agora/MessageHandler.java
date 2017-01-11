package io.agora.demo.agora;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * Created by apple on 15/9/10.
 */
public class MessageHandler extends IRtcEngineEventHandler {

    private BaseEngineEventHandlerActivity mHandlerActivity;

    //显示房间内其他用户的视频
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        }
    }

    //用户进入
    @Override
    public void onUserJoined(int uid, int elapsed){

        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onUserJoined(uid, elapsed);
        }
    }

    //用户退出
    @Override
    public void onUserOffline(int uid, int reason) {

        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onUserOffline(uid);
        }
    }

    //监听其他用户是否关闭视频
    @Override
    public void onUserMuteVideo(int uid,boolean muted){

        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onUserMuteVideo(uid, muted);
        }
    }

    //更新聊天数据
    @Override
    public void onRtcStats(RtcStats stats){

        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onUpdateSessionStats(stats);
        }
    }


    @Override
    public void onLeaveChannel(RtcStats stats) {
        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onLeaveChannel(stats);
        }
    }


    @Override
    public void onError(int err) {
        BaseEngineEventHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onError(err);
        }
    }

    public void setActivity(BaseEngineEventHandlerActivity activity) {

        this.mHandlerActivity = activity;
    }

    public BaseEngineEventHandlerActivity getActivity(){

        return mHandlerActivity;
    }
}
