/**
 * 
 */
package com.iflytek.speech.setting;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.voicedemo.R;

/**
 * 评测设置界面
 */
public class IseSettings extends PreferenceActivity {
	private final static String PREFER_NAME = "ise_settings";
	
	private ListPreference mLanguagePref;
	private ListPreference mCategoryPref;
	private ListPreference mResultLevelPref;
	private EditTextPreference mVadBosPref;
	private EditTextPreference mVadEosPref;
	private EditTextPreference mSpeechTimeoutPref;
	
	private Toast mToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
		addPreferencesFromResource(R.xml.ise_settings);
		
		initUI();
	}

	private void initUI() {
		mLanguagePref = (ListPreference) findPreference(SpeechConstant.LANGUAGE);
		mCategoryPref = (ListPreference) findPreference(SpeechConstant.ISE_CATEGORY);
		mResultLevelPref = (ListPreference) findPreference(SpeechConstant.RESULT_LEVEL);
		mVadBosPref = (EditTextPreference) findPreference(SpeechConstant.VAD_BOS);
		mVadEosPref = (EditTextPreference) findPreference(SpeechConstant.VAD_EOS);
		mSpeechTimeoutPref = (EditTextPreference) findPreference(SpeechConstant.KEY_SPEECH_TIMEOUT);

		mToast = Toast.makeText(IseSettings.this, "", Toast.LENGTH_LONG);
		
		mLanguagePref.setSummary("当前：" + mLanguagePref.getEntry());
		mCategoryPref.setSummary("当前：" + mCategoryPref.getEntry());
		mResultLevelPref.setSummary("当前：" + mResultLevelPref.getEntry());
		mVadBosPref.setSummary("当前：" + mVadBosPref.getText() + "ms");
		mVadEosPref.setSummary("当前：" + mVadEosPref.getText() + "ms");
		
		String speech_timeout = mSpeechTimeoutPref.getText();
		String summary = "当前：" + speech_timeout;
		if (!"-1".equals(speech_timeout)) {
			summary += "ms";
		}
		mSpeechTimeoutPref.setSummary(summary);
		
		mLanguagePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if ("zh_cn".equals(newValue.toString())) {
					if ("plain".equals(mResultLevelPref.getValue())) {
						showTip("汉语评测结果格式不支持plain设置");
						return false;
					}
				} else {
					if ("read_syllable".equals(mCategoryPref.getValue())) {
						showTip("英语评测不支持单字");
						return false;
					}
				}
				
				int newValueIndex = mLanguagePref.findIndexOfValue(newValue.toString());
				String newEntry = (String) mLanguagePref.getEntries()[newValueIndex];
				mLanguagePref.setSummary("当前：" + newEntry);
				return true;
			}
		});
		
		mCategoryPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if ("en_us".equals(mLanguagePref.getValue()) && "read_syllable".equals(newValue.toString())) {
					showTip("英语评测不支持单字，请选其他项");
					return false;
				}
				
				int newValueIndex = mCategoryPref.findIndexOfValue(newValue.toString());
				String newEntry = (String) mCategoryPref.getEntries()[newValueIndex];
				mCategoryPref.setSummary("当前：" + newEntry);
				return true;
			}
		});
		
		mResultLevelPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if ("zh_cn".equals(mLanguagePref.getValue()) && "plain".equals(newValue.toString())) {
					showTip("汉语评测不支持plain，请选其他项");
					return false;
				}
				
				mResultLevelPref.setSummary("当前：" + newValue.toString());
				return true;
			}
		});
		
		mVadBosPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		mVadBosPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int bos;
				try {
					bos = Integer.parseInt(newValue.toString());
				} catch (Exception e) {
					showTip("无效输入！");
					return false;
				}
				if (bos < 0 || bos > 30000) {
					showTip("取值范围为0~30000");
					return false;
				}
				
				mVadBosPref.setSummary("当前：" + bos + "ms");
				return true;
			}
		});
		
		mVadEosPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		mVadEosPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int eos;
				try {
					eos = Integer.parseInt(newValue.toString());
				} catch (Exception e) {
					showTip("无效输入！");
					return false;
				}
				if (eos < 0 || eos > 30000) {
					showTip("取值范围为0~30000");
					return false;
				}
				
				mVadEosPref.setSummary("当前：" + eos + "ms");
				return true;
			}
		});
		
		mSpeechTimeoutPref.getEditText().setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_CLASS_NUMBER);
		mSpeechTimeoutPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int speech_timeout;
				try {
					speech_timeout = Integer.parseInt(newValue.toString());
				} catch (Exception e) {
					showTip("无效输入！");
					return false;
				}
				 
				if (speech_timeout < -1) {
					showTip("必须大于等于-1");
					return false;
				}
				
				if (speech_timeout == -1) {
					mSpeechTimeoutPref.setSummary("当前：-1");
				} else {
					mSpeechTimeoutPref.setSummary("当前：" + speech_timeout + "ms");
				}
				
				return true;
			}
		});
	}
	
	private void showTip(String str) {
		if(!TextUtils.isEmpty(str)) {
			mToast.setText(str);
			mToast.show();
		}
	}
}
