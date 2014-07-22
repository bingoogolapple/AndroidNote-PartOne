package cn.bingoogol.viewpager.util;

import cn.bingoogol.viewpager.App;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * 网络操作工具类
 * 
 * @author bingoogol@sina.com
 * @creation 2014-7-22
 */
public class NetUtil {
	private static final String TAG = NetUtil.class.getSimpleName();;

	private NetUtil() {
	}

	/**
	 * 判断当前是否已链接到wifi
	 * 
	 * @return 如果已链接到wifi则返回true，否则返回false
	 */
	public static boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * 获取ip前缀
	 * 
	 * @return
	 */
	public static String getLocalIpAddressPrefix() {
		String ipAddress = getLocalIpAddress();
		Logger.i(TAG, "ipAddress:" + ipAddress);
		return ipAddress.substring(0, ipAddress.lastIndexOf(".") + 1);
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getLocalIpAddress() {
		WifiInfo wifiInfo = ((WifiManager) App.getInstance().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
		return Formatter.formatIpAddress(wifiInfo.getIpAddress());
	}
}