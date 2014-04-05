package com.bingoogol.mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import com.bingoogol.mobilesafe.service.UpdateWidgetService;


public class MyWidget extends AppWidgetProvider {
	private static Intent intent;

	@Override
	public void onReceive(Context context, Intent intent) {
		//注意: 广播接受者活的时间非常短的 只要onreceive方法执行完毕了.
		//广播接受者就会被系统回收.
		super.onReceive(context, intent);
	}
	
	@Override
	public void onEnabled(Context context) {
		intent = new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		//Intent intent = new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	

	@Override
	public void onDisabled(Context context) {
		//Intent intent = new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	}

}
