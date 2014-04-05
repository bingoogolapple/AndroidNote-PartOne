package com.bingoogol.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AutoKillService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO 注册一个屏幕锁定的广播接受者
		//当发现屏幕锁定的时候 清除后台的进程
		//给用户一个白名单设置的界面.
		super.onCreate();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO 取消注册广播接受者
		super.onDestroy();
	}

}
