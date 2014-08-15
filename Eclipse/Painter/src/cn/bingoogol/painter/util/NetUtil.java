package cn.bingoogol.painter.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class NetUtil {

	private NetUtil() {
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getLocalIpAddress(Context context) {
		WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
		return Formatter.formatIpAddress(wifiInfo.getIpAddress());
	}
}