package com.synpowertech.dataCollectionJar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ***************************************************************************
 * @Package: com.synpowertech.dataCollectionJar.utils
 * @ClassName: ByteProcess
 * @Description: TODO 十六进制字符串和十进制字符串转换工具类,涵盖有无符号数，最大8个字节
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年4月19日上午9:36:12   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
/**
 * @ClassName: RadixProcessUtil
 * @Description: TODO 十六进制字符串和十进制字符串转换工具类,涵盖有无符号数，最大8个字节
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年4月19日上午11:27:53   SP0010             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 *****************************************************************************
 */
public class RadixProcessUtil {

	private static final Logger logger = LoggerFactory.getLogger(RadixProcessUtil.class);
	
	private static String hexStr = "0123456789ABCDEF";
	private static String[] binaryArray = 
		{ "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000",
		  "1001", "1010", "1011", "1100", "1101", "1110", "1111" };

	/**
	  * @Title:  hexStr2BinArr 
	  * @Description: 将十六进制字符串转换为二进制字节数组
	  * @param hexString
	  * @return: byte[]
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月19日上午9:39:04
	 */
	@Deprecated
	public static byte[] hexStr2BinArr(String hexString) {
		// hexString的长度对2取整，作为bytes的长度
		int len = hexString.length() / 2;
		byte[] bytes = new byte[len];
		byte high = 0;// 字节高四位
		byte low = 0;// 字节低四位
		for (int i = 0; i < len; i++) {
			// 右移四位得到高位
			high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
			low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
			bytes[i] = (byte) (high | low);// 高地位做或运算
		}
		return bytes;
	}
	
	/**
	  * @Title:  bytes2BinStr 
	  * @Description:   二进制数组转换为二进制字符串
	  * @param bArray
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月19日上午9:39:24
	 */
	@Deprecated
	public static String bytes2BinStr(byte[] bArray) {

		String outStr = "";
		int pos = 0;
		for (byte b : bArray) {
			// 高四位
			pos = (b & 0xF0) >> 4;
			outStr += binaryArray[pos];
			// 低四位
			pos = b & 0x0F;
			outStr += binaryArray[pos];
		}
		return outStr;
	}


	/**
	 * 
	 * @param hexString
	 * @return 将十六进制字符串转换为二进制字符串
	 */
	@Deprecated
	public static String hexStr2BinStr(String hexString) {
		return bytes2BinStr(hexStr2BinArr(hexString));
	}
	
	/*****************************************************/
	
	/**
	  * @Title:  bin2HexStr 
	  * @Description:将二进制数组转换为十六进制字符串
	  * @param bytes
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月19日上午9:39:44
	 */
	@Deprecated
	public static String bin2HexStr(byte[] bytes) {

		String result = "";
		String hex = "";
		for (int i = 0; i < bytes.length; i++) {
			// 字节高4位
			hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
			// 字节低4位
			hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
			result += hex; // +" "
		}
		return result;
	}
	
	/************************************************/

	
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
							b[i] = b[i].equals("0")?"1":"0";
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
	
	/**********************************************************/
	/**
	  * @Title:  binaryStrToHexStr 
	  * @Description:  TODO 二进制字符串转换为十六进制字符串 ,二进制字符串位数必须满足是4的倍数 
	  * @param binaryStr
	  * @return: String
	  * @lastEditor:  SP0010
	  * @lastEdit:  2018年4月19日上午9:42:09
	 */
    public static String binaryStrToHexStr(String binaryStr) throws Exception {  
  
        if (binaryStr == null || binaryStr.equals("") || binaryStr.length() % 4 != 0) {  
            return null;  
        }  
  
        StringBuffer sbs = new StringBuffer();  
        // 二进制字符串是4的倍数，所以四位二进制转换成一位十六进制  
        for (int i = 0; i < binaryStr.length() / 4; i++) {  
            String subStr = binaryStr.substring(i * 4, i * 4 + 4);  
            String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));  
            sbs.append(hexStr);  
        }  
  
        return sbs.toString();  
    }  
    
    /**
      * @Title:  hexStrToBinaryStr 
      * @Description:  TODO 将十六进制的字符串转换成二进制的字符串 
      * @param hexString
      * @return: String
      * @lastEditor:  SP0010
      * @lastEdit:  2018年4月19日上午9:41:48
     */
    public static String hexStrToBinaryStr(String hexString) throws Exception {  
  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        StringBuffer sbu = new StringBuffer();  
        // 将每一个十六进制字符分别转换成一个四位的二进制字符  
        for (int i = 0; i < hexString.length(); i++) {  
            String indexStr = hexString.substring(i, i + 1);  
            String binaryStr = Integer.toBinaryString(Integer.parseInt(indexStr, 16));  
            while (binaryStr.length() < 4) {  
                binaryStr = "0" + binaryStr;  
            }  
            sbu.append(binaryStr);  
        }  
  
        return sbu.toString();  
    }  
	
    /****************************************************************/
    
    /**
      * @Title:  HexStr2Long 
      * @Description:  TODO 十六进制字符串转十进制数字，最大支持Long,即16位16进制字符串
      * @param hexStr
      * @param hasMark true表示有符号数，false表示无符号数
      * @return: Long
      * @lastEditor:  SP0010
      * @lastEdit:  2018年4月19日上午9:57:15
     */
    public static Long HexStr2Long(String hexStr,Boolean hasMark) {
    	Long target = null;
    	try {
			target = binaryToInteger(hexStrToBinaryStr(hexStr), hasMark);
		} catch (Exception e) {
			logger.info("HexStr2Int failed:{},{}",hexStr + "_" + hasMark,e.getClass());
		}
    	return target;
	}
    
    /**
      * @Title:  Long2HexStr 
      * @Description:  十进制数转化成16位十六进制字符串,支持long，默认转为16位
      * @param sourceVal
      * @return: String
      * @lastEditor:  SP0010
      * @lastEdit:  2018年4月19日上午10:06:08
     */
	public static String Long2HexStr(Long sourceVal) {
    	//实现1
		String  hexString = String.format("%016x", sourceVal);
		return hexString;
		//int length = hexString.length();
		//hexString = length > 4 ? hexString.substring(length - 4) : hexString;
		
		//实现2
		/*StringBuffer sbf = null ;
		String binaryStr = Long.toBinaryString(sourceVal);
		int tempNum = binaryStr.length() % 4;
		if (tempNum != 0) {
			for (int i = 0; i < (4 - tempNum); i++) {
				sbf.append("0");
			}
		}
		sbf.append(binaryStr);
		//②
		try {
			hexString = binaryStrToHexStr(sbf.toString());
		} catch (Exception e) {
			logger.info("binaryStrToHexStr failed:{},{}",sourceVal,e.getClass());
		}
		//处理成4位
   		*/ 
	}
    
}
