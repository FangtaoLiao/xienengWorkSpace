package com.synpowertech.dataCollectionJar.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class Util4j {

	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */

	/**
	 * 检查字符串是否是null，是null返回空
	 * 
	 * @param str
	 * @return
	 */
	public static String null2String(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 检查对象是否null，为null返回""
	 * 
	 * @param obj
	 * @return
	 */
	public static String null2String(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 检查输入是否是int，不是则返回指定的 i
	 * 
	 * @param str
	 * @param i
	 * @return
	 */
	public static int getIntValue(String str, int i) {
		try {
			return Integer.parseInt(null2String(str));
		} catch (Exception localException) {
			
		}
		return i;
	}
	
	 /**
	   * @Title:  getIntValue4ListSize 
	   * @Description: 遥测入库List大小参数获取，大于10000设为10000，小于0，设为2500默认值
	   * @param str
	   * @param i
	   * @return: int
	   * @lastEditor:  SP0010
	   * @lastEdit:  2017年12月6日上午10:50:22
	  */
	public static int getIntValue4ListSize(String str, int i) {
		try {
			int listSize = Integer.parseInt(null2String(str));
			int listSizeTemp = listSize > 10000 ? 10000 : listSize;
			listSize = listSizeTemp < 50 ? 2500 : listSizeTemp;
			return listSize;
		} catch (Exception localException) {
			
		}
		return i;
	}

	/**
	 * 检查输入是否是float，不是则返回指定的 f
	 * 
	 * @param str
	 * @param f
	 * @return
	 */
	public static float getFloatValue(String str, float f) {
		try {
			return Float.parseFloat(str);
		} catch (Exception localException) {
		}
		return f;
	}
	public static long getLongValue(String str, long l) {
		try {
			return Long.parseLong(null2String(str));
		} catch (Exception localException) {
		}
		return l;
	}
	
	/**
	 * 检查输入是否是double，不是则返回指定的 d
	 * 
	 * @param str
	 * @param d
	 * @return
	 */
	public static double getDoubleValue(String str, double d) {
		try {
			return Double.parseDouble(str);
		} catch (Exception localException) {
		}
		return d;
	}

	/**
	 * 检查输入项是否是email地址
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isEmail(String paramString) {
		if ((paramString == null) || ("".equals(paramString))) {
			return false;
		}
		if ((paramString.indexOf(";") >= 0) || (paramString.indexOf(",") >= 0) || (paramString.indexOf(">") >= 0)
				|| (paramString.indexOf("<") >= 0) || (paramString.indexOf("[") >= 0) || (paramString.indexOf("]") >= 0)
				|| (paramString.indexOf(")") >= 0) || (paramString.indexOf("(") >= 0)) {
			return false;
		}
		int i = 0;
		i = paramString.indexOf("@");
		if (i == -1) {
			return false;
		}
		if (paramString.substring(i).indexOf(".") < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 对象数组中是否包含某一对象
	 * 
	 * @param arrayOfObject
	 * @param paramObject
	 * @return
	 */
	public static boolean contains(Object[] arrayOfObject, Object obj) {
		if ((arrayOfObject == null) || (obj == null)) {
			return false;
		}
		for (int i = 0; i < arrayOfObject.length; i++) {
			if ((arrayOfObject[i] != null) && (arrayOfObject[i].equals(obj))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 截取数字字符串中的整数部分
	 * 
	 * @param str
	 * @return
	 */
	public static String getIntValues(String str) {
		if (str.indexOf(".") == -1) {
			return str;
		} else {
			return str.substring(0, str.indexOf("."));
		}
	}

	/**
	 * 获取请求主机
	 * 
	 * @param paramHttpServletRequest
	 * @return
	 */

 	/** 
 	  * @Title: dateToDateString 
 	  * @Description: date类型转换为String类型
 	  * @param: @param data
 	  * @param: @param formatType formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
 	  * @param: @return
 	  * @return: String
 	  * @lastEditor: Taylor
 	  * @lastEdit: 2017年7月17日下午3:01:07
 	*/ 
 	public static String dateToDateString(Date data, String formatType) {
 		return new SimpleDateFormat(formatType).format(data);
 	}

 	/** 
 	  * @Title: longToDateString 
 	  * @Description: long类型转换为String类型 
 	  * @param: @param l 要转换的long类型的时间
 	  * @param: @param formatType 要转换的string类型的时间格式
 	  * @param: @return
 	  * @return: String
 	  * @lastEditor: Taylor
 	  * @lastEdit: 2017年7月17日下午3:06:07
 	*/ 
 	public static String longToDateString(long l, String formatType){
 		Date date;
		date = longToDate(l, formatType);
 		String strTime = dateToDateString(date, formatType); // date类型转成String
 		return strTime;
 	}

 	/** 
 	  * @Title: strDateToDate 
 	  * @Description: string类型转换为date类型 
 	  * @param: @param strTime 要转换的string类型的时间
 	  * @param: @param formatType 要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
 	  * @param: @return
 	  * @return: Date
 	  * @lastEditor: Taylor
 	  * @lastEdit: 2017年7月17日下午3:04:26
 	  * @note:strTime的时间格式和formatType的时间格式必须相同
 	*/ 
 	public static Date dateStringToDate(String strTime, String formatType) {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
 		return date;

 	}
 
 	/** 
 	  * @Title: longToDate 
 	  * @Description: long转换为Date类型 
 	  * @param: @param l 要转换的long类型的时间
 	  * @param: @param formatType 要转换的时间格式yyyy-MM-dd HH:mm:ss或yyyy-MM-dd HH:mm:ss.SSS//yyyy年MM月dd日 HH时mm分ss秒
 	  * @param: @return
 	  * @return: Date
 	  * @lastEditor: Taylor
 	  * @lastEdit: 2017年7月17日下午3:07:24
 	*/ 
 	public static Date longToDate(long l, String formatType) {
 		Date dateOld = new Date(l); // 根据long类型的毫秒数生命一个date类型的时间
 		String sDateTime = dateToDateString(dateOld, formatType); // 把date类型的时间转换为string
 		Date date = dateStringToDate(sDateTime, formatType); // 把String类型转换为Date类型
 		return date;
 	}

 	/** 
 	  * @Title: stringToLong 
 	  * @Description: string类型转换为long类型 
 	  * @param: @param strTime 要转换的String类型的时间
 	  * @param: @param formatType 要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
 	  * @param: @return
 	  * @return: long
 	  * @lastEditor: Taylor
 	  * @lastEdit: 2017年7月17日下午3:08:59
 	  * @note:strTime的时间格式和formatType的时间格式必须相同
 	*/ 
 	public static long dateStringToLong(String strTime, String formatType) {
 		Date date = dateStringToDate(strTime, formatType); // String类型转成date类型
 		if (date == null) {
 			return 0;
 		} else {
 			return date.getTime(); // date类型转成long类型
 		}
 	}

 	/** 
 	  * @Title: getCurrentTime 
 	  * @Description: 类型转换为long类型 
 	  * @param: formatType 目标格式
 	  * @return: String
 	  * @lastEditor: Taylor
 	  * @lastEdit: 2017年7月17日下午3:11:12
 	*/ 
 	public static String getCurrentTimeStr(String formatType) {
 		Date date = new Date();
 		return longToDateString(date.getTime(),formatType);
 	}
 	
	/**
	 * 获取两个日期中的月份差
	 * 
	 * @param paramString1
	 * @param paramString2
	 * @return
	 */
	public static int monthDiff(String paramString1, String paramString2) {
		int i = 0;
		if ((paramString1.equals("")) || (paramString1 == null) || (paramString1.length() < 10)
				|| (paramString2.equals("")) || (paramString2 == null) || (paramString2.length() < 10)) {
			return 0;
		}
		int j = getIntValue(paramString1.substring(0, 4), -1);
		int k = getIntValue(paramString1.substring(5, 7), -1) - 1;
		int m = getIntValue(paramString2.substring(0, 4), -1);
		int n = getIntValue(paramString2.substring(5, 7), -1) - 1;

		i = n - k;
		i += 12 * (m - j);

		return i;
	}

	/**
	 * 获取两个日期中的年份差
	 * 
	 * @param paramString1
	 * @param paramString2
	 * @return
	 */
	public static int yearDiff(String paramString1, String paramString2) {
		int i = 0;
		if ((paramString1.equals("")) || (paramString1 == null) || (paramString1.length() < 10)
				|| (paramString2.equals("")) || (paramString2 == null) || (paramString2.length() < 10)) {
			return 0;
		}
		int j = getIntValue(paramString1.substring(0, 4), -1);
		int k = getIntValue(paramString2.substring(0, 4), -1);

		i = k - j;

		return i;
	}

	/**
	 * 数组转换为ArrayList
	 * 
	 * @param paramArrayOfObject
	 * @return
	 */
	public static ArrayList<Object> arrayToArrayList(Object[] paramArrayOfObject) {
		ArrayList<Object> localArrayList = new ArrayList<Object>();
		if (paramArrayOfObject == null) {
			return localArrayList;
		}
		for (int i = 0; i < paramArrayOfObject.length; i++) {
			localArrayList.add(paramArrayOfObject[i]);
		}
		return localArrayList;
	}

	/**
	 * 字符串过长，截取后补“...”
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String getSubStr(String paramString, int length) {
		int i = paramString.length();
		if (i < length) {
			return paramString;
		}
		return new StringBuilder().append(paramString.substring(0, length - 1)).append("...").toString();
	}

	/**
	 * 获取随机值，英文+数字,默认长度为8
	 * 
	 * @length 随机值长度
	 * @return
	 */
	public static String getRandomStr(int length) {
		String str = "";
		int i = 0;
		for (int j = 0; j < (length > 0 ? length : 8); j++) {
			char c;
			if (i == 0) {
				i = 1;
				c = (char) (int) (Math.random() * 26.0D + 97.0D);
				str = new StringBuilder().append(str).append(c).toString();
			} else {
				i = 0;
				c = (char) (int) (Math.random() * 10.0D + 48.0D);
				str = new StringBuilder().append(str).append(c).toString();
			}
		}
		return str;
	}

	/**
	 * 生成随机数
	 * 
	 * @param paramInt
	 *            密码长度
	 * @return
	 */
	public static String getRandomNo(int length) {
		String str1 = "";
		String[] arrayOfString = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

		Random localRandom = new Random();
		while (str1.length() < length) {
			String str2 = arrayOfString[localRandom.nextInt(10)];
			if (str1.indexOf(str2) == -1) {
				str1 = new StringBuilder().append(str1).append(str2).toString();
			}
		}
		return str1;
	}

	/**
	 * 生成随机英文字符串
	 * 
	 * @param length
	 *            密码长度
	 * @return
	 */
	public static String getRandomEn(int length) {
		String str1 = "";
		String[] arrayOfString = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
				"r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

		Random localRandom = new Random();
		while (str1.length() < length) {
			String str2 = arrayOfString[localRandom.nextInt(52)];
			if (str1.indexOf(str2) == -1) {
				str1 = new StringBuilder().append(str1).append(str2).toString();
			}
		}
		return str1;
	}

	/**
	 * 千分位表示数字字符串
	 * 
	 * @param paramString
	 * @return
	 */
	public static String thousandFloatFormat(String paramString) {
		String str1 = paramString;
		String str2 = "";
		if (!"".equals(paramString)) {
			if (paramString.indexOf(".") == -1) {
				str1 = paramString;
			} else {
				if (!paramString.substring(0, paramString.indexOf(".")).equals("")) {
					str1 = paramString.substring(0, paramString.indexOf("."));
				} else {
					str1 = "0";
				}
				str2 = paramString.substring(paramString.indexOf(".") + 1);
			}
			double d = getDoubleValue(str1,-1d);
			DecimalFormat localDecimalFormat = new DecimalFormat("###,###");
			if (paramString.indexOf(".") == -1) {
				str2 = new StringBuilder().append("").append(localDecimalFormat.format(d)).toString();
			} else {
				str2 = new StringBuilder().append("").append(localDecimalFormat.format(d)).append(".").append(str2)
						.toString();
			}
		}
		return str2;
	}
	
	/**
	 * 获取指定小数位数的字符串
	 * @param str
	 * @param length
	 * @return
	 */
	public static String getDecimalDigits(String str, int length){	
		
		String doubleStr = String.valueOf(getDoubleValue(str,0));
		BigDecimal bd=new BigDecimal(doubleStr); 	
		return String.valueOf(bd.setScale(length,RoundingMode.HALF_UP));
	}
	
	/**
	 * 查找ArrayList，返回索引
	 * 
	 * @param arrayList
	 * @param paramString
	 * @return
	 */
	public static int getListIndex(ArrayList<?> arrayList, String str) {
		int i = -1;
		try {
			str = null2String(str);
			if ((arrayList == null) || (arrayList.size() < 1)) {
				i = -1;
			} else {
				for (int j = 0; j < arrayList.size(); j++) {
					String localStr = null2String((String) arrayList.get(j)).trim();
					if (localStr.equalsIgnoreCase(str)) {
						i = j;
						break;
					}
				}
			}
		} catch (Exception localException) {
			i = -1;
		}
		return i;
	}

	/**
	 * 获取日期
	 * 
	 * @param format  返回的日期格式
	 * 1表示yyyy-MM-dd，2表示yyyy-MM-dd HH:mm:ss，3表示
	 * @return
	 */
	public static String getCurrentDate(int format) {
		String str = "";
		SimpleDateFormat localSimpleDateFormat = null;
		if (format == 1) {
			localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else if (format == 2) {
			localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (format == 3) {
			localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
		Date localDate = new Date();
		str = localSimpleDateFormat.format(localDate);
		return str;
	}

    /** 
      * @Title: subZeroAndDot 
      * @Description: 去掉多余的.与0  
      * @param: @param s
      * @param: @return
      * @return: String
      * @lastEditor: Taylor
      * @lastEdit: 2017年8月10日下午10:23:01
    */ 
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }  
    
    /** 
	  * @Title: binaryToInteger 
	  * @Description: 将长度小于64位的二进制字符串转换为有符号、无符号数
	  * @param: @param binStr
	  * @param: @param hasMark true：有符号；false：无符号
	  * @param: @return
	  * @return: long
	  * @lastEditor: Taylor
	  * @lastEdit: 2017年8月11日下午2:32:51
	*/ 
	public static long binaryToInteger(String binStr,boolean hasMark) {	
		if(binStr==null || binStr.length()==0 || binStr.length() > 64){
			return 0;
		}else{
			int length = binStr.length();
			String[] b = new String[binStr.length()];
			long l = 0;
			try {				
				if(hasMark){
					if(binStr.substring(0,1).equals("0")){
						for(int i=0;i<length;i++){
							b[i] = binStr.substring(i,(i+1));
							l += Integer.parseInt(b[i])*Math.pow(2, (length-i-1));
						}
						return l;
					}else{
						for(int i=1;i<length;i++){
							b[i] = binStr.substring(i,(i+1));
							b[i] = "0".equals(b[i])?"1":"0";
							l += Integer.parseInt(b[i])*Math.pow(2, (length-i-1));
						}
						return -l-1;
					}
				}else{
					for(int i=0;i<binStr.length();i++){
						b[i] = binStr.substring(i,(i+1));
						l += Integer.parseInt(b[i])*Math.pow(2, (length-i-1));
					}
					return l;
				}				
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}			
		 }
	}
	/** 
	  * @Title:  subFloat 
	  * @Description:  float 减法
	  * @param f1
	  * @param f2
	  * @return: float
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年9月15日上午11:36:19
	*/ 
	public static double subFloat(double f1,double f2,int type){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal d=d1.subtract(d2);
		if (type==1) {
			d=d.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return d.doubleValue();
	}
	/** 
	  * @Title:  subDouble 
	  * @Description:  只相减，不四舍五入 
	  * @param f1
	  * @param f2
	  * @return: double
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年9月25日上午11:40:05
	*/ 
	public static double subDouble(double f1,double f2){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal d=d1.subtract(d2);
		return d.doubleValue();
	}
	public static double multFloat(double f1,double f2,int type){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal d=d1.multiply(d2);
		if (type==1) {
			d=d.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return d.doubleValue();
	}
	public static double multFloat(double f1,double f2){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal d=d1.multiply(d2);
		return d.doubleValue();
	}
	/** 
	  * @Title:  multDouble 
	  * @Description:  只相乘，不四舍五入  
	  * @param f1
	  * @param f2
	  * @return: double
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年9月25日上午11:41:16
	*/ 
	public static double multDouble(double f1,double f2){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal d=d1.multiply(d2);
		return d.doubleValue();
	}
	public static double getPowerPrice(double f1,double f2,double f3){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal  d3=new BigDecimal(Double.toString(f3));
		BigDecimal d=d1.subtract(d2).multiply(d3).setScale(2, BigDecimal.ROUND_HALF_UP);
		return d.doubleValue();
	}
	public static double divideFloat(double f1,double f2){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		if (f2!=0) {
			BigDecimal  d2=new BigDecimal(Double.toString(f2));
			BigDecimal d=d1.divide(d2,2,BigDecimal.ROUND_HALF_UP);
			return d.doubleValue();
		}else{
			return 0;
		}
	}
	/** 
	  * @Title:  divideDouble 
	  * @Description:  除法，保留2位 
	  * @param f1
	  * @param f2
	  * @return: double
	  * @lastEditor:  SP0007
	  * @lastEdit:  2017年9月29日上午10:49:08
	*/ 
	public static double divideDouble(double f1,double f2){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		if (f2!=0) {
			BigDecimal  d2=new BigDecimal(Double.toString(f2));
			BigDecimal d=d1.divide(d2,8,BigDecimal.ROUND_HALF_UP);
			return d.doubleValue();
		}else{
			return 0;
		}
	}
	public static double divideDouble(double f1,double f2,int type){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		if (f2!=0) {
			BigDecimal  d2=new BigDecimal(Double.toString(f2));
			BigDecimal d=d1.divide(d2,8,BigDecimal.ROUND_HALF_UP);
			if (type==1) {
				d=d.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			return d.doubleValue();
		}else{
			return 0;
		}
	}
	public static double addFloat(double f1,double f2,int type){
		BigDecimal  d1=new BigDecimal(Double.toString(f1));
		BigDecimal  d2=new BigDecimal(Double.toString(f2));
		BigDecimal d=d1.add(d2);
		if (type==1) {
			d=d.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return d.doubleValue();
	}
	public static double addFloats(int type,double ...f1){
		BigDecimal d=new BigDecimal(Double.toString(f1[0]));
		for (int i = 1; i < f1.length; i++) {
			BigDecimal d1=new BigDecimal(Double.toString(f1[i]));
			d=d.add(d1);
		}
		if (type==1) {
			d=d.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return d.doubleValue();
	}
	/** 
	  * @Title:  addDoubles 
	  * @Description:  相加多个，只相加，不四舍五入 
	  * @param f1
	  * @return: double
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年9月25日上午11:46:03
	*/ 
	public static double addDoubles(double ...f1 ){
		BigDecimal d=new BigDecimal(Double.toString(f1[0]));
		for (int i = 1; i < f1.length; i++) {
			BigDecimal d1=new BigDecimal(Double.toString(f1[i]));
			d=d.add(d1);
		}
		return d.doubleValue();
	}
	
	/**
	  * @Title:  getStr 
	  * @Description:  返回字符串，如果为空，就返回默认字符串
	  * @param str
	  * @param defaultStr
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月6日下午12:34:46
	 */
	public static String getStr(String str,String defaultStr){
		if (StringUtils.isNotBlank(str)) {
			return str;
		} else {
			return defaultStr;
		}
	}
	
	/**
	  * @Title:  str2int 
	  * @Description:  str能有转化int就返回，否则返回默认值 
	  * @param str
	  * @param i
	  * @return: int
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月6日下午2:02:20
	 */
	public static int str2int(String str, int i) {
		if (StringUtils.isNotBlank(str)) {
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				return i;
			}
			
		} 
		return i;
	}
	
	/**
	  * @Title:  getBoolean 
	  * @Description: str转化Boolean
	  * @param str
	  * @param bool
	  * @return: Boolean
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年3月16日下午5:47:01
	 */
	public static Boolean getBoolean(String str,Boolean bool){
		if (StringUtils.isNotBlank(str)) {
			return Boolean.valueOf(str);
		} else {
			return bool;
		}
	}

}
