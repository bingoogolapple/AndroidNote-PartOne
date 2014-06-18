package com.bingoogol.mobilesafe.util;

import java.io.File;

/**
 * 系统常量工具类
 *
 * @author bingoogol@sina.com 14-2-8.
 */
public class Constants {

    public static final class file {
        public static final String DIR_ROOT = "mobilesafe";
        public static final String APK_PATH = StorageUtil.getAppDir().getAbsolutePath() + File.separator + DIR_ROOT + ".apk";
    }

    public static final class config {
        public static final String UPGRADE_URL = "http://192.168.10.123:8080/update.xml";
    }
}
