package com.bingoogol.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class KillBgProcessReceiver extends BroadcastReceiver {

	private static final String TAG = "BroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"我是自定义的清理进程的广播事件"); 
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo>  infos = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info : infos){
			am.killBackgroundProcesses(info.processName);   
		}
		Toast.makeText(context, "清理完毕", 0).show();
	}

}
