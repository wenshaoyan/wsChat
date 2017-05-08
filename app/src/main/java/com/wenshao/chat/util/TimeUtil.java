package com.wenshao.chat.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wenshao on 2017/5/7.
 * 时间显示类
 */

public class TimeUtil {

    /**
     * 时间戳格式转换
     */
    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


    public static String getNewChatShortTime(long time) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(time);

        String timeFormat;
        String yearTimeFormat;
        timeFormat = "M月d日";
        yearTimeFormat = "yyyy年";
        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getFormat(time, "HH:mm");
                        break;
                    case 1:
                        result = "昨天 ";
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getFormat(time, "HH:mm");
                            } else {
                                result = getFormat(time, timeFormat);
                            }
                        } else {
                            result = getFormat(time, timeFormat);
                        }
                        break;
                    default:
                        result = getFormat(time, timeFormat);
                        break;
                }
            } else {
                result = getFormat(time, timeFormat);
            }
        } else {
            result = getFormat(time, yearTimeFormat);
        }
        return result;
    }

    public static String getNewChatTime(long time) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(time);

        String timeFormat;
        String yearTimeFormat;
        /*String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上";
        } else if (hour == 12) {
            am_pm = "中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午";
        } else if (hour >= 18) {
            am_pm = "晚上";
        }*/
        timeFormat = "M月d日 " + "HH:mm";
        yearTimeFormat = "yyyy年M月d日 " + "HH:mm";
        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getFormat(time, "HH:mm");
                        break;
                    case 1:
                        result = "昨天 " + getFormat(time, "HH:mm");
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getFormat(time, "HH:mm");
                            } else {
                                result = getFormat(time, timeFormat);
                            }
                        } else {
                            result = getFormat(time, timeFormat);
                        }
                        break;
                    default:
                        result = getFormat(time, timeFormat);
                        break;
                }
            } else {
                result = getFormat(time, timeFormat);
            }
        } else {
            result = getFormat(time, yearTimeFormat);
        }
        return result;
    }


    private static String getFormat(long time, String timeFormat) {
        return new SimpleDateFormat(timeFormat, Locale.getDefault()).format(new Date(time));
    }
}
