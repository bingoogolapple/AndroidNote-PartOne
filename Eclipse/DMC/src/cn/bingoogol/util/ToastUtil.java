package cn.bingoogol.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具类，使用前必须先初始化，最好在Application的onCreate方法里初始化
 * 
 * @author bingoogol@sina.com 2014-6-20
 */
public class ToastUtil {
	private static Context mContext;

	private ToastUtil() {
	}

	public static void init(Context context) {
		mContext = context;
	}

	public static void makeText(CharSequence text) {
		if (text.length() < 10) {
			Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
		}
	}

	public static void makeText(int resId) {
		makeText(mContext.getResources().getString(resId));
	}
}
