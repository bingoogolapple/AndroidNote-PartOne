package com.bingoogol.netstate.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.bingoogol.netstate.util.ConnectivityUtil;
import com.bingoogol.netstate.util.ToastUtil;

import java.util.Currency;

/**
 * 监听网络状态变化的广播接收者
 *
 * @author bingoogol@sina.com 14-2-8.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnectivityChangeReceiver";
    private static final int NOTCONNECTED = -1;
    /**
     * 通过该标记解决网络变化时，广播通知会发送多次的问题
     */
    private static int lastType = NOTCONNECTED;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = ConnectivityUtil.getActiveNetworkInfo(context);
        if(networkInfo != null && networkInfo.isConnected()) {
            int currentType = networkInfo.getType();
            if(currentType != lastType) {
                Log.d(TAG,"已链接到新的网络");
                ToastUtil.makeText(context, "已链接到新的网络");
                lastType = currentType;
            }
        } else {
            if(lastType != ConnectivityChangeReceiver.NOTCONNECTED) {
                Log.d(TAG,"网络链接中断");
                ToastUtil.makeText(context, "网络链接中断");
                lastType = ConnectivityChangeReceiver.NOTCONNECTED;
            }
        }
    }
}
