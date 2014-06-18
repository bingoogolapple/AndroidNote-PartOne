package com.bingoogol.mobilesafe.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐丝工具类
 *
 * @author bingoogol@sina.com 14-2-8.
 */
public class ToastUtil {
    private ToastUtil() {
    }

    public static void makeText(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void makeText(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
