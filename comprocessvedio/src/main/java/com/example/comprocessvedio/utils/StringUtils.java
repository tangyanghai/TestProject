package com.example.comprocessvedio.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\" +
            ".\\w+([-.]\\w+)*");
    private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final static SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("MM月d日");
    private final static SimpleDateFormat dateFormater3 = new SimpleDateFormat("HH时mm分ss秒");

    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        String times = formatter.format(new Date(System.currentTimeMillis()));
        return times;
    }

    public static String getCurrentTime(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime6(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime2(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime3(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTimeSaveData(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime5(Long l) {
        if (null == l)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime8(Long l) {
        if (null == l)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd号");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime7(Long l) {
        if (null == l)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime9(Long l) {
        if (null == l)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd号");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String getCurrentTime10(int l) {
        if (l < 0)
            return "";
        if (l > 60 * 60) {
            return l / 60 / 60 + "小时后截止";
        } else {
            if (l <= 60) {
                return l + "秒后截止";
            } else {
                return l / 60 + "分钟后截止";

            }
        }
    }

    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return "(" + weekOfDays[w] + ")";
    }

    public static String getCurrentTime4(Long l) {
        String times = dateFormater3.format(new Date(l));
        return times;
    }

    public static String getOrderDealTime(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd  HH:mm");
        String times = formatter.format(new Date(l));
        return times;
    }

    public static String _getOrderDealTime(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd号  HH:mm");
        String times = formatter.format(new Date(l));
        return times;
    }


    public static String getOrderRefundTime(Long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        SimpleDateFormat formatter = new SimpleDateFormat("dd天HH时mm分");
        String times = formatter.format(calendar.getTime());
        return times;
    }

    public static String getDBJLBTime(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH小时mm分");
        String times = formatter.format(new Date(l));
        return times;
    }


    public static String getCurrentDate(Long l) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String times = formatter.format(new Date(l));
        return times;
    }


    public static String getTodayString() {
        Calendar calendar = Calendar.getInstance();
        final String str = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        return str;
    }


    public static String deadline(long date) {

        Date time = new Date(date);
        System.out.println(time);
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        Long T1 = new Date().getTime();
        Long T2 = time.getTime();

        System.out.println("T2" + T2 / 86400000);
        System.out.println("T1" + T1 / 86400000);
        int day = (int) ((T2 - T1) / 86400000);
        int hour = (int) ((T2 - T1) / 3600000) % 24;
        int minute = (int) ((T2 - T1) / 60000) % 60;
        ftime = day + "天" + hour + "小时" + minute + "分";

        return ftime;

    }

    /**
     * 以友好的方式显示时间
     *
     * @param date
     * @return
     */
    public static String friendly_time(long date) {
        // Date time = toDate(sdate);
//        Calendar instance = Calendar.getInstance();
//        instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        instance.setTimeInMillis(date);
//        Date time = instance.getTime();

        Date date1 = new Date(date);
        System.out.println(date1);
        // if(time == null) {
        // return "Unknown";
        // }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String curDate = dateFormater.format(cal.getTime());
        String paramDate = dateFormater.format(date1);
        Date time = new Date(paramDate.replace("-", "/"));//2017年10月30日17:12:29   有的手机是GMT+12  不是北京时间GMT+8 需要转一下才能使用

        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater.format(time);
        }
        return ftime;
    }


    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String priceToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }


    public static String amountFormat(float num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }


    /**
     * 判断给定字符串时间是否为今日
     *
     * @param l
     * @return boolean
     */
    public static boolean isToday(long l) {
        boolean b = false;
        String nowDate = getDate(getNow());
        String timeDate = getDate(l);
        if (nowDate.equals(timeDate)) {
            b = true;
        }
        return b;
    }


    public static boolean isTomorrow(long l) {
        Date d1 = new Date();
        Date d2 = new Date(l);
        double diff = d2.getTime() - d1.getTime();
        double days = diff / (1000 * 60 * 60 * 24);
        //		if(days<0){
        //			tvDateTip.setText("(今天)");
        //		}else if(days>=0&&days<1){
        //			tvDateTip.setText("(明天)");
        //		}else if(days>=1&&days<2){
        //			tvDateTip.setText("(后天)");
        //		}else{
        //			tvDateTip.setText("");
        //		}
        return days >= 0 && days < 1;
    }

    // /**
    // * 返回long类型的今天的日期
    // *
    // * @return
    // */
    // public static long getToday() {
    // Calendar cal = Calendar.getInstance();
    // String curDate = dateFormater2.get().format(cal.getTime());
    // curDate = curDate.replace("-", "");
    // return Long.parseLong(curDate);
    // }

    // String[] timeStrings

    public static String[] getDateStrings() {
        String[] strings = new String[15];
        for (int i = 0; i < strings.length; i++) {
            Long l = getNow() + (86400000L * i);
            strings[i] = dateFormater1.format(l);
        }
        return strings;
    }

    public static String getDateString(Long l) {

        return dateFormater1.format(l);
    }

    public static String getDateTimeString(Long l) {

        return dateFormater.format(l);
    }

    public static String getRemainTimeString(long date) {
        long day = date / (1000 * 60 * 60 * 24);
        long hour = (date / (1000 * 60 * 60) - day * 24);
        long min = ((date / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (date / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//        System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
        return hour + "小时" + min + "分" + s + "秒";
    }

    public static String getRemainTimeString2(long date) {
        long day = date / (1000 * 60 * 60 * 24);
        long hour = (date / (1000 * 60 * 60) - day * 24);
        long min = ((date / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (date / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
    }


    public static ArrayList<String> getOrderAppointDateStrings() {
        //		String[] strings = new String[15];


        ArrayList<String> strings = new ArrayList<String>();

        int length = 40;
        for (int i = 0; i < length; i++) {
            Long l = getNow() + (86400000L * i);


            //			strings[i] = dateFormater2.format(l) + getWeekOfDate(l);
            strings.add(dateFormater2.format(l) + getWeekOfDate(l));

        }
        return strings;
    }

    public static ArrayList<Long> getOrderAppointTimestamps(int length) {

        ArrayList<Long> strings = new ArrayList<Long>();
        Long time = getNow();
        for (int i = 0; i < length; i++) {
            Long l = time + (86400000L * i);

            strings.add(l);


        }

        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        return strings;
    }


    public static String getMonthStr(Long l) {

        return new SimpleDateFormat("yyyy年MM月").format(l);
    }


    public static ArrayList<String> getOrderAppointDateStrings(ArrayList<Long> arrayList) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < arrayList.size(); i++) {
            Long l = arrayList.get(i);

            if (isToday(l)) {
                strings.add("今天" + " (" + getWeekOfDate(l) + ")");
            } else if (isTomorrow(l)) {
                strings.add("明天" + " (" + getWeekOfDate(l) + ")");
            } else {
                strings.add(dateFormater2.format(l) + " (" + getWeekOfDate(l) + ")");
            }


        }

        return strings;
    }


    public static String showFriendlyInfo(String str) {
        String showStr;
        if (StringUtils.isEmpty(str)) {
            showStr = "- -";
        } else {
            showStr = str;
        }

        return showStr;
    }


    /**
     * 友好显示质量分数
     *
     * @param score
     * @return
     */
    public static String showFriendlyQualityScore(int score) {
        if (100 == score) {
            return "极佳";
        } else if (score < 100 && score >= 80) {
            return "优秀";
        } else if (score < 80 && score >= 60) {
            return "良好";
        } else if (score < 60 && score > 0) {
            return "较差";
        } else {
            return "极差";
        }

    }

    /**
     * 传入质量分数 返回不同颜色，不适用R.color 因为context可能为空
     *
     * @param score
     * @return
     */
    public static int showQualityScoreColor(int score) {
        if (100 == score) {
            return Color.parseColor("#2cbc41");
        } else if (score < 100 && score >= 80) {
            return Color.parseColor("#2cbc41");
        } else if (score < 80 && score >= 60) {
            return Color.parseColor("#2cbc41");
        } else if (score < 60 && score > 0) {
            return Color.parseColor("#fd9118");
        } else {
            return Color.parseColor("#ff4b4b");
        }

    }

    public static String refundState2String(int i) {

        if (1 == i) {
            return "退款申请中";
        } else if (2 == i) {
            return "不同意部分退款";
        } else if (3 == i) {
            return "同意部分退款";
        } else if (4 == i) {
            return "同意全额退款";
        } else if (5 == i) {
            return "退款仲裁中";
        } else if (6 == i) {
            return "客服不支持退款";
        } else if (7 == i) {
            return "客服支持退款";
        } else if (8 == i) {
            return "客服支持全额退款";
        } else if (9 == i) {
            return "下单方已取消退款";
        } else if (10 == i) {
            return "同意部分退款（系统默认）";
        } else if (11 == i) {
            return "同意全额退款（系统默认）";
        } else if (12 == i) {
            return "不同意全额退款";
        } else {
            return "";
        }

    }

    public static String showFriendlyWeek(String week) {

        if ("N1".equals(week)) {
            return "星期一";
        } else if ("N2".equals(week)) {
            return "星期二";
        } else if ("N3".equals(week)) {
            return "星期三";
        } else if ("N4".equals(week)) {
            return "星期四";
        } else if ("N5".equals(week)) {
            return "星期五";
        } else if ("N6".equals(week)) {
            return "星期六";
        } else if ("N7".equals(week)) {
            return "星期日";
        } else {
            return "";
        }
    }

    public static String getWeekStr(String weekStr) {

        if ("星期一".equals(weekStr)) {
            return "N1";
        } else if ("星期二".equals(weekStr)) {
            return "N2";
        } else if ("星期三".equals(weekStr)) {
            return "N3";
        } else if ("星期四".equals(weekStr)) {
            return "N4";
        } else if ("星期五".equals(weekStr)) {
            return "N5";
        } else if ("星期六".equals(weekStr)) {
            return "N6";
        } else if ("星期日".equals(weekStr)) {
            return "N7";
        } else {
            return "";
        }
    }


    public static String showFriendlyDealType(String dealType) {// //交易类型（order:订单,redpacket:红包,withdraw:提现,invite:邀请,certify_wallet:保证金，penalty:违约金,shop:购物

        if (dealType.equals("order")) {
            return "订单";
        } else if (dealType.equals("redpacket")) {
            return "红包";
        } else if (dealType.equals("withdraw")) {
            return "提现";
        } else if (dealType.equals("withdraw_fee")) {
            return "提现";
        } else if (dealType.equals("invite")) {
            return "百万";
        } else if (dealType.equals("certify")) {
            return "保证";
        } else if (dealType.equals("penalty")) {
            return "违约";
        } else if (dealType.equals("shop")) {
            return "购物";
        } else {
            return "";
        }
    }

    public static String withDrawStatus(String status) {
        if (status.equals("apply")) {
            return "提现申请中";
        } else if (status.equals("fail")) {
            return "提现失败";
        } else if (status.equals("paid")) {
            return "银行处理中";
        } else if (status.equals("finish")) {
            return "提现成功";
        } else {
            return "";
        }
    }


    public static String showEvaluateState(String type) {
        if (type.equals("good")) {
            return "好评";
        } else if (type.equals("normal")) {
            return "中评";
        } else if (type.equals("bad")) {
            return "差评";
        } else {
            return "";
        }
    }


//    (offer:报价 hire:雇佣 pay:付款 appoint:指派 cancel:取消 refund:退款
    // arbitrate:仲裁 complaint:投诉 redpacket:红包 shop:商城 grade:等级 质量分:qualityscore)





    public static String showFriendCHChar(int i) {
        String[] ChChas = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
                "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十"};
        return ChChas[i];


    }


    public static String getWeekOfDate(Long l) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        //        cal.setTime(dt);
        cal.setTimeInMillis(l);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    // 获得当前时间的毫秒表示

    public static long getNow() {
        return Calendar.getInstance().getTimeInMillis();

    }

    public static String getDate(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        return DateFormat.getDateInstance().format(cal.getTime());

    }

    public static int getTime(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        return cal.get(Calendar.HOUR_OF_DAY);

    }

    public static Boolean isListEmpty(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!"".equals(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否有空格 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmptySpacing(String input) {
        if (input.contains(" ")) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }


    public static String join(String join, List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == (list.size() - 1)) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i)).append(join);
            }
        }

        return new String(sb);
    }

    /**
     * 将字符串转数组
     *
     * @param s [123,123,123]
     * @return
     */
    public static ArrayList<String> String2Array(String s) {

        if (s.contains("[")) {
            s = s.replace("[", "");
        }
        if (s.contains("]")) {
            s = s.replace("]", "");
        }


        ArrayList<String> arrayList = new ArrayList<String>();
        String[] stringArr = s.split(","); // 注意分隔符是需要转译滴...
        List<String> list = Arrays.asList(stringArr);
        for (String string2 : list) {
            arrayList.add(string2);
        }

        return arrayList;
    }


    public static SpannableStringBuilder changeChildStringColor(String textAll, String text, int
            color) {

        int bstart = textAll.indexOf(text);
        int bend = bstart + text.length();
        SpannableStringBuilder style = new SpannableStringBuilder(textAll);
        style.setSpan(new ForegroundColorSpan(color), bstart, bend, Spannable
                .SPAN_EXCLUSIVE_EXCLUSIVE);

        return style;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second)
                        + "秒";
            }
        }
        return timeStr;
    }

    public static String secToTimeNoSecond(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                timeStr = unitFormat(minute) + "分";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分";
            }
        }
        return timeStr;
    }

    public static String secToTimehasDayNoSecond(int time) {
        String timeStr = null;
        int day = 0;
        int hour = 0;
        int minute = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                timeStr = unitFormat(minute) + "分";
            } else {
                hour = minute / 60;
                if (hour > 23) {
                    day = hour / 24;
                    hour = (time - day * 24 * 60 * 60) / 60 / 60;
                    minute = (time - day * 24 * 60 * 60 - hour * 60 * 60) / 60;
                    timeStr = unitFormat(day) + "天" + hour + "时" + minute + "分";
                } else {
                    minute = minute % 60;
                    timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分";
                }
            }
        }
        return timeStr;
    }

    public static String formatSeconds(long seconds) {
        String timeStr = "";
        if (seconds > 60) {
            long min = seconds / 60;
            timeStr = min + "分";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "时" + min + "分";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "时" + min + "分";
                }
            }
        }
        return timeStr;
    }

    public static String formatSecondsSign(long seconds) {
        String timeStr = "";
        if (seconds > 60) {
            long min = seconds / 60;
            timeStr = min + "分";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分";
                }
            }
        }
        return timeStr;
    }

    public static String formatSecondshassecond(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }






    public static int showDayState(long date) {

        Date time = new Date(date);
        if (time == null) {
            return 0;
        }

        int hourSP = 0;
        DateFormat df = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(df.format(date));


        if (hour >= 5 && hour < 8) {
            hourSP = 5;
        } else if (hour >= 8 && hour < 11) {
            hourSP = 8;
        } else if (hour >= 11 && hour < 13) {
            hourSP = 11;
        } else if (hour >= 13 && hour < 18) {
            hourSP = 13;
        } else if (hour >= 18 && hour < 24) {
            hourSP = 18;
        } else if (hour >= 0 && hour < 5) {
            hourSP = 24;
        }


        return hourSP;
    }


}
