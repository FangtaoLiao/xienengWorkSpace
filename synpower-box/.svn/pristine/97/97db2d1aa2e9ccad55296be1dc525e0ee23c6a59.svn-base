package com.synpower.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author ybj
 * @date 2018年8月29日下午2:09:09
 * @Description -_-近似值工具类
 */
public class ApproUtil {

	private ApproUtil() {
	}

	// doubl值保留两位小数 四舍五入
	public static double getRoundDouble(double value) {
	    DecimalFormat df = new DecimalFormat("#.00");
		df.setRoundingMode(RoundingMode.FLOOR);
		String format = df.format(value+0.005d);
		return Double.parseDouble(format);
	}

}
