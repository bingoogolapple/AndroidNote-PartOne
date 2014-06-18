package com.bingoogol.wifi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	
	private DateUtil() {
	}
	
	public static String date2String(Date date) {
		return dateFormat.format(date);
	}
}
