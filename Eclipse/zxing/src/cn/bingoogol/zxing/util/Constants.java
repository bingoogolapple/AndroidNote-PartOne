package cn.bingoogol.zxing.util;

import java.io.File;

/**
 * 系统常量工具类
 * 
 * @author bingoogol@sina.com 2014-4-25
 */
public final class Constants {

	public static final String APP_NAME = "zxing";

	public static final class config {
		public static final String UPGRADE_URL = "";
	}

	public static final class mime {
		public static final String APK = "application/vnd.android.package-archive";
	}

	public static final class file {
		public static final String NEW_APK_NAME = APP_NAME + ".apk";
		public static final String DIR_ROOT = APP_NAME;
		public static final String DIR_DOWNLOAD = DIR_ROOT + File.separator + "download";
		public static final String DIR_FEEDBACK = DIR_ROOT + File.separator + "feedback";
	}

	public static final class net {
		public static final int CONNECT_TIMEOUT = 3000;
		public static final int READ_TIMEOUT = 3000;
	}

}