package com.bingoogol.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.*;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.service.GPSService;
import com.bingoogol.mobilesafe.util.Logger;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG, "短信到来");
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody();
                // 还得再检查发件人号码
                if ("#*location*#".equals(body)) {
                    Logger.i(TAG, "返回手机位置");
                    Intent i = new Intent(context, GPSService.class);
                    context.startService(i);
                    abortBroadcast();
                    String lastLocation = sp.getString("last_location", "");
                    if (!TextUtils.isEmpty(lastLocation)) {
                        SmsManager sm = SmsManager.getDefault();
                        sm.sendTextMessage(sender, null, lastLocation, null, null);
                    }
                } else if ("#*alarm*#".equals(body)) {
                    Logger.i(TAG, "播放报警音乐");
                    abortBroadcast();
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setLooping(false);
                    player.setVolume(1.0f, 1.0f);
                    player.start();
                } else if ("#*wipedata*#".equals(body)) {
                    abortBroadcast();
                    Logger.i(TAG, "清除数据");
                    DevicePolicyManager dpm = (DevicePolicyManager) context
                            .getSystemService(Context.DEVICE_POLICY_SERVICE);
                    if (dpm.isAdminActive(new ComponentName(context, MyAdminReceiver.class))) {
                        dpm.wipeData(0);
                    }
                } else if ("#*lockscreen*#".equals(body)) {

                    Logger.i(TAG, "远程锁屏");
                    DevicePolicyManager dpm = (DevicePolicyManager) context
                            .getSystemService(Context.DEVICE_POLICY_SERVICE);
                    if (dpm.isAdminActive(new ComponentName(context,
                            MyAdminReceiver.class))) {
                        dpm.resetPassword("123", 0);
                        dpm.lockNow();
                    }
                    abortBroadcast();

                }

            }

        }

    }
}