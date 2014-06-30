package cn.bingoogol.screenexpert.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.bingoogol.screenexpert.App;

/**
 * SharedPreferences工具类，使用前必须先初始化，最好在Application的onCreate方法里初始化
 * 
 * @author bingoogol@sina.com 2014-4-25
 */
public class SpUtil {

	private static SharedPreferences mSharedPreferences;

	private SpUtil() {
	}

	public static void init() {
		mSharedPreferences = App.getInstance().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
	}

	public static void putString(String key, String value) {
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	public static void putInt(String key, int value) {
		Editor editor = mSharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	public static void putBoolean(String key, boolean value) {
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public static void setOnekeyPosition(int x, int y) {
		putInt("onekey_x", x);
		putInt("onekey_y", y);
	}

	public static int getOnekeyX() {
		return getInt("onekey_x", 100);
	}

	public static int getOnekeyY() {
		return getInt("onekey_y", 100);
	}

	public static void putShakeUnlockScreen(boolean value) {
		putBoolean("shake_unlock_screen", value);
	}

	public static boolean getShakeUnlockScreen() {
		return getBoolean("shake_unlock_screen", false);
	}

	public static void putOnekeyLockScreen(boolean value) {
		putBoolean("onekey_lock_screen", value);
	}

	public static boolean getOnekeyLockScreen() {
		return getBoolean("onekey_lock_screen", false);
	}

	public static void putIsShowAd(boolean value) {
		putBoolean("is_show_ad", value);
	}

	public static boolean getIsShowAd() {
		return getBoolean("is_show_ad", true);
	}
}
