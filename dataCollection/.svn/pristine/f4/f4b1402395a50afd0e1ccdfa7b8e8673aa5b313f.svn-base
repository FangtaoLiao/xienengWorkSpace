package com.synpowertech.dataCollectionJar.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * ***************************************************************************
 *
 * @version -----------------------------------------------------------------------------
 * VERSION           TIME                BY           CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 * 1.0    2017年11月7日下午6:07:33   SP0010             create
 * -----------------------------------------------------------------------------
 * @Package: com.synpowertech.utils
 * @ClassName: ReflectionUtil
 * @Description: 反射获取类的实例，并赋值
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 * ****************************************************************************
 */
public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * @param obj
     * @param field
     * @param value: void
     * @Title: setAttr
     * @Description: 通过反射修改对象的值
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月7日下午6:07:02
     */
    public static void setAttr(Object obj, String field, Object value) {

        Field f1 = null;
        try {
            // 获取类Hero的名字叫做name的字段
            f1 = obj.getClass().getDeclaredField(field);
            //获取权限
            f1.setAccessible(true);
            // 修改这个字段的值
            f1.set(obj, value);
        } catch (Exception e) {

            logger.debug("trying to solve......", e);
            //修改其强转异常
            //float strValTemp1 =(float)value;
            float strValTemp1 = Float.valueOf(String.valueOf(value));

            //方法②：部分字段不是Float,又没有单独转化，在这里处理
            //根据反射获取类型
            Class<?> type = f1.getType();
            //特殊处理turnOnTime等
            if (type == long.class || type == Long.class) {
                long strValTemp2 = (long) strValTemp1;
                // 修改这个字段的值
                try {
                    f1.set(obj, strValTemp2);
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    logger.error("failed to set the value :{}", e1);
                }

                //特殊处理data_bms表中字段等
            } else if (type == int.class || type == Integer.class) {
                int strValTemp2 = (int) strValTemp1;
                // 修改这个字段的值
                try {
                    f1.set(obj, strValTemp2);
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    logger.error("failed to set the value :{}", e1);
                }

                //特殊处理runningStatus等
            } else if (type == byte.class || type == Byte.class) {
                byte strValTemp2 = (byte) strValTemp1;
                // 修改这个字段的值
                try {
                    f1.set(obj, strValTemp2);
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    logger.error("failed to set the value :{}", e1);
                }
            } else if (type == double.class || type == Double.class) {
                double strValTemp2 = (double) strValTemp1;
                // 修改这个字段的值
                try {
                    f1.set(obj, strValTemp2);
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    logger.error("failed to set the value :{}", e1);
                }
            } else {
                //记录未特殊处理的入库字段
                logger.warn("failed to set the field:{},value:{}", field, value.toString(), e);
            }

            //方法①：特殊处理运行状态
			/*if ("runningStatus".equals(field)) {
				byte strValTemp2 = (byte)(int) strValTemp1;
				// 修改这个字段的值
				try {
					f1.set(obj,strValTemp2);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("failed to set the value :{}", e1);
				}
				//给对象赋值,值都转化成浮点型，让其自动转换,如果是开关机时间，转换成Long
			} else if ("turnOnTime".equals(field) || "turnOffTime".equals(field)) {
				long strValTemp2 = (long) strValTemp1;
				// 修改这个字段的值
				try {
					f1.set(obj,strValTemp2);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("failed to set the value :{}", e1);
				}
			} else {
				//记录未特殊处理的入库字段
				logger.warn("failed to set the field:{},value:{}",field,value.toString());
			}*/
        }

    }

    /**
     * @Title: instance4name
     * @Description: 根据类名获取实例
     * @return: Object
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月7日下午5:10:48
     */
    public static Object instance4name(String className) {

        try {
            Object newInstance = Class.forName(className).getConstructor().newInstance();
            return newInstance;
        } catch (Exception e) {
            logger.error("fail to get {}'s instance", className, e);
        }

        return null;
    }

    /**
     * @Title: specialProcess4Parse
     * @Description: 接收到遥测遥信值解析时特殊处理(处理遥信小数位)
     * @return: String
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月7日下午5:10:48
     */
    public static String specialProcess4Parse(String method, String sourceVal) {
        String resultVal = sourceVal;
        try {
            Class<?> class1 = Class.forName("com.synpowertech.dataCollectionJar.utils.DataProcessingUtil");
            Method method1 = class1.getMethod(method, String.class);
            method1.setAccessible(true);
            resultVal = (String) method1.invoke(null, sourceVal);
        } catch (Exception e) {
            logger.error("specialProcess4Parse failed：{}", e);
        }
        return resultVal;
    }

    /**
     * @Title: specialProcess4Parse
     * @Description: 接收到遥测遥信值解析时特殊处理(重载 ， 处理遥测表遥信)
     * @return: String
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月7日下午5:10:48
     */
    public static String specialProcess4Parse(String method, String sourceVal, Integer startBit, Integer endBit) {
        String resultVal = sourceVal;
        try {
            Class<?> class1 = Class.forName("com.synpowertech.dataCollectionJar.utils.DataProcessingUtil");
            Method method1 = class1.getMethod(method, String.class, Integer.class, Integer.class);
            method1.setAccessible(true);
            resultVal = (String) method1.invoke(null, sourceVal, startBit, endBit);
        } catch (Exception e) {
            logger.error("specialProcess4Parse failed：{}", e);
        }
        return resultVal;
    }


}
