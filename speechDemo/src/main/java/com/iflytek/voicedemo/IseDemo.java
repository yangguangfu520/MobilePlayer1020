package com.iflytek.voicedemo;

import com.iflytek.ise.result.Result;
import com.iflytek.ise.result.xml.XmlResultParser;
import com.iflytek.speech.setting.IseSettings;
import com.iflytek.sunflower.FlowerCollector;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 语音评测demo
 */
public class IseDemo extends Activity implements OnClickListener {
	private static String TAG = IseDemo.class.getSimpleName();
	
	private final static String PREFER_NAME = "ise_settings";
	private final static int REQUEST_CODE_SETTINGS = 1;

	private EditText mEvaTextEditText;
	private EditText mResultEditText;
	private Button mIseStartButton;
	private Toast mToast;

	// 评测语种
	private String language;
	// 评测题型
	private String category;
	// 结果等级
	private String result_level;
	
	private String mLastResult;
	private SpeechEvaluator mIse;
	
	
	// 评测监听接口
	private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {
		
		@Override
		public void onResult(EvaluatorResult result, boolean isLast) {
			Log.d(TAG, "evaluator result :" + isLast);

			if (isLast) {
				StringBuilder builder = new StringBuilder();
				builder.append(result.getResultString());
				
				if(!TextUtils.isEmpty(builder)) {
					mResultEditText.setText(builder.toString());
				}
				mIseStartButton.setEnabled(true);
				mLastResult = builder.toString();
				
				showTip("评测结束");
			}
		}

		@Override
		public void onError(SpeechError error) {
			mIseStartButton.setEnabled(true);
			if(error != null) {	
				showTip("error:"+ error.getErrorCode() + "," + error.getErrorDescription());
				mResultEditText.setText("");
				mResultEditText.setHint("请点击“开始评测”按钮");
			} else {
				Log.d(TAG, "evaluator over");
			}
		}

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			Log.d(TAG, "evaluator begin");
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
			Log.d(TAG, "evaluator stoped");
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("当前音量：" + volume);
			Log.d(TAG, "返回音频数据："+data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.isedemo);

		mIse = SpeechEvaluator.createEvaluator(IseDemo.this, null);
		initUI();
		setEvaText();
	}

	private void initUI() {
		findViewById(R.id.image_ise_set).setOnClickListener(IseDemo.this);
		mEvaTextEditText = (EditText) findViewById(R.id.ise_eva_text);
		mResultEditText = (EditText)findViewById(R.id.ise_result_text);
		mIseStartButton = (Button) findViewById(R.id.ise_start);
		mIseStartButton.setOnClickListener(IseDemo.this);
		findViewById(R.id.ise_parse).setOnClickListener(IseDemo.this);
		findViewById(R.id.ise_stop).setOnClickListener(IseDemo.this);
		findViewById(R.id.ise_cancel).setOnClickListener(IseDemo.this);
				
		mToast = Toast.makeText(IseDemo.this, "", Toast.LENGTH_LONG);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.image_ise_set:
				Intent intent = new Intent(IseDemo.this, IseSettings.class);
				startActivityForResult(intent, REQUEST_CODE_SETTINGS);
				break;
			case R.id.ise_start:
				if (mIse == null) {
					return;
				}
	
				String evaText = mEvaTextEditText.getText().toString();
				mLastResult = null;
				mResultEditText.setText("");
				mResultEditText.setHint("请朗读以上内容");
				mIseStartButton.setEnabled(false);
				
				setParams();
				mIse.startEvaluating(evaText, null, mEvaluatorListener);
				break;
			case R.id.ise_parse:
				// 解析最终结果
				if (!TextUtils.isEmpty(mLastResult)) {
					XmlResultParser resultParser = new XmlResultParser();
					Result result = resultParser.parse(mLastResult);
					
					if (null != result) {
						mResultEditText.setText(result.toString());
					} else {
						showTip("结析结果为空");
					}
				}
				break;
			case R.id.ise_stop:
				if (mIse.isEvaluating()) {
					mResultEditText.setHint("评测已停止，等待结果中...");
					mIse.stopEvaluating();
				}
				break;
			case R.id.ise_cancel: {
				mIse.cancel();
				mIseStartButton.setEnabled(true);
				mResultEditText.setText("");
				mResultEditText.setHint("请点击“开始评测”按钮");
				mLastResult = null;
				break;
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (REQUEST_CODE_SETTINGS == requestCode) {
			setEvaText();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (null != mIse) {
			mIse.destroy();
			mIse = null;
		}
	}
	
	// 设置评测试题
	private void setEvaText() {
		SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
		language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
		category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");
		
		String text = "";
		if ("en_us".equals(language)) {
			if ("read_word".equals(category)) {
				text = getString(R.string.text_en_word);
			} else if ("read_sentence".equals(category)) {
				text = getString(R.string.text_en_sentence);
			} 
		} else {
			// 中文评测
			if ("read_syllable".equals(category)) {
				text = getString(R.string.text_cn_syllable);
			} else if ("read_word".equals(category)) {
				text = getString(R.string.text_cn_word);
			} else if ("read_sentence".equals(category)) {
				text = getString(R.string.text_cn_sentence);
			} 
		}
		
		mEvaTextEditText.setText(text);
		mResultEditText.setText("");
		mLastResult = null;
		mResultEditText.setHint("请点击“开始评测”按钮");
	}

	private void showTip(String str) {
		if(!TextUtils.isEmpty(str)) {
			mToast.setText(str);
			mToast.show();
		}
	}
	private void setParams() {
		SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
		// 设置评测语言
		language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置需要评测的类型
		category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");
		// 设置结果等级（中文仅支持complete）
		result_level = pref.getString(SpeechConstant.RESULT_LEVEL, "complete");
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		String vad_bos = pref.getString(SpeechConstant.VAD_BOS, "5000");
		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		String vad_eos = pref.getString(SpeechConstant.VAD_EOS, "1800");
		// 语音输入超时时间，即用户最多可以连续说多长时间；
		String speech_timeout = pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
		
		mIse.setParameter(SpeechConstant.LANGUAGE, language);
		mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
		mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
		mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
		mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);
		mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
		mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIse.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
	}
	
	@Override
	protected void onResume() {
		// 开放统计 移动数据统计分析
		FlowerCollector.onResume(IseDemo.this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// 开放统计 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(IseDemo.this);
		super.onPause();
	}
}
