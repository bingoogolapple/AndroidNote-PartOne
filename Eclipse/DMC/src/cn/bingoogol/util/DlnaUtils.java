package cn.bingoogol.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import cn.bingoogol.dmc.App;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class DlnaUtils {

	public static String creat12BitUUID() {
		String defaultUUID = "123456789abc";
		String mac = CommonUtil.getLocalMacAddress();
		mac = mac.replace(":", "");
		mac = mac.replace(".", "");
		if (mac.length() != 12) {
			mac = defaultUUID;
		}
		mac += "-dms";
		return mac;
	}

	public static String getIPs() {
		String ipaddress = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress += inetAddress.getHostAddress().toString() + ";";
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ipaddress;
	}

	public static String getIP() {
		WifiManager wifiMan = (WifiManager) App.getInstance().getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiMan.getConnectionInfo();
		int ipAddress = info.getIpAddress();
		String ipString = "";
		if (ipAddress != 0) {
			ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
		}
		return ipString;
	}

}
