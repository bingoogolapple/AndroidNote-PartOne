package cn.bingoogol.viewpager.util;

import cn.bingoogol.viewpager.App;

/**
 * 屏幕适配工具类
 * 
 * @author bingoogol@sina.com 2014-4-23
 */
public class DensityUtil {
	private DensityUtil() {
	}

	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		final float scale = App.getInstance().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(float pxValue) {
		final float scale = App.getInstance().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}