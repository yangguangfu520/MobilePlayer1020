package com.atguigu.voicedialog;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public final class SpeechUtils {

    private static SpeechUtils instance;

    private SpeechRecognizer mIat;

    private LexiconListener lexiconListener;

    private SpeechUtils(Context context) {
        //1.创建SpeechRecognizer对象，第二个参数： 本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(context, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》 SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

    }


    public SpeechRecognizer getmIat() {
        return mIat;
    }

    public static SpeechUtils getInstance(Context context) {

        if (instance == null) {
            synchronized (SpeechUtils.class) {
                if (instance == null) {
                    instance = new SpeechUtils(context);
                }
            }
        }
        return instance;
    }


    /**
     * 把给定的文本内容读出来
     *
     * @param context
     * @param text
     */
    public void speakText(Context context, String text) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "vixl");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "70");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80"); //设置音量，范围0~100
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        //3.开始合成
        mTts.startSpeaking(text, new MySynthesizerListener());
    }

    class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

        }

        @Override
        public void onCompleted(SpeechError arg0) {

        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakProgress(int arg0, int arg1, int arg2) {

        }

        @Override
        public void onSpeakResumed() {

        }

    }

    /**
     * 弹出识别的对话框, 并开始语音识别
     */
    public void showListenVoiceDialog(Context context, RecognizerDialogListener recognizerDialogListener) {
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        RecognizerDialog iatDialog = new RecognizerDialog(context, new MyInitListener());
        //2.设置听写参数
        iatDialog.setParameter(SpeechConstant.DOMAIN, "iat");//听写匹配引擎,iat为默认值(日常用语) video(视频) poi(地图) music(音乐)
        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//zh_cn(简体中文) en_us(美式英文)
        iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin ");//方言参数 mandarin为默认值(普通话) cantonese(粤语) lmz(四川话) henanese(河南话)
        //3.设置回调接口
        iatDialog.setListener(recognizerDialogListener);
        //4.开始听写
        iatDialog.show();
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int arg0) {

        }
    }

}
