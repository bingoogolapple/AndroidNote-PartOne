package cn.bingoogol.screenexpert.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.bingoogol.screenexpert.service.ScreenService;
import cn.bingoogol.screenexpert.util.Logger;

public class BootCompleteReceiver extends BroadcastReceiver {
	private static final String TAG = BootCompleteReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.i(TAG, "手机重启完毕");
		context.startService(new Intent(context, ScreenService.class));
	}

}
