package com.bingoogol.mobilesafe.util;

import android.os.Environment;

import java.io.File;

/**
 * 文件操作工具类
 *
 * @author bingoogol@sina.com 14-1-25.
 */
public class StorageUtil {
    private StorageUtil() {
    }

    /**
     * 判断外部存储是否可写
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断外存储是否可读
     *
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * 获取当前app文件存储路径
     *
     * @return
     */
    public static File getAppDir() {
        File appDir = new File(Environment.getExternalStorageDirectory(), Constants.file.DIR_ROOT);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }
}
