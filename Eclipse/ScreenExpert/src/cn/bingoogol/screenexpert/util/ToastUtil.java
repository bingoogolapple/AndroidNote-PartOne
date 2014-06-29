package cn.bingoogol.screenexpert.util;

import android.widget.Toast;
import cn.bingoogol.screenexpert.App;

/**
 * 吐丝工具类
 * 
 * @author bingoogol@sina.com 2014-2-18
 */
public class ToastUtil {

	private ToastUtil() {
	}

	public static void makeText(CharSequence text) {
		if (text.length() > 10) {
			Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
		}
	}

	public static void makeText(int resId) {
		makeText(App.getInstance().getResources().getString(resId));
	}
}