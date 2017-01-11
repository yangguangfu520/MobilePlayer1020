package io.agora.demo.agora;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 *
 * A handler activity act as a bridge to take callbacks from @MessageHandler.
 * Subclasses should override these key methods.
 *
 * Created by on 9/13/15.
 */
public class BaseEngineEventHandlerActivity extends BaseActivity {


    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    public void onError(int err) {
    }

    public void onCameraReady() {
    }

    public void onAudioQuality(int uid, int quality, short delay, short lost) {
    }

    public void onAudioTransportQuality(int uid, short delay, short lost) {
    }

    public void onVideoTransportQuality(int uid, short delay, short lost) {
    }

    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
    }

    public void onUpdateSessionStats(IRtcEngineEventHandler.RtcStats stats) {
    }

    public void onRecap(byte[] recap) {
    }

    public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
    }

    public void onNetworkQuality(int quality) {
    }

    public void onUserJoined(int uid, int elapsed) {
    }

    public void onUserOffline(int uid) {
    }

    public void onUserMuteAudio(int uid, boolean muted) {
    }

    public void onUserMuteVideo(int uid, boolean muted) {
    }

    public void onAudioRecorderException(int nLastTimeStamp) {
    }

    public void onRemoteVideoStat(int uid, int frameCount, int delay, int receivedBytes) {
    }

    public void onLocalVideoStat(int sentBytes, int sentFrames) {
    }

    public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
    }

    public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
    }

    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
    }

    public void onConnectionLost() {
    }

    public void onMediaEngineEvent(int code) {
    }

}
