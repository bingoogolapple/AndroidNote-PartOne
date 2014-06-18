package com.bingoogol.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// TODO 用内部广播接收者，需要的时候才注册
public class PackageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String text = intent.getDataString();
		System.out.println(text);
	}

}
