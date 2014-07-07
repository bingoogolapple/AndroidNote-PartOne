package cn.bingoogol.bingo.util;

import android.widget.Toast;
import cn.bingoogol.bingo.App;

/**
 * 吐司工具类
 * 
 * @author bingoogol@sina.com 2014-6-20
 */
public class ToastUtil {

	private ToastUtil() {
	}

	public static void makeText(CharSequence text) {
		if (text.length() < 10) {
			Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
		}
	}

	public static void makeText(int resId) {
		makeText(App.getInstance().getResources().getString(resId));
	}
}
