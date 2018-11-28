package com.synpower.util;

/*****************************************************************************
 * @Package: 
 * @ClassName: SunRiseSet
 * @Description: 根据经纬度、日期、时区 获取日出日落时间
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月3日上午9:57:40   Taylor             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
public class SunRiseSet {
	private static int[] days_of_month_1 = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static int[] days_of_month_2 = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private final static double h = -0.833;// 日出日落时太阳的位置
	private final static double UTo = 180.0;// 上次计算的日落日出时间，初始迭代值180.0

	/** 
	  * @Title:  getSunrise 
	  * @Description:  获取日出时间，格式 h:m  
	  * @param glong 经度
	  * @param glat 维度
	  * @param time，格式‘yyyy-MM-dd’，如2017-3-14
	  * @param zone，整数
	  * @return: String
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:02:14
	*/ 
	public static String getSunrise(double glong, double glat, String time, int zone) {
		double sunrise;
		int year, month, date;
		year = Util.getIntValue(time.split("-")[0],2001);
		month = Util.getIntValue(time.split("-")[1],1);
		date = Util.getIntValue(time.split("-")[2],1);

		sunrise = result_rise(
				UT_rise(UTo,
						GHA(UTo,
								G_sun(t_century(days(year, month, date), UTo)),
								ecliptic_longitude(
										L_sun(t_century(
												days(year, month, date), UTo)),
										G_sun(t_century(
												days(year, month, date), UTo)))),
						glong,
						e(h,
								glat,
								sun_deviation(
										earth_tilt(t_century(
												days(year, month, date), UTo)),
										ecliptic_longitude(
												L_sun(t_century(
														days(year, month, date),
														UTo)),
												G_sun(t_century(
														days(year, month, date),
														UTo)))))), UTo, glong,
				glat, year, month, date);
		return (int) (sunrise / 15 + zone) + ":"+ (int) (60 * (sunrise / 15 + zone - (int) (sunrise / 15 + zone)));
	}

	/** 
	  * @Title:  getSunset 
	  * @Description:  获取日落时间 
	  * @param glong 经度
	  * @param glat 维度
	  * @param time 时间，格式‘yyyy-MM-dd’，如2017-3-14
	  * @param zone 时区，整数
	  * @return: String
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日下午3:21:58
	*/ 
	public static String getSunset(double glong, double glat, String time, int zone) {
		double sunset;
		int year, month, date;
		year = Util.getIntValue(time.split("-")[0],2001);
		month = Util.getIntValue(time.split("-")[1],1);
		date = Util.getIntValue(time.split("-")[2],1);
		sunset = result_set(
				UT_set(UTo,
						GHA(UTo,
								G_sun(t_century(days(year, month, date), UTo)),
								ecliptic_longitude(
										L_sun(t_century(
												days(year, month, date), UTo)),
										G_sun(t_century(
												days(year, month, date), UTo)))),
						glong,
						e(h,
								glat,
								sun_deviation(
										earth_tilt(t_century(
												days(year, month, date), UTo)),
										ecliptic_longitude(
												L_sun(t_century(
														days(year, month, date),
														UTo)),
												G_sun(t_century(
														days(year, month, date),
														UTo)))))), UTo, glong,
				glat, year, month, date);
		return  (int) (sunset / 15 + zone) + ":"+ (int) (60 * (sunset / 15 + zone - (int) (sunset / 15 + zone)));
	}
	
	/** 
	  * @Title:  leap_year 
	  * @Description:  判断是否为闰年
	  * @param year
	  * @return: boolean
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午9:58:19
	*/ 
	private static boolean leap_year(int year) {
		if (((year % 400 == 0) || (year % 100 != 0) && (year % 4 == 0)))
			return true;
		else
			return false;
	}

	/** 
	  * @Title:  days 
	  * @Description:  求从格林威治时间公元2000年1月1日到计算日天数days 
	  * @param year
	  * @param month
	  * @param date
	  * @return: int
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午9:58:38
	*/ 
	private static int days(int year, int month, int date) {
		int i, a = 0;
		for (i = 2000; i < year; i++) {
			if (leap_year(i))
				a = a + 366;
			else
				a = a + 365;
		}
		if (leap_year(year)) {
			for (i = 0; i < month - 1; i++) {
				a = a + days_of_month_2[i];
			}
		} else {
			for (i = 0; i < month - 1; i++) {
				a = a + days_of_month_1[i];
			}
		}
		a = a + date;
		return a;
	}

	/** 
	  * @Title:  t_century 
	  * @Description:  求格林威治时间公元2000年1月1日到计算日的世纪数t 
	  * @param days
	  * @param UTo
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:00:06
	*/ 
	private static double t_century(int days, double UTo) {
		return ((double) days + UTo / 360) / 36525;
	}

	/** 
	  * @Title:  L_sun 
	  * @Description:  求太阳的平黄径 
	  * @param t_century
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:00:18
	*/ 
	private static double L_sun(double t_century) {
		return (280.460 + 36000.770 * t_century);
	}

	/** 
	  * @Title:  G_sun 
	  * @Description:  求太阳的平近点角 
	  * @param t_century
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:00:29
	*/ 
	private static double G_sun(double t_century) {
		return (357.528 + 35999.050 * t_century);
	}

	/** 
	  * @Title:  ecliptic_longitude 
	  * @Description:  求黄道经度 
	  * @param L_sun
	  * @param G_sun
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:00:38
	*/ 
	private static double ecliptic_longitude(double L_sun, double G_sun) {
		return (L_sun + 1.915 * Math.sin(G_sun * Math.PI / 180) + 0.02 * Math
				.sin(2 * G_sun * Math.PI / 180));
	}

	/** 
	  * @Title:  earth_tilt 
	  * @Description:  求地球倾角 
	  * @param t_century
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:00:49
	*/ 
	private static double earth_tilt(double t_century) {
		return (23.4393 - 0.0130 * t_century);
	}

	/** 
	  * @Title:  sun_deviation 
	  * @Description:  求太阳偏差 
	  * @param earth_tilt
	  * @param ecliptic_longitude
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:00:57
	*/ 
	private static double sun_deviation(double earth_tilt, double ecliptic_longitude) {
		return (180 / Math.PI * Math.asin(Math.sin(Math.PI / 180 * earth_tilt) * Math.sin(Math.PI / 180 * ecliptic_longitude)));
	}

	/** 
	  * @Title:  GHA 
	  * @Description:  求格林威治时间的太阳时间角GHA 
	  * @param UTo
	  * @param G_sun
	  * @param ecliptic_longitude
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:01:07
	*/ 
	private static double GHA(double UTo, double G_sun, double ecliptic_longitude) {
		return (UTo - 180 - 1.915 * Math.sin(G_sun * Math.PI / 180) - 0.02
				* Math.sin(2 * G_sun * Math.PI / 180) + 2.466
				* Math.sin(2 * ecliptic_longitude * Math.PI / 180) - 0.053 * Math
				.sin(4 * ecliptic_longitude * Math.PI / 180));
	}

	/** 
	  * @Title:  e 
	  * @Description:  求修正值e 
	  * @param h
	  * @param glat
	  * @param sun_deviation
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:01:18
	*/ 
	private static double e(double h, double glat, double sun_deviation) {
		return 180 / Math.PI
				* Math.acos((Math.sin(h * Math.PI / 180) - Math.sin(glat
						* Math.PI / 180)
						* Math.sin(sun_deviation * Math.PI / 180))
						/ (Math.cos(glat * Math.PI / 180) 
								* Math.cos(sun_deviation * Math.PI / 180)
						  ));
	}

	/** 
	  * @Title:  UT_rise 
	  * @Description:  求日出时间 
	  * @param UTo
	  * @param GHA
	  * @param glong
	  * @param e
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:01:33
	*/ 
	private static double UT_rise(double UTo, double GHA, double glong, double e) {
		return (UTo - (GHA + glong + e));
	}

	/** 
	  * @Title:  UT_set 
	  * @Description:  求日落时间 
	  * @param UTo
	  * @param GHA
	  * @param glong
	  * @param e
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:01:42
	*/ 
	private static double UT_set(double UTo, double GHA, double glong, double e) {
		return (UTo - (GHA + glong - e));
	}

	/** 
	  * @Title:  result_rise 
	  * @Description:  判断并返回结果（日出） 
	  * @param UT
	  * @param UTo
	  * @param glong
	  * @param glat
	  * @param year
	  * @param month
	  * @param date
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:01:51
	*/ 
	public static double result_rise(double UT, double UTo, double glong,
			double glat, int year, int month, int date) {
		double d;
		if (UT >= UTo)
			d = UT - UTo;
		else
			d = UTo - UT;
		if (d >= 0.1) {
			UTo = UT;
			UT = UT_rise(
					UTo,
					GHA(UTo,
							G_sun(t_century(days(year, month, date), UTo)),
							ecliptic_longitude(
									L_sun(t_century(days(year, month, date),
											UTo)),
									G_sun(t_century(days(year, month, date),
											UTo)))),
					glong,
					e(h,
							glat,
							sun_deviation(
									earth_tilt(t_century(
											days(year, month, date), UTo)),
									ecliptic_longitude(
											L_sun(t_century(
													days(year, month, date),
													UTo)),
											G_sun(t_century(
													days(year, month, date),
													UTo))))));
			result_rise(UT, UTo, glong, glat, year, month, date);
		}
		return UT;
	}

	/** 
	  * @Title:  result_set 
	  * @Description:  判断并返回结果（日落） 
	  * @param UT
	  * @param UTo
	  * @param glong
	  * @param glat
	  * @param year
	  * @param month
	  * @param date
	  * @return: double
	  * @lastEditor:  Taylor
	  * @lastEdit:  2017年11月3日上午10:02:03
	*/ 
	public static double result_set(double UT, double UTo, double glong,
			double glat, int year, int month, int date) {
		double d;
		if (UT >= UTo)
			d = UT - UTo;
		else
			d = UTo - UT;
		if (d >= 0.1) {
			UTo = UT;
			UT = UT_set(
					UTo,
					GHA(UTo,
							G_sun(t_century(days(year, month, date), UTo)),
							ecliptic_longitude(
									L_sun(t_century(days(year, month, date),
											UTo)),
									G_sun(t_century(days(year, month, date),
											UTo)))),
					glong,
					e(h,
							glat,
							sun_deviation(
									earth_tilt(t_century(
											days(year, month, date), UTo)),
									ecliptic_longitude(
											L_sun(t_century(
													days(year, month, date),
													UTo)),
											G_sun(t_century(
													days(year, month, date),
													UTo))))));
			result_set(UT, UTo, glong, glat, year, month, date);
		}
		return UT;
	}

	

	public static void main(String[] args){
		System.out.println(getSunrise(104.0644200000,30.5688900000,"2017-11-03",8));
		System.out.println(getSunset(104.0636293034,30.5687090052,"2017-11-03",8));
	}
}