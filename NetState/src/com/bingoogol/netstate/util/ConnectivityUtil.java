package com.bingoogol.netstate.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络链接状态检查工具类
 *
 * @author bingoogol@sina.com 14-2-8.
 */
public class ConnectivityUtil {
    private ConnectivityUtil() {
    }

    /**
     * 判断当前是否已链接网络
     *
     * @param context 应用程序上下文
     * @rern 如果已链接网络则返回true，否则返回false
     */
    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = ConnectivityUtil.getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 判断当前是否已链接到wifi
     *
     * @param context 应用程序上下文
     * @return 如果已链接到wifi则返回true，否则返回false
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 获取当前已激活的网络链接信息
     *
     * @param context 应用程序上下文
     * @return 返回当前已激活的网络链接信息
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
}