package com.skyworth.tv_browser.util;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class BrowserTools {

	public static String getDateStr(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		Formatter ft = new Formatter(Locale.CHINA);
		return ft.format("%1$tY.%1$tm.%1$td", cal).toString();
	}
	
}
