package com.yanguangfu.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.yanguangfu.binder.aidl.AIDLActivity;
import com.yanguangfu.binder.aidl.AIDLService;
import com.yanguangfu.binder.aidl.Rect1;

/**
 *
 * @author 杨光福
 *
 */
public class MyService extends Service {

	private AIDLActivity callback;



	@Override
	public void onCreate() {
		Log.e("yangguangfu", "MyService.onCreate");
	}


	@Override
	public void onStart(Intent intent, int startId) {
		Log.e("yangguangfu", "MyService.onStart startId="+startId);
	}


	@Override
	public IBinder onBind(Intent t) {
		Log.e("yangguangfu", "MyService.onBind");
		return mBinder;
	}


	@Override
	public void onDestroy() {
		Log.e("yangguangfu", "MyService.onDestroy");
		super.onDestroy();
	}


	@Override
	public boolean onUnbind(Intent intent) {
		Log.e("yangguangfu", "MyService.onUnbind");
		return super.onUnbind(intent);
	}


	public void onRebind(Intent intent) {
		Log.e("yangguangfu", "MyService.onRebind");
		super.onRebind(intent);
	}

	private String getNames(){
		Log.e("yangguangfu", "MyService.getName");
		return "name from service";
	}

	private int getAges(){
		Log.e("yangguangfu", "MyService.getAge");
		return 24;
	}

	private final AIDLService.Stub mBinder = new AIDLService.Stub() {

		@Override
		public void invokCallBack() throws RemoteException {
			Log.e("yangguangfu", "MyService.AIDLService.invokCallBack");
			Rect1 rect = new Rect1();
			rect.bottom=-1;
			rect.left=-1;
			rect.right=1;
			rect.top=1;
			callback.performAction(rect);
		}


		@Override
		public void registerTestCall(AIDLActivity cb) throws RemoteException {
			Log.e("yangguangfu", "MyService.AIDLService.registerTestCall");
			callback = cb;
		}


		@Override
		public String getName() throws RemoteException {
			Log.e("yangguangfu", "MyService.AIDLService.getName");
			return getNames();
		}


		@Override
		public int getAge() throws RemoteException {
			Log.e("yangguangfu", "MyService.AIDLService.getAge");
			return getAges();
		}
	};
}
