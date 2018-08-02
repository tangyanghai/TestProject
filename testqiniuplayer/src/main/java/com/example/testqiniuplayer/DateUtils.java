package com.example.testqiniuplayer;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <b>项目名：</b><span color=#FF5205>Carsland </span><br>
 * <b>文件名：</b><span color=#FF5205>DateUtils </span><br>
 * <b>创建者：</b><span color=#FF5205>wxr </span><br>
 * <b>创建时间：</b><span color=#FF5205>2017/10/11 10:55 </span><br>
 * <b>描述：日期转换</b>
 */
public final class DateUtils {

    private DateUtils() {}

    public static String format(long date) {
        //当前时间
        String nowDate  = format(System.currentTimeMillis(), "yyyy-MM-dd");
        String dateDate = format(date, "yyyy-MM-dd");

        long   nowTime  = System.currentTimeMillis();
        String nowYear  = format(nowTime, "yyyy");
        String dateYear = format(date, "yyyy");
        if (!nowYear.equals(dateYear)) {return format(date, "yyyy-MM-dd");}

        String nowDay  = format(nowTime, "MM-dd");
        String dateDay = format(date, "MM-dd");
        return nowDay.equals(dateDay) ?
               "今天" + format(date, "HH:mm") :
               format(date, "MM月dd日 HH:mm");
    }

    /**
     * 把毫秒值转换成特定格式(默认格式yyyy-MM-dd HH:mm:ss)
     * @param date 时间毫秒值
     * @param rule 要转换的格式(null值会返回默认格式)
     * @return 转换后的日期(转换失败会返回null)
     */
    public static String format(long date, String rule) {
        String dateStr = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(TextUtils.isEmpty(rule) ?
                                                           "yyyy-MM-dd HH:mm:ss" :
                                                           rule);
            dateStr = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 把日期转换成特定格式(默认格式yyyy-MM-dd HH:mm:ss)
     * @param date 日期对象
     * @param rule 要转换的格式(null值会返回默认格式)
     * @return 转换后的日期(转换失败会返回null)
     */
    public static String format(Date date, String rule) {
        String dateStr = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(TextUtils.isEmpty(rule) ?
                                                           "yyyy-MM-dd HH:mm:ss" :
                                                           rule);
            dateStr = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 日期转毫秒值
     * @param date 日期
     * @param rule 对应日期的格式
     * @return 毫秒(转换失败会返回0)
     */
    public static long format(String date, String rule) {
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(rule)) { return 0; }
        long time = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(rule);
            time = simpleDateFormat.parse(date).getTime();
        } catch (Exception e) {e.printStackTrace();}
        return time;
    }

    /**
     * 时长转换成  00:00格式
     */
    public static String formatMS(long duration){
        int var = (int) (duration / 1000.0f + 0.5f);
        int min = var / 60;
        int sec = var % 60;
        return String.format("%1$02d:%2$02d", min, sec);
    }
}
