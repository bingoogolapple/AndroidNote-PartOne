package cn.bingoogol.simples.util;

import cn.bingoogol.simples.App;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * wifi操作工具类
 * 
 * @author bingoogol@sina.com 2014-4-25
 */
public class ConnectivityUtil {
	private ConnectivityUtil() {
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
}