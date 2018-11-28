package com.synpower.util;

import com.synpower.constant.UNIT;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单位转换工具类
 *
 * @author ybj
 * @date 2018年8月23日上午10:52:02
 * @Description -_-
 */
public class UnitUtil {

	private static NumberFormat nf = null;

	static {
		nf = NumberFormat.getNumberInstance();
		// 保留两位小数
		nf.setMaximumFractionDigits(2);
		// 如果不需要四舍五入，可以使用RoundingMode.DOWN
		nf.setRoundingMode(RoundingMode.UP);
	}

	private UnitUtil() {
	}

	/**
	 * @param elec
	 * @return
	 * @author ybj
	 * @date 2018年8月23日上午11:31:48
	 * @Description -_-电量单位转换
	 */
	public static void ConvertElec01(List<Map<String, Object>> list) {

		if (!Util.isNotBlank(list)) {
			return;
		}

		double maxValue = 0;

		for (Map<String, Object> map : list) {
			double d = Util.getDouble(map.get("value"));
			if (d > maxValue) {
				maxValue = d;
			}
		}
		if (maxValue >= 10000d) {
			for (Map<String, Object> map : list) {
				double value = Util.getDouble(map.get("value"));
				value /= 10000d;
				String format = nf.format(value);
				map.put("value", Util.getDouble(format));
				map.put("unit", "万度");
			}
		} else if (maxValue > 100000000d) {
			for (Map<String, Object> map : list) {
				double value = Util.getDouble(map.get("value"));
				value /= 100000000d;
				String format = nf.format(value);
				map.put("value", Util.getDouble(format));
				map.put("unit", "亿度");
			}
		}
	}

	/**
	 * @param elec
	 * @return
	 * @author ybj
	 * @date 2018年8月23日上午11:31:48
	 * @Description -_-电量单位转换 嵌套比较深 自己都不想看
	 */
	public static void ConvertElec02(Map<String, Object> map) {
		if (!Util.isNotBlank(map)) {
			return;
		}
		List<Object> y = (List<Object>) map.get("yData");

		if (Util.isNotBlank(y)) {
			Map<String, Object> y1 = (Map<String, Object>) y.get(1);
			Map<String, Object> y2 = (Map<String, Object>) y.get(2);
			double maxValue = Util.getDouble(y1.get("max")) > Util.getDouble(y2.get("max"))
					? Util.getDouble(y1.get("max"))
					: Util.getDouble(y2.get("max"));

			Double[] y1Values = (Double[]) y1.get("value");
			Double[] y2Values = (Double[]) y2.get("value");
			if (maxValue >= 10000d) {
				for (int i = 0; i < y1Values.length; i++) {
					double value = y1Values[0] / 10000d;
					String format = nf.format(value);
					y1Values[0] = Util.getDouble(format);
					y1.put("unit", "万度");
				}
				for (int i = 0; i < y2Values.length; i++) {
					double value = y2Values[0] / 10000d;
					String format = nf.format(value);
					y2Values[0] = Util.getDouble(format);
					y2.put("unit", "万度");
				}
			} else if (maxValue > 100000000d) {
				for (int i = 0; i < y1Values.length; i++) {
					double value = y1Values[0] / 100000000d;
					String format = nf.format(value);
					y1Values[0] = Util.getDouble(format);
					y1.put("unit", "亿度");
				}
				for (int i = 0; i < y2Values.length; i++) {
					double value = y2Values[0] / 100000000d;
					String format = nf.format(value);
					y2Values[0] = Util.getDouble(format);
					y2.put("unit", "亿度");
				}
			}
		}
	}

	/**
	 * @Author lz
	 * @Description 自主识别单位
	 * @Date 15:43 2018/8/27
	 * @Param [value, u]
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	public static Map<String, Object> ConvertUNIT(Double value, UNIT u) {
		Map<String, Object> map = new HashMap<>(2);
		if (value == null) {
			value = 0d;
		}
		map.put("unit", u.getbUnit1());
		if (value >= u.getTraH1()) {
			value = value / u.getTraH1();
			String format = nf.format(value);
			value = Double.parseDouble(format);
			map.put("unit", u.getbUnit2());
		} else if (value > u.getTraH2()) {
			value = value / u.getTraH2();
			String format = nf.format(value);
			value = Double.parseDouble(format);
			map.put("unit", u.getbUnit3());
		}

		map.put("value", value);
		String valueAndunit = value + "" + map.get("unit");
		map.put("valueAndunit", valueAndunit);
		return map;
	}

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月31日下午4:04:22
	 * @Description -_-将电量单位转换为KW
	 */
	public static double elecUnit2KW(double value, String unit) {
		if ("MW".equals(unit)) {
			value *= 1000d;
		} else if ("GW".equals(unit)) {
			value *= 1000000d;
		}
		return value;
	}
}
