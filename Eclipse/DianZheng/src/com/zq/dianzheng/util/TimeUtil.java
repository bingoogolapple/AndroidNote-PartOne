package com.zq.dianzheng.util;

import java.util.Calendar;

public class TimeUtil {
	public static String millis2String(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DATE);
		int hrs = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		return year + "-" + month + "-" + date + " " + hrs + ":" + min;
	}
}
