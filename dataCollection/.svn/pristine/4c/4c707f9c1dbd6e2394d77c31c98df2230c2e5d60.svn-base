package com.synpowertech.dataCollectionJar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ***************************************************************************
 * 
 * @Package: com.synpowertech.utils
 * @ClassName: DataProcessing
 * @Description: 数据处理的工具类，包括数据增益和偏移计算，数据范围判断等
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年11月8日下午3:58:00 SP0010 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class DataProcessingUtil {

	private static final Logger logger = LoggerFactory.getLogger(DataProcessingUtil.class);
	/**
	 * @Title: gainFactor
	 * @Description: 采集到值得增益计算
	 * @param originVal
	 * @param signalGuid
	 * @return: double
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月8日下午6:03:29
	 */
	public static double gainFactor(String originVal, String signalGuid) {

		double valueTemp = Double.parseDouble(originVal);
		// 增益计算
		valueTemp = CacheMappingUtil.signalGuid2gain(signalGuid) == null ? valueTemp
				: valueTemp * CacheMappingUtil.signalGuid2gain(signalGuid);

		// 修正计算
		valueTemp = CacheMappingUtil.signalGuid2factor(signalGuid) == null ? valueTemp
				: valueTemp + CacheMappingUtil.signalGuid2factor(signalGuid);

		return valueTemp;
	}

	/**
	 * @Title: valLegitimation
	 * @Description: 对有数据大小限制的遥测遥信和遥控遥调数据进行合法性判断
	 * @param signalGuid
	 * @param signalVal
	 * @return: boolean
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月9日上午9:36:32
	 */
	public static boolean valLegitimation(String signalGuid, double signalVal) {
		// 最小值判断结果
		boolean minResult = true;
		// 最大值判断结果
		boolean maxResult = true;

		// 最小值判断
		minResult = CacheMappingUtil.signalGuid2minVal(signalGuid) == null ? true : signalVal >= CacheMappingUtil.signalGuid2minVal(signalGuid);

		// 最大值判断
		maxResult = CacheMappingUtil.signalGuid2maxVal(signalGuid) == null ? true : signalVal <= CacheMappingUtil.signalGuid2maxVal(signalGuid);

		return minResult && maxResult;
	}

	/**
	 * @Title: gainFactorReverse
	 * @Description: 遥控遥调下发值反向增益计算
	 * @param ykYtVal
	 * @param signalGuid
	 * @return: String
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月16日下午2:25:52
	 */
	public static String gainFactorReverse(String ykYtVal, String signalGuid) {

		double valueTemp = Double.parseDouble(ykYtVal);

		// 修正计算
		valueTemp = CacheMappingUtil.signalGuid2factor(signalGuid) == null ? valueTemp : valueTemp - CacheMappingUtil.signalGuid2factor(signalGuid);

		// 增益计算
		valueTemp = CacheMappingUtil.signalGuid2gain(signalGuid) == null ? valueTemp : valueTemp / CacheMappingUtil.signalGuid2gain(signalGuid);
		return String.valueOf(valueTemp);
	}

	/**
	 * @Title: ycProcess4Clou
	 * @Description: 科陆电表，双寄存器遥测特殊处理
	 * @param sourseVal:
	 *            void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月22日下午5:02:51
	 */
	public static String ycProcess4Clou(String sourceVal) {
		// 整数位
		long f1 = 0;
		// 小数位
		long f2 = 0;
		double f = 0;

		double sourceValTemp = Double.valueOf(sourceVal);
		int sourceValTemp1 = (int) sourceValTemp;
		String bitStr = Integer.toBinaryString(sourceValTemp1);

		// 位数不足补0
		if (bitStr.length() < 32) {
			bitStr = String.format("%032d", Integer.valueOf(bitStr));
		}

		String targetValTemp = sourceVal;
		try {
			f1 = Util4j.binaryToInteger(bitStr.substring(0, 24), false);// 无符号数据
			f2 = Util4j.binaryToInteger(bitStr.substring(24, 32), false);// 无符号数据
			f = Double.valueOf((String.valueOf(f1) + "." + String.valueOf(f2)));
			targetValTemp = Util4j.subZeroAndDot(String.format("%.2f", f));
		} catch (Exception e) {
			logger.warn("can not process the val:{}",e);
		}
		return targetValTemp;
	}

	/**
	 * @Title: ycProcess4yx
	 * @Description: 遥测表遥信(中间几位处理)
	 * @param sourseVal
	 * @param startBit
	 * @param endBit:
	 *            void
	 * @lastEditor: SP0010
	 * @lastEdit: 2017年11月22日下午5:06:05
	 */
	public static String ycProcess4yx(String sourceVal, Integer startBit, Integer endBit) {
		
		logger.debug(sourceVal + ":" + startBit + ":" + endBit);
		double sourceValTemp = Double.valueOf(sourceVal);
		int sourceValTemp1 = (int) sourceValTemp;
		String bitStr = Integer.toBinaryString(sourceValTemp1);

		// 位数不足补0,
		int endBitQuotient = endBit % 8 == 0 ? endBit / 8 : endBit / 8 + 1;
		
		if (bitStr.length() < endBitQuotient * 8) {
			
			logger.debug(bitStr);
//			bitStr = String.format("%0" + endBitQuotient * 8 + "d", Integer.valueOf(bitStr));
			bitStr = String.format("%0" + endBitQuotient * 8 + "d", Long.valueOf(bitStr));
			logger.debug(bitStr);
		}

		int strLength = bitStr.length();
		String targetValTemp = sourceVal;
		try {
			// 无符号数据
			long f1 = Util4j.binaryToInteger(bitStr.substring(strLength - endBit, strLength - startBit), false);
			targetValTemp = String.valueOf(f1);
		} catch (Exception e) {
			logger.warn("can not process the val:{}",e);
		}
		return targetValTemp;
	}
}
