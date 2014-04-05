package com.bingoogol.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.receiver.MyWidget;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateWidgetService extends Service {
	private static final String TAG = "UpdateWidgetService";
	private Timer timer;
	private TimerTask task;
	private ActivityManager am;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		Log.i(TAG,"更新widget的服务开启了...");
		//开启一个定时器 定期更新widget
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				//更新widget
				AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
				ComponentName provider = new ComponentName(getApplicationContext(), MyWidget.class);
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "正在运行进程数量:"+am.getRunningAppProcesses().size());
				views.setTextViewText(R.id.process_memory, "剩余内存:"+getAvailMem());
				Intent intent = new Intent();
				intent.setAction("com.itheima.killallbgprocess");
				//定义一个延期的广播事件
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 1000, 1000);
		super.onCreate();
	}
	
	private String getAvailMem(){
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return Formatter.formatFileSize(this, outInfo.availMem);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG,"更新widget的服务关闭了...");
		timer.cancel();
		task.cancel();
		timer = null;
		task = null;
		super.onDestroy();
	}
}
