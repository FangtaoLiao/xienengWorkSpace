package com.synpowertech.dataCollectionJar.utils;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.utils
 * @ClassName: Table2modelUtil
 * @Description: 表名与实体名，字段名与属性名转化工具类
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月7日下午4:04:19   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class Table2modelUtil {

	/**
	  * @Title:  table2model 
	  * @Description:  表名转化实体名
	  * @param tableName
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月7日下午4:07:43
	 */
	public static String table2model(String tableName){
		
		String[] strings = tableName.split("_");
		StringBuffer modelStr = new StringBuffer();
		if (strings.length > 1) {
			for (int i = 0; i < strings.length; i++) {
				modelStr.append(upperCaseFirstLatter(strings[i]));
			}
			return "com.synpowertech.dataCollectionJar.domain." +  modelStr.toString();
		} else {
			return "com.synpowertech.dataCollectionJar.domain." +  upperCaseFirstLatter(tableName);
		}
	}
	
	/**
	  * @Title:  field2Attr 
	  * @Description: 数据库字段名转化实体属性名
	  * @param fieldName
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月7日下午4:08:13
	 */
	public static String field2Attr(String fieldName){
		String[] strings = fieldName.split("_");
		StringBuffer attrStr = new StringBuffer();
		if (strings.length > 1) {
			for (int i = 0; i < strings.length; i++) {
				if (i == 0) {
					attrStr.append(strings[i]);
				} else {
					attrStr.append(upperCaseFirstLatter(strings[i]));
				}
				
			}
			return attrStr.toString();
		} else {
			return fieldName;
		}
	}
	
	/**
	  * @Title:  upperCaseFirstLatter 
	  * @Description: 字符串首字母大写
	  * @param str
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月7日下午4:26:23
	 */
	public static String upperCaseFirstLatter(String str){  
	    char[] strChar=str.toCharArray();  
	    strChar[0]-=32;  
	    return String.valueOf(strChar);  
	}  
	

	/**
	  * @Title:  lowerCaseFirstLatter 
	  * @Description: 字符串首字母小写
	  * @param str
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2017年11月17日上午11:55:05
	 */
	public static String lowerCaseFirstLatter(String str){  
		char[] strChar=str.toCharArray();  
		strChar[0]+=32;  
		return String.valueOf(strChar);  
	}  
}
