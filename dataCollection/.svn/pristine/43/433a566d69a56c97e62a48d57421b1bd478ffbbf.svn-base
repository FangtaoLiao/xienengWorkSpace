package com.synpowertech.dataCollectionJar.utils;

public class TimeUtil {

    //把时间转换为5分钟的整数 向最进五分钟整数靠拢
    public static long getTo5Int(long currTime) {

        // 五分钟的毫秒数
        double minute = 1000 * 60 * 5d;
        // 五分钟的个数
        double count = currTime / minute;
        // 五分钟四舍五入
        long countplus = (long) (count + 0.5);

        currTime = countplus * (long) minute;

        return currTime;
    }
}
