package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Test;

/**
 * @author ybj
 * @date 2018年9月1日上午10:47:24
 * @Description -_-
 */
public class Test01 {

    //把时间转换为5分钟的整数 向最进五分钟整数靠拢
    private static long getTo5Int(long currTime) {

        // 五分钟的毫秒数
        double minute = 1000 * 60 * 5d;
        // 五分钟的个数
        double count = currTime / minute;
        // 五分钟四舍五入
        long countplus = (long) (count + 0.5);

        currTime = countplus * (long) minute;

        return currTime;
    }

    public static void main(String[] args) throws ParseException {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String s1 = "." + random.nextInt(1000);
            String s2 = ":" + (random.nextInt(50) + 10);
            String s = "2018-08-20 11:" + random.nextInt(60) + s2 + s1;
            Date parse = df.parse(s);
            long time = parse.getTime();
            long to5Int = getTo5Int(time);
            String format = df.format(new Date(to5Int));
            System.out.println(s + "--" + format);
        }

    }

}
