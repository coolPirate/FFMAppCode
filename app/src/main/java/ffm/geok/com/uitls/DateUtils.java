package ffm.geok.com.uitls;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zhanghs on 2017/3/1.
 *
 * 时间、日期处理工具类
 */

public class DateUtils {
    public static String pattern_day = "yyyy-MM-dd";
    public static String pattern_day_no = "yyyyMMdd";
    public static String pattern_timestamp = "yyyyMMddHHmmssSSS";
    public static String pattern_full = "yyyy-MM-dd HH:mm:ss";
    public static String pattern_24full = "YYYY-MM-DD HH24:MI:SS";
    /**
     * 将字符串转还成日期格式
     * @param source
     * @param template
     * @return
     */
    public static Date String2Date(String source, String template){
        if(source == null){
            return null;
        }
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(template);
        try {
            date = sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 将日期装换为String
     * @param date
     * @param template
     * @return
     */
    public static String Date2String(Date date,String template){
        String dateResult = "";
        SimpleDateFormat sdf = new SimpleDateFormat(template);
        if(date != null){
            dateResult = sdf.format(date);
        }
        return dateResult;
    }
    /**
     * 比较日期大小
     * @return
     */
    public static boolean dateCompare(String btime, String etime) {
        boolean result = false;
        try {
            Date bDate = String2Date(dateStringFormat(btime),pattern_day);
            Date eDate = String2Date(dateStringFormat(etime),pattern_day);
            //比较开始时间是否大于截止时间
            if (bDate.getTime() > eDate.getTime()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 日期字符串格式化 例 2016年9月7日 to 2016-09-07
     * @param btime
     * @return
     */
    public static String dateStringFormat(String btime) {
        String selectYear = btime.substring(0, btime.indexOf("年"));
        String selectMonth = btime.substring(btime.indexOf("年") + 1, btime.lastIndexOf("月"));
        if (Integer.parseInt(selectMonth) < 10) {
            selectMonth = "0" + selectMonth;
        }
        String selectDay = btime.substring(btime.lastIndexOf("月")+1, btime.lastIndexOf("日"));
        if (Integer.parseInt(selectDay) < 10) {
            selectDay = "0" + selectDay;

        }
        String formatString = selectYear + "-" + selectMonth + "-" + selectDay;
        return formatString;
    }
    /**
     * 日期字符串格式化 例 2016-09-07 to 2016年9月7日
     * @param btime
     * @return
     */
    public static String dateFormatYearMonth(String btime) {
        String formatString = "";
        if (!TextUtils.isEmpty(btime)) {
            String[] dataArray = btime.split("-");
            formatString = dataArray[0] + "年" + dataArray[1] + "月" + dataArray[2] + "日";
        }
        return formatString;
    }

    /**
     * 返回当前年份
     * @return
     */
    public static int getCurrentYear(){
        int month_ = 0;
        GregorianCalendar gc = new GregorianCalendar();
        month_ = gc.get(Calendar.YEAR);
        return month_;
    }
    /**
     * 返回当前月份
     * @return
     */
    public static int getCurrentMonth(){
        int month_ = 0;
        GregorianCalendar gc = new GregorianCalendar();
        month_ = gc.get(Calendar.MONTH)+1;
        return month_;
    }
    /**
     * 返指定时间Date的月份
     * @return
     */
    public static int getMonthByDate(Date date){
        int month_ = 0;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        month_ = gc.get(Calendar.MONTH)+1;
        return month_;
    }
    /**
     * 返回当前小时
     * @return
     */
    public static int getCurrentHour(){
        int month_ = 0;
        GregorianCalendar gc = new GregorianCalendar();
        month_ = gc.get(Calendar.HOUR_OF_DAY);
        return month_;
    }
    /**
     * 返回当前分
     * @return
     */
    public static int getCurrentMinute(){
        int month_ = 0;
        GregorianCalendar gc = new GregorianCalendar();
        month_ = gc.get(Calendar.MINUTE);
        return month_;
    }
    /**
     * 返回一个指定的时间
     * @return
     */
    public static Date getMyCurrentDate(int year,int month,int date){
        GregorianCalendar gc = new GregorianCalendar(year,month,date);
        return gc.getTime();
    }

    /**
     * 返回当前日
     * @return
     */
    public static int getMyCurrentDateOfMonth(){
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DATE);
    }
    /**
     * 返回当前是第几周
     * @return
     */
    public static int getMyCurrentWeekOfYear(){
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.WEEK_OF_YEAR);
    }
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
    public static String getDateStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.pattern_full);
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        }
        catch (ParseException e) {
            e.printStackTrace();
        } //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.pattern_full);
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //Day:日期字符串例如 2015-3-10 10:10:10  Num:需要减少的小时数例如 7
    public static String getDateHourStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.pattern_full);
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        }
        catch (ParseException e) {
            e.printStackTrace();
        } //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.pattern_full);
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //Day:日期字符串例如 2015-3-10 10:10:10  Num:需要减少的小时数例如 7
    public static String getDateMinStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.pattern_full);
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        }
        catch (ParseException e) {
            e.printStackTrace();
        } //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.pattern_full);
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }
}
