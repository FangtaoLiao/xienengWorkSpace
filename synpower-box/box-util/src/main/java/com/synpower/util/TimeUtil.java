package com.synpower.util;

import com.synpower.constant.TimeEXP;
import com.synpower.lang.ServiceException;
import com.synpower.msg.Msg;
import org.apache.commons.lang.StringUtils;

import javax.sql.rowset.serial.SerialException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtil {

	// 这是线程不安全的 自己要用一样的也要new 并在方法中加synchronized
	private static SimpleDateFormat f3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 获取指定时间对应的毫秒数
     *
     * @param time "HH:mm:ss"
     * @return
     */
    public static long getTimeMillis(String time) {
        try {
            if (StringUtils.isBlank(time)) {
                return 0;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
                long millionSeconds = dateFormat.parse(time).getTime();// 毫秒
                return millionSeconds;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param date      当前时间
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @Title: isInDate
     * @Description: 判断一个时间是否在另外两个时间段之间
     * @return: boolean 在之间为true；不在为false
     * @lastEditor: SP0009
     * @lastEdit: 2017年10月23日上午11:34:20
     */
    public static boolean isInDate(Date date, String beginDate, String endDate) {
        Long currentTime = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Long benginTime = sdf.parse(beginDate).getTime();

            Long endTime = sdf.parse(endDate).getTime();

            if (currentTime >= benginTime && currentTime <= endTime) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;

    }

    /**
     * @param past 前几个月
     * @Title: getPastMon
     * @Description: 获取前几个月的月份
     * @return: String
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月16日上午10:31:07
     */
    public static String getPastMon(int past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -past);
        Date m = c.getTime();
        String mon = format.format(m);
        String[] mons = mon.split("-");
        return mons[1];
    }

    /**
     * @param past 前几个月
     * @Title: getPastMon
     * @Description: 获取前几个月的月份
     * @return: String
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月16日上午10:31:07
     */
    public static String getPastMonAndYear(int past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -past);
        Date m = c.getTime();
        String mon = format.format(m);
        String[] mons = mon.split("-");
        return mons[0] + "-" + mons[1];
    }

    public static String getPastYear(int past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        // 过去一月
        c.setTime(new Date());
        c.add(Calendar.YEAR, -past);
        Date m = c.getTime();
        String mon = format.format(m);
        String[] mons = mon.split("-");
        return mons[0];
    }

    /**
     * @param specifiedDay 具体一天的日期（如：2017-11-21）
     * @param past         前几天
     * @Title: getSpecifiedDayBefore
     * @Description: 输入具体某一天获取到前多少天的日期
     * @return: String 返回得到的日期
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月21日下午5:31:55
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int past) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - past);
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    public static String getSpecifiedMonBefore(String specifiedDay, int past) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, day - past);
        String dayBefore = new SimpleDateFormat("yyyy-MM").format(c.getTime());
        return dayBefore;
    }

    public static String getSpecifiedYearBefore(String specifiedYear, int past) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy").parse(specifiedYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.YEAR);
        c.set(Calendar.YEAR, day - past);
        String dayBefore = new SimpleDateFormat("yyyy").format(c.getTime());
        return dayBefore;
    }

    /**
     * @return 天数
     * @throws ParseException: long
     * @Title: getDistanceDay
     * @Description: 获取指定日期到当前日期的天数
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月21日下午6:06:49
     */
    public static long getDistanceDay(String dstr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse(dstr);
        long s1 = date.getTime();// 将时间转为毫秒
        long s2 = System.currentTimeMillis();// 得到当前的毫秒
        long day = (s2 - s1) / 1000 / 60 / 60 / 24;
        return day;
    }

    /**
     * @param date1 起始日期
     * @param date2 结束日期
     * @return
     * @throws ParseException: int
     * @Title: countMonths
     * @Description: 获取一个时间段之间的月数
     * @lastEditor: SP0007
     * @lastEdit: 2017年11月22日下午4:53:07
     */
    public static int countMonths(String date1, String date2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));

        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        // 开始日期若小月结束日期
        if (year < 0) {
            year = -year;
            return year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        }
        return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }

    /**
     * @param startTime 起始日期
     * @param endTime   结束日期
     * @return
     * @throws ParseException: long
     * @Title: getDistanceDay
     * @Description: 判断一个时间段之间相差多少天
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月22日下午2:26:08
     */
    public static long getDistanceDay(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date1 = sdf.parse(startTime);
        java.util.Date date2 = sdf.parse(endTime);
        long s1 = date1.getTime();// 将时间转为毫秒
        long s2 = date2.getTime();// 得到当前的毫秒
        long day = (s2 - s1) / 1000 / 60 / 60 / 24;
        return day;
    }

    /**
     * @param specifiedDay 具体一天的日期（如：2017-11-21）
     * @param past         后几天
     * @Title: getSpecifiedDayAfter
     * @Description: 输入具体某一天获取到后多少天的日期
     * @return: String
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月21日下午6:08:02
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int past) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + past);
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    public static String getSpecifiedMonAfter(String specifiedMon, int past) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM").parse(specifiedMon);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, day + past);
        String dayBefore = new SimpleDateFormat("yyyy-MM").format(c.getTime());
        return dayBefore;
    }

    public static String getSpecifiedYearAfter(String specifiedYear, int past) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy").parse(specifiedYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.YEAR);
        c.set(Calendar.YEAR, day + past);
        String dayBefore = new SimpleDateFormat("yyyy").format(c.getTime());
        return dayBefore;
    }

    /**
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @Title: getDatesBetweenTwoDate
     * @Description: 得到一个时间段之间的日期
     * @return: List<Date> 结果集合
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月22日下午3:46:47
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf.format(beginDate);
        String date2 = sdf.format(endDate);
        if (!date1.equals(date2)) {
            lDate.add(beginDate);// 把开始时间加入集合
        }
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }

    /**
     * @param date 输入的日期(2017-11-23)
     * @return 周几
     * @throws ParseException: String
     * @Title: getWeek
     * @Description: 输入一个日期返回时周几
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月23日上午11:28:11
     */
    public static String getWeek(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(date));
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String week = null;
        switch (i) {
            case 1:
                week = "周日";
                return week;
            case 2:
                week = "周一";
                return week;
            case 3:
                week = "周二";
                return week;
            case 4:
                week = "周三";
                return week;
            case 5:
                week = "周四";
                return week;
            case 6:
                week = "周五";
                return week;
            case 7:
                week = "周六";
                return week;
            default:
                return "日期输入有误";
        }
    }

    /**
     * @param beginDate 起始日期
     * @param endTime   结束日期
     * @Title: getDateLength
     * @Description: 获取一个时间段之间的年份（若年份相同，则返回0）
     * @return: int 相差几年
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月23日下午5:40:51
     */
    public static int getDateLength(String beginDate, String endTime) {
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(beginDate));
            c2.setTime(df.parse(endTime));
        } catch (java.text.ParseException e) {
            // XXX Auto-generated catch block
            System.err.println("日期格式不正确，应该是yyyyMMdd");
            e.printStackTrace();
        }
        int result = (c2.get(java.util.Calendar.YEAR) - c1.get(java.util.Calendar.YEAR)) < 0 ? 0
                : c2.get(java.util.Calendar.YEAR) - c1.get(java.util.Calendar.YEAR);
        return result;
    }

    /**
     * @param year  年
     * @param month 月
     * @Title: getMonthOfDay
     * @Description: 传入年和月，返回这个月有多少天
     * @return: int 有多少天
     * @lastEditor: SP0009
     * @lastEdit: 2017年11月24日下午5:33:22
     */
    public static int getMonthOfDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static long getBeforHour(String time, int hour) {
        long data = Long.valueOf(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        return calendar.getTimeInMillis();
    }

    public static long getAfterHour(String time, int hour) {
        long data = Long.valueOf(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        return calendar.getTimeInMillis();
    }

    /**
     * @param time
     * @Title: getNowTimeInTimes
     * @Description: TODO
     * @return: String
     * @lastEditor: SP0011
     * @lastEdit: 2017年12月11日下午6:28:08
     */
    public static String getNowTimeInTimes(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String[] times = StringUtils.split(sdf.format(time), ":");
        int min = Integer.parseInt(times[1]);
        int s = Integer.parseInt(times[2]);
        if (min % 5 != 0) {
            if (min % 5 >= 3) {
                min = min + (5 - min % 5);
            } else if (min % 5 == 2 && s > 30) {
                min = min + (5 - min % 5);
            } else {
                min = min - min % 5;
            }
        }
        return times[0] + ":" + (min < 10 ? "0" + min : min + "");
    }

    public static String getTimeFiveBefor(String startTime) {
        String nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
        String[] riseTimes = StringUtils.split(startTime, ":");
        int riseMin = Integer.parseInt(riseTimes[1]);
        if (riseMin % 5 != 0) {
            riseMin = riseMin - riseMin % 5;
        }
        startTime = riseTimes[0] + ":" + (riseMin < 10 ? "0" + riseMin : riseMin + "");
        return startTime;
    }

    public static String getTimeFiveEnd(String endTime) {
        String nowTime = Util.getCurrentTimeStr("yyyy-MM-dd");
        String[] setTimes = StringUtils.split(endTime, ":");
        int setMin = Integer.parseInt(setTimes[1]);
        if (setMin % 5 != 0) {
            setMin = setMin + (5 - setMin % 5);
        }
        endTime = setTimes[0] + ":" + (setMin < 10 ? "0" + setMin : setMin + "");
        return endTime;
    }

    public static String getCommonTimeFiveBefor(String startTime) throws SerialException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d H:m");
        String time = null;
        try {
            time = sdf.format(sdf.parse(startTime));
        } catch (Exception e) {
            throw new SerialException(Msg.TYPE_DATA_ERROR);
        }
        String[] riseTimes = StringUtils.split(time.split(" ")[1], ":");
        int riseMin = Integer.parseInt(riseTimes[1]);
        if (riseMin % 5 != 0) {
            riseMin = riseMin - riseMin % 5;
        }
        time = riseTimes[0] + ":" + riseMin;
        return time;
    }

    /**
     * @param endTime
     * @throws SerialException
     * @Title: getCommonTimeFiveEnd
     * @Description: 不要补0
     * @return: String
     * @lastEditor: SP0011
     * @lastEdit: 2017年12月15日上午11:50:51
     */
    public static String getCommonTimeFiveEnd(String endTime) throws SerialException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d H:m");
        String time = null;
        try {
            time = sdf.format(sdf.parse(endTime));
        } catch (Exception e) {
            throw new SerialException(Msg.TYPE_DATA_ERROR);
        }
        String[] setTimes = StringUtils.split(time.split(" ")[1], ":");
        int setMin = Integer.parseInt(setTimes[1]);
        if (setMin % 5 != 0) {
            setMin = setMin + (5 - setMin % 5);
        }
        time = setTimes[0] + ":" + setMin;
        return time;
    }

    public static String getTimeToLine(String time) throws ServiceException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String result = null;
        try {
            result = sdf.format(Long.valueOf(time));
        } catch (Exception e) {
            throw new ServiceException(Msg.TYPE_DATA_ERROR);
        }
        String[] setTimes = StringUtils.split(result, ":");
        int min = Integer.parseInt(setTimes[1]);
        int num = min % 5;
        if (num != 0) {
            if (num >= 3) {
                min = min + (5 - num);
            } else {
                min = min - num;
            }
        }
        time = setTimes[0] + ":" + (min >= 10 ? (min + "") : ("0" + min));
        return time;
    }

    public static String formatTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(time);
    }

    /**
     * @param endDate
     * @param nowDate
     * @Title: getDatePoor
     * @Description: 计算相差多少天
     * @return: long
     * @lastEditor: SP0009
     * @lastEdit: 2018年4月16日下午4:20:09
     */
    public static long getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day;
    }

    /**
     * @return long
     * @Author lz
     * @Description 获取当天0点的时间戳
     * @Date 15:36 2018/8/22
     * @Param []
     **/
    public static long getDay0() {
        return System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24)
                - TimeZone.getDefault().getRawOffset();
    }

    /**
     * @return long
     * @Author lz
     * @Description 获取当前月第一天0点时间戳
     * @Date 9:31 2018/8/23
     * @Param []
     **/
    public static long getMon0() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        // 将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        c.set(Calendar.MINUTE, 0);
        // 将秒至0
        c.set(Calendar.SECOND, 0);
        // 将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTimeInMillis();
    }

    /**
     * @return java.lang.String
     * @Author lz
     * @Description 传入一个时间和时间格式获得当月最后一天
     * @Date 16:54 2018/8/30
     * @Param [date, format]
     **/
    public static String getNextMonLday(String date, String format) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(Util.dateStringToDate(date, format));
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return Util.longToDateString(cal.getTimeInMillis(), format);
    }

    /**
     * @return long
     * @Author lz
     * @Description 获取当前月第一天0点时间戳
     * @Date 9:31 2018/8/23
     * @Param []
     **/
    public static long getYear0() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        int year = c.get(Calendar.YEAR);
        c.set(Calendar.DAY_OF_YEAR, 1);// 设置为1号,当前日期既为本月第一天
        // 将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        c.set(Calendar.MINUTE, 0);
        // 将秒至0
        c.set(Calendar.SECOND, 0);
        // 将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTimeInMillis();
    }

    /**
     * @return java.lang.String
     * @Author lz
     * @Description 传入一个时间和时间格式获得当年最后一天
     * @Date 16:54 2018/8/30
     * @Param [date, format]
     **/
    public static String getNextYearLday(String date, String format) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(Util.dateStringToDate(date, format));
        final int last = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        return Util.longToDateString(cal.getTimeInMillis(), format);
    }

    /**
     * @param currtime 当前是时间戳
     * @param count    正为加 负为减
     * @return
     * @author ybj
     * @date 2018年8月22日下午2:34:14
     * @Description -_-得到下/上 count 年/月/日的当前时间
     */
    public static long getNextOrLastTime(int field, long currtime, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currtime);
        calendar.add(field, count);
        return calendar.getTimeInMillis();
    }

    /**
     * @param time
     * @return
     * @throws ServiceException
     * @author ybj
     * @date 2018年8月23日下午3:56:01
     * @Description -_- 判断是否是当前月份
     */
    public static boolean isCurrMonth(long time) throws ServiceException {
        Date d = new Date();
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM");
        String s = f1.format(d);
        long strFormate = Util.strFormate(s, "yyyy-MM");
        if (time >= strFormate) {
            return true;
        }
        return false;
    }

    /**
     * @param offset 偏移量 离当前天的偏移量
     * @return
     * @author ybj
     * @date 2018年8月23日下午4:49:28
     * @Description -_-得到当前天的开始时间 比如 2018-08-23 00：00：00
     */
    public static long getCurrDayStartTime(int offset) {
        Calendar c = Calendar.getInstance();

        c.add(Calendar.DAY_OF_MONTH, offset);

        // 将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        c.set(Calendar.MINUTE, 0);
        // 将秒至0
        c.set(Calendar.SECOND, 0);
        // 将毫秒至0
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }

    /**
     * @param offset 偏移量 离当前天的偏移量
     * @return
     * @author ybj
     * @date 2018年8月23日下午5:48:13
     * @Description -_-得到当天结束时间 // 把结束时间加一天 在把毫秒减一 就是当天最后一刻 即结束时间
     */
    public static long getCurrDayEndTime(int offset) {

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DAY_OF_MONTH, offset);

        // 将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        c.set(Calendar.MINUTE, 0);
        // 将秒至0
        c.set(Calendar.SECOND, 0);
        // 将毫秒至0
        c.set(Calendar.MILLISECOND, 0);

        c.add(Calendar.DAY_OF_YEAR, 1);// 加一天

        c.add(Calendar.MILLISECOND, -1); // 减一毫秒

        return c.getTimeInMillis();
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     * @author ybj
     * @date 2018年8月27日上午10:55:41
     * @Description -_-得到一个时间到另一个时间内的t分钟间隔列表
     */
    public static synchronized List<String> getminuteInterval(long startTime, long endTime, int value) {
        Calendar calendar = Calendar.getInstance();
        List<String> list = new ArrayList<>();
        calendar.setTimeInMillis(startTime);
        while (calendar.getTimeInMillis() < endTime) {
            list.add(f3.format(calendar.getTime()));
            calendar.add(Calendar.MINUTE, 5);
        }
//		calendar.add(Calendar.MILLISECOND, -1);
//		list.add(f3.format(calendar.getTime()));
        return list;
    }

    /**
     * @return
     * @author ybj
     * @date 2018年8月27日下午3:30:49
     * @Description -_-得到两端时间内的所有年份列表
     */
    public static List<String> getYears(long startTime, long endTime) {
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        List<String> years = new ArrayList<>();
        calendarStart.setTimeInMillis(startTime);
        for (; calendarStart.get(Calendar.YEAR) <= calendarEnd.get(Calendar.YEAR); calendarStart.add(Calendar.YEAR,
                1)) {
            years.add(String.valueOf(calendarStart.get(Calendar.YEAR)));
        }
        return years;

    }

    /**
     * @return java.util.List<java.lang.String>
     * @Author lz
     * @Description 得到两端时间内的所有传入格式列表
     * @Date 10:02 2018/8/31
     * @Param [startTime, endTime, format, calendar]
     * 支持 按天等分Calendar.DAY_OF_MONTH，Calendar.MONTH 按月等分，Calendar.YEAR 按年等分
     **/
    public static ArrayList<String> getTimeList(String startTime, String endTime, String format, int calendar) {

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        ArrayList<String> years = new ArrayList<>();
        calendarStart.setTimeInMillis(Util.dateStringToLong(startTime, TimeEXP.exp1));
        calendarEnd.setTimeInMillis(Util.dateStringToLong(endTime, TimeEXP.exp1));
        if (calendar == Calendar.DAY_OF_MONTH) {
            calendarEnd.add(Calendar.DAY_OF_MONTH, 0);
            calendarEnd.set(Calendar.HOUR, 0);
            calendarEnd.set(Calendar.MINUTE, 0);
            calendarEnd.set(Calendar.SECOND, 0);
            calendarEnd.set(Calendar.MILLISECOND, 1);
        } else if (calendar == Calendar.MONTH) {
            calendarEnd.add(Calendar.MONTH, 1);
            calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
            calendarEnd.set(Calendar.HOUR, 0);
            calendarEnd.set(Calendar.MINUTE, 0);
            calendarEnd.set(Calendar.SECOND, 0);
            calendarEnd.set(Calendar.MILLISECOND, 0);
        } else if (calendar == Calendar.YEAR) {
            calendarEnd.set(Calendar.MONTH, 1);
            calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
            calendarEnd.set(Calendar.HOUR, 0);
            calendarEnd.set(Calendar.MINUTE, 0);
            calendarEnd.set(Calendar.SECOND, 0);
            calendarEnd.set(Calendar.MILLISECOND, 0);

        } else {
            return years;
        }
        for (; calendarStart.getTimeInMillis() < calendarEnd.getTimeInMillis(); calendarStart.add(calendar, 1)) {
            years.add(Util.longToDateString(calendarStart.getTimeInMillis(), format));
        }
        return years;
    }

    /**
     * @param str
     * @return
     * @throws ServiceException
     * @author ybj
     * @date 2018年8月27日下午3:51:31
     * @Description -_- yyyy-MM-dd HH:mm格式的时间字符串转换为时间戳
     */
    public static long str2Long(String str,String format) throws ServiceException {
        long time = -1;
        try {
        	SimpleDateFormat f3 = new SimpleDateFormat(format);
            time = f3.parse(str).getTime();
        } catch (ParseException e) {
            throw new ServiceException(Msg.TIME_FORMAT_ERROR);
        }
        return time;
    }

    /**
     * @param time
     * @return
     * @author ybj
     * @date 2018年8月27日下午3:54:55
     * @Description -_- 得到该时间的年
     */
    public static Integer getYearMonthDay(long time, int field) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(time);
        if (field == Calendar.MONTH || field == Calendar.DAY_OF_MONTH) {
            return calendar.get(field) + 1;
        }
        if (field == Calendar.YEAR) {
            return calendar.get(field);
        }
        return -1;
    }

    public static void main(String[] args) throws ParseException, SerialException, ServiceException {

        // System.out.println(formatTime(getYear0(), "yyyy年M月d日"));

        System.out.println(getTimeList("2018-08-20", "2018-09-04", TimeEXP.exp1, Calendar.DAY_OF_MONTH));
        System.out.println(getNextYearLday("2018-07-20", TimeEXP.exp1));
        System.out.println(getMon0());

    }

    /**
     * @return long
     * @Author lz
     * @Description 获取传入时间与count的和 filed为count单位DAY_OF_MONTH等
     * @Date 16:34 2018/8/27
     * @Param [field, currtime, count, formatType]
     **/
    public static String getNextOrLastTime(int field, String currtime, int count, String formatType) {
        long currtimel = Util.dateStringToLong(currtime, formatType);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currtimel);
        calendar.add(field, count);
        return Util.longToDateString(calendar.getTimeInMillis(), formatType);
    }
}
