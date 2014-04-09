package com.bingoogol.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.bingoogol.mobilesafe.util.Logger;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG, "手机启动完毕");
        // TODO 把拦截短信的服务开启

        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String realSimSeriaNumber = tm.getSimSerialNumber();

            String bindSimSeriaNumber = sp.getString("simSeriaNumber", "");
            if (!realSimSeriaNumber.equals(bindSimSeriaNumber)) {
                SmsManager smsManager = SmsManager.getDefault();
                String safenumber = sp.getString("safenumber", "");
                smsManager.sendTextMessage(safenumber, null, "sim changed!", null, null);
            }

        }
//        从sp中获取某服务的状态，在此处开启或关闭服务
    }
}
