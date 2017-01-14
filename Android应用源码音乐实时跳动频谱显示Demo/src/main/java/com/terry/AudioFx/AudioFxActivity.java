package com.terry.AudioFx;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

public class AudioFxActivity extends Activity
{

	private static final String TAG = "AudioFxActivity";


	private MediaPlayer mMediaPlayer;
	private Visualizer mVisualizer;

	private  BaseVisualizerView mBaseVisualizerView;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);


		setContentView(R.layout.main);
		mBaseVisualizerView = (BaseVisualizerView) findViewById(R.id.visualizerview);

		mMediaPlayer = MediaPlayer.create(this, R.raw.z8806c);




		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mMediaPlayer.start();
				setupVisualizerFxAndUi();
			}
		});


		mMediaPlayer.setLooping(true);

	}


	/**
	 * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
	 */
	private void setupVisualizerFxAndUi()
	{

		int audioSessionid = mMediaPlayer.getAudioSessionId();
		System.out.println("audioSessionid=="+audioSessionid);
		mVisualizer = new Visualizer(audioSessionid);
		// 参数内必须是2的位数
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		// 设置允许波形表示，并且捕获它
		mBaseVisualizerView.setVisualizer(mVisualizer);
		mVisualizer.setEnabled(true);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (isFinishing() && mMediaPlayer != null)
		{
			mVisualizer.release();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

}