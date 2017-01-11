package com.yanguangfu.binder;

import java.text.MessageFormat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.yanguangfu.binder.aidl.AIDLActivity;
import com.yanguangfu.binder.aidl.AIDLService;
import com.yanguangfu.binder.aidl.Rect1;

/**
 *
 * @author 杨光福
 *
 */
public class MainActivity extends Activity implements OnClickListener {
	private Button btn_bindService;
	private Button btn_unbindService;
	private Button btnCallBack;
	private Button btn_get_data;



	@Override
	public void onCreate(Bundle icicle) {
		Log.e("yangguangfu", "MainActivity.onCreate");
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);
		//初始化布局文件
		initView();
		//设置点击事件
		setClickListener();
	}

	/**
	 * 设置点击事件
	 */
	private void setClickListener() {
		btn_bindService.setOnClickListener(this);
		btn_unbindService.setOnClickListener(this);
		btnCallBack.setOnClickListener(this);
		btn_get_data.setOnClickListener(this);
	}

	/**
	 * 初始化布局文件
	 */
	private void initView() {
		btn_bindService = (Button) findViewById(R.id.btn_bindService);
		btn_unbindService = (Button) findViewById(R.id.btn_unbindService);
		btnCallBack = (Button) findViewById(R.id.btn_call_back);
		btn_get_data = (Button) findViewById(R.id.btn_get_data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_bindService:
				Log.e("yangguangfu", "MainActivity.点击了btn_bindService");

				Intent intent = new Intent(this,MyService.class);
				//绑定服务
				bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
				//启动服务
			    startService(intent);
				break;
			case R.id.btn_unbindService:
				Log.e("yangguangfu", "MainActivity.点击了btn_unbindService");
				unbindService(mConnection);
				// stopService(intent);
				break;
			case R.id.btn_call_back:

				Log.e("yangguangfu", "MainActivity.点击了btn_unbindService");
				try {
					mService.invokCallBack();
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				break;
			case R.id.btn_get_data:

				Log.e("yangguangfu", "MainActivity.点击了btn_get_data");
				if(mService==null){
					Toast.makeText(getApplicationContext(), "服务还没有绑定", 0).show();
					return ;
				}

				try {
					String dataFromService = mService.getName()+"---"+mService.getAge();
					Log.e("yangguangfu", "MainActivity.dataFromService=="+dataFromService);
					Toast.makeText(getApplicationContext(), dataFromService, 0).show();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;


			default:
				break;
		}

	}



	private AIDLActivity mCallback = new AIDLActivity.Stub() {

		@Override
		public void performAction(Rect1 rect) throws RemoteException {
			Log.e("yangguangfu", "MainActivity.performAction");
			Log.e("yangguangfu", MessageFormat.format(
					"MainActivity.rect[bottom={0},top={1},left={2},right={3}]", rect.bottom,
					rect.top, rect.left, rect.right));
			Toast.makeText(MainActivity.this,
					"这个土司是由Service回调Activity弹出来的", 1).show();

		}
	};

	private AIDLService mService;
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.e("yangguangfu", "MainActivity.onServiceConnected");
			mService = AIDLService.Stub.asInterface(service);
			try {
				mService.registerTestCall(mCallback);
			} catch (RemoteException e) {

			}
		}

		public void onServiceDisconnected(ComponentName className) {
			Log.e("yangguangfu", "MainActivity.onServiceDisconnected");
			mService = null;
		}
	};
}
