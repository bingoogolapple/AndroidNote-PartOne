package com.bingoogol.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import com.bingoogol.mobilesafe.db.dao.AddressDao;
import com.bingoogol.mobilesafe.ui.LostFindActivity;
import com.bingoogol.mobilesafe.util.Logger;
import com.bingoogol.mobilesafe.util.ToastUtil;

/**
 * 主要用于通过拨打指定的号码开启某个activity
 * @author bingoogol@sina.com 14-2-12.
 */
public class OutCallReceiver extends BroadcastReceiver {
    private static String TAG = "OutCallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String number = getResultData();
        Logger.i(TAG,number);
        if("8888".equals(number)) {
            Intent lostFindIntent = new Intent(context, LostFindActivity.class);
            //activity是运行在在自己的任务栈中的，广播接收者或者服务里面没有任务栈，必须显示的指定flag,指定要激活的activity在自己的任务栈里面运行
            lostFindIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lostFindIntent);
            //终止掉这个电话
            //外拨电话的广播显示的指明了广播接收者，不能通过 abortBroadcast()种植广播
            setResultData(null);
        }
    }
}
