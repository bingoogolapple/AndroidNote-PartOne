package com.zq.dianzheng.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zq.dianzheng.ui.AlarmMessageActivity;

/**
 * 接收闹钟提醒的广播接收者
 * 
 * @author 郑强
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, AlarmMessageActivity.class);
		context.startActivity(intent);
	}

}
