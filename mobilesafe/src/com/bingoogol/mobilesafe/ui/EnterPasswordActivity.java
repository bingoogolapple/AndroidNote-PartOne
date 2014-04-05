package com.bingoogol.mobilesafe.ui;

import com.bingoogol.mobilesafe.IService;
import com.bingoogol.mobilesafe.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bingoogol.mobilesafe.service.WatchDogService;

public class EnterPasswordActivity extends Activity {
	private TextView tv_name;
	private ImageView iv_icon;
	
	private EditText et_password;
	
	
	private String packname;
	
	private MyConn conn;
	
	private IService iService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_password);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		et_password = (EditText) findViewById(R.id.et_password);
		//获取到激活当前activity的意图
		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		
		try {
			ApplicationInfo appInfo  =  getPackageManager().getApplicationInfo(packname, 0);
			String appname = appInfo.loadLabel(getPackageManager()).toString();
			tv_name.setText(appname);
			iv_icon.setImageDrawable(appInfo.loadIcon(getPackageManager()));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		//绑定服务
		Intent service = new Intent(this,WatchDogService.class);
		conn = new MyConn();
		bindService(service, conn, BIND_AUTO_CREATE);
		
		
	}
	
	
	private class MyConn implements ServiceConnection{

		//当服务被成功绑定的时候调用.
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iService = (IService) service;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
	}
	
	@Override
	protected void onDestroy() {
		//activity销毁的时候 调用的方法
		//取消绑定服务
		unbindService(conn);
		conn = null;
		super.onDestroy();
	}
	
	public void click(View view){
		String password = et_password.getText().toString().trim();
		
		if("123".equals(password)){
			//密码正确 
			finish();//关闭当前输入密码的activity;
			//TODO: 告诉看门狗  自己的应用 不需要保护了 密码正确
			//后台的一个服务.
			//前台的一个界面.
			//发送一个自定义的广播
		/*	Intent intent = new Intent();
			intent.setAction("com.bingoogol.stopprotect");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);*/
			//采用绑定服务的方法 调用服务里面的方法  临时的停止对某个应用程序的保护.
			iService.callTempStopProtect(packname);
			
		}else{
			Toast.makeText(this, "密码不正确", 0).show();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){//返回键被按下了.
			//回桌面.
//            <action android:name="android.intent.action.MAIN" />
//            <category android:name="android.intent.category.HOME" />
//            <category android:name="android.intent.category.DEFAULT" />
//            <category android:name="android.intent.category.MONKEY"/>
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory("android.intent.category.HOME");//回桌面
			intent.addCategory("android.intent.category.DEFAULT");//默认的附加动作
			intent.addCategory("android.intent.category.MONKEY");
			startActivity(intent);
			finish();
			return true;//把事件给屏蔽掉.
		}	
		return super.onKeyDown(keyCode, event);
	}

}
