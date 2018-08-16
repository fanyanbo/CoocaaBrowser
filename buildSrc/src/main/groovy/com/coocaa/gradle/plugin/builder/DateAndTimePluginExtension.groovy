package com.coocaa.gradle.plugin.builder;

/**
 * Created by C0der on 2017/6/17.
 */

public class DateAndTimePluginExtension {
    private static final String TIME_FORMAT = "MM/dd/yyyyHH:mm:ss.SSS";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    String timeFormat = TIME_FORMAT;
    String dateFormat = DATE_FORMAT;
}
