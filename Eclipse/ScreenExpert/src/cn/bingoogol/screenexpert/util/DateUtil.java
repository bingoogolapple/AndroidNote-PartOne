package cn.bingoogol.screenexpert.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private DateUtil() {
	}

	public static String dateToDayString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String dateToSecondsString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
	}
}
