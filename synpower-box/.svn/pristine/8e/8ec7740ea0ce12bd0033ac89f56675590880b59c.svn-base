package com.synpower.util;

import java.io.UnsupportedEncodingException;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/*****************************************************************************
 * @Package: com.synpower.util
 * ClassName: ChineseToEnglish
 * @Description: 汉字转拼音
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月20日下午2:10:39   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
public final class ChineseToEnglish {  
	   public static String getPingYin(String src) {  
		   
	        char[] t1 = null;  
	        t1 = src.toCharArray();  
	        String[] t2 = new String[t1.length];  
	        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();  
	          
	        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
	        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
	        t3.setVCharType(HanyuPinyinVCharType.WITH_V);  
	        String t4 = "";  
	        int t0 = t1.length;  
	        try {  
	            for (int i = 0; i < t0; i++) {  
	                // 判断是否为汉字字符  
	                if (java.lang.Character.toString(t1[i]).matches(  
	                        "[\\u4E00-\\u9FA5]+")) {  
	                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
	                    t4 += t2[0];  
	                } else  
	                    t4 += java.lang.Character.toString(t1[i]);  
	            }  
	            // System.out.println(t4);  
	            return t4;  
	        } catch (BadHanyuPinyinOutputFormatCombination e1) {  
	            e1.printStackTrace();  
	        }  
	        return t4;  
	    }  
	  
	    // 返回中文的首字母  
	    public static String getPinYinHeadChar(String str) {  
	  
	        String convert = "";  
	        for (int j = 0; j < str.length(); j++) {  
	            char word = str.charAt(j);  
	            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
	            if (pinyinArray != null) {  
	                convert += pinyinArray[0].charAt(0);  
	            } else {  
	                convert += word;  
	            }  
	        }  
	        return convert;  
	    }
    /** 
     * 字符串编码转换 
     * @param str 要转换编码的字符串 
     * @param charsetName 原来的编码 
     * @param toCharsetName 转换后的编码 
     * @return 经过编码转换后的字符串 
     */  
    private String conversionStr(String str, String charsetName,String toCharsetName) {  
        try {  
            str = new String(str.getBytes(charsetName), toCharsetName);  
        } catch (UnsupportedEncodingException ex) {  
            System.out.println("字符串编码转换异常：" + ex.getMessage());  
        }  
        return str;  
    }  
  
}  
