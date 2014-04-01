package com.bingoogol.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.bingoogol.mobilesafe.IService;
import com.bingoogol.mobilesafe.db.dao.AppLockDao;
import com.bingoogol.mobilesafe.ui.EnterPasswordActivity;

public class WatchDogService extends Service {
	public static final String TAG = "WatchDogService";
	private boolean flag;
	private ActivityManager am;
	private AppLockDao dao;
	private InnerReceiver receiver;

	private ScreenLockReceiver screenLockReceiver;
	
	private ScreenUnlockReceiver screenUnlockReceiver;

	/**
	 * 临时停止保护的包名集合
	 */
	private List<String> tempStopProtectPackNames;

	/**
	 * 所有的要保护的应用程序的集合
	 */
	private List<String> protectPackNames;

	private ApplockObserver observer;

	/**
	 * 当服务被成功绑定的时候 调用的方法.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"服务被绑定了");
		return new MyBinder();
	}

	private class MyBinder extends Binder implements IService {
		@Override
		public void callTempStopProtect(String packname) {
			tempStopProtect(packname);
		}
		
	}
	
	/**
	 * 服务内部的方法  调用可以添加一个临时停止保护的包名
	 * @param packname
	 */
	private void tempStopProtect(String packname){
		Log.i(TAG,"我是服务里面的私有方法,我被调用了.");
		tempStopProtectPackNames.add(packname);
	}
	
	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String packname = intent.getStringExtra("packname");
			Log.i(TAG, "我是自定义的广播接受者,我接受到了消息,停止保护" + packname);
			tempStopProtectPackNames.add(packname);
		}

	}

	private class ScreenLockReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "屏幕锁屏了...");
			tempStopProtectPackNames.clear();
			flag = false;
		}
	}

	private class ScreenUnlockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (!flag) {
				Log.i(TAG, "屏幕解锁了...");
				startWatchDog();
			}
		}

	}

	private class ApplockObserver extends ContentObserver {

		public ApplockObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			Log.i(TAG, "发现数据库内容变化了");
			// 查询全部的锁定的应用程序
			protectPackNames = dao.findAll();
			super.onChange(selfChange);
		}
	}

	@Override
	public void onCreate() {
		tempStopProtectPackNames = new ArrayList<String>();
		dao = new AppLockDao(this);

		// 查询全部的锁定的应用程序
		protectPackNames = dao.findAll();
		observer = new ApplockObserver(new Handler());
		getContentResolver().registerContentObserver(AppLockDao.uri, true,
				observer);

		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.itheima.stopprotect");
		registerReceiver(receiver, filter);

		screenLockReceiver = new ScreenLockReceiver();
		IntentFilter lockFilter = new IntentFilter();
		lockFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenLockReceiver, lockFilter);
		
		
		screenUnlockReceiver = new ScreenUnlockReceiver();
		IntentFilter unlockFilter = new IntentFilter();
		unlockFilter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(screenUnlockReceiver, unlockFilter);

		// 开启看门狗 不停的监视当前手机正在运行的程序信息
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		startWatchDog();
		super.onCreate();
	}

	private void startWatchDog() {
		final Intent intent = new Intent(this, EnterPasswordActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		new Thread() {
			public void run() {
				flag = true;
				while (flag) {
					// 获取当前最近创建的任务栈
					RunningTaskInfo info = am.getRunningTasks(2).get(0);
					String packname = info.topActivity.getPackageName();
					// if (dao.find(packname)) {// 当前应用程序需要保护 TODO:改成查询内存
					if (protectPackNames.contains(packname)) { // 查询内存
						// 检查当前的应用程序是否需要临时的停止保护
						if (tempStopProtectPackNames.contains(packname)) {
							// 需要临时的停止保护. 什么事情都不做 不弹出界面.
						} else {
							// 看门狗跳出来, 弹出来一个输入密码的界面.让用户输入密码.
							intent.putExtra("packname", packname);
							startActivity(intent);
						}
					}
					System.out.println(packname);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(receiver);
		receiver = null;
		unregisterReceiver(screenLockReceiver);
		screenLockReceiver = null;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
		unregisterReceiver(screenUnlockReceiver);
		screenUnlockReceiver = null;
		super.onDestroy();
	}
}
