package com.synpower.mobile;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SmsException;
import com.synpower.mobile.msg.MobileMsg;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 短信操作命令类，可以随时扩展，目前只做了发送，支持定时发送，
 * 但必须再MobileMsg里面的sendTime字段设置固定14位长度字符串，
 * 比如：20060912152435代表2006年9月12日15时24分35秒，为空表示立即发送,
 * 先不加多线程
 * Created by SP0013 on 2017/11/1.
 */
public class SmsOper {
    /**
     * 电话最大批量数
     */
    private static final int BATCH_TELNO = 500;
    /**
     * 验证错误代码
     */
    private static final String ERROR_CODE = "-1";
    private static final int CONTENT_LENGTH = 300;
    /**
     * 发送短信
     * @param corpId 短信用户名
     * @param pwd 短信登录密码
     * @param urlLink 短信访问地址
     * @param mobileMsg 短信内容
     * @return 短信状态码
     * @throws IOException 
     * @throws Exception 异常
     */
    public int sendSMS(String corpId,String pwd,String urlLink,MobileMsg mobileMsg) throws ServiceException, SmsException, IOException {
        if(StringUtils.isBlank(corpId) || StringUtils.isBlank(pwd) || StringUtils.isBlank(urlLink)) {
            throw new NullPointerException("参数corpId,pwd,urlLink为空,请检查参数设置!");
        }
        if(mobileMsg==null || ERROR_CODE.equals(mobileMsg.toString())) {
            throw new SmsException("参数mobileMsg为空，或者里面的属性未填充!");
        }
        /**
         * 超过消息内容长度
         */
        if(mobileMsg.getContent().length() > CONTENT_LENGTH) {
            return -8;
        }
        URL url;
        String send_content= URLEncoder.encode(mobileMsg.getContent().replaceAll("<br/>", " "), "GB2312");
        String telNos = splitTelNo(mobileMsg.getTelNo());
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(urlLink);
		urlBuilder.append("?CorpID=");
		urlBuilder.append(corpId);
		urlBuilder.append("&Pwd=");
		urlBuilder.append(pwd);
		urlBuilder.append("&Mobile=");
		urlBuilder.append(telNos);
		urlBuilder.append("&Content=");
		urlBuilder.append(send_content);
		urlBuilder.append("&Cell=");
        urlBuilder.append("&SendTime=");
        urlBuilder.append(mobileMsg.getSendTime());
		url = new URL(urlBuilder.toString());
        BufferedReader in = null;
		int msgCode;
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
            String code  = in.readLine();
            if(StringUtils.isBlank(code)) {
                msgCode = -2;
            } else {
                msgCode = Integer.parseInt(code);
            }
		} catch (Exception e) {
			throw new SmsException(e,"短信发送异常");
		} finally {
		    in.close();
        }
        return msgCode;
    }

    /**
     * 拆分群发手机号，以英文,为间隔
     * @param telNos 手机号码集
     * @return 拆分组装后的手机号码，发送手机号码（号码之间用英文逗号隔开，建议500个号码） 例如：
     * 13812345678,13519876543,15812349876
     */
    private String splitTelNo(Set<String> telNos) throws ServiceException {
        /**
         * 文档建议500个电话号码为群发极限
         */
        if(telNos.size() > BATCH_TELNO) {
                throw new ServiceException("目前只支持群发500个号码");
        }
        StringBuilder stringBuilder = new StringBuilder();
        /**
         * 拼接成以英文,为间隔的手机号字符串
         */
        Iterator<String> telNoIterator = telNos.iterator();
        while (telNoIterator.hasNext()) {
            stringBuilder.append(telNoIterator.next());
            stringBuilder.append(",");
        }
        telNoIterator = null;
        return stringBuilder.toString();
    }
}
