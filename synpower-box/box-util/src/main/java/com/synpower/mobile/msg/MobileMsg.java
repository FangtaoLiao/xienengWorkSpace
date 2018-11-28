package com.synpower.mobile.msg;


import org.apache.commons.lang.StringUtils;


import java.util.Set;

/**
 * 手机短信消息
 * Created by SP0013 on 2017/11/1.
 */
public class MobileMsg {

    /**
     * 手机号,可以批量发,一次最多500个
     */
    private Set<String> telNo;
    /**
     * 手机短信内容
     */
    private String content;
    /**
     * 发送时间,固定14位长度字符串，比如：20060912152435代表2006年9月12日15时24分35秒，为空表示立即发送
     */
    private String sendTime;

    public Set<String> getTelNo() {
        return telNo;
    }

    public void setTelNo(Set<String> telNo) {
        this.telNo = telNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        boolean isEmpty = telNo==null || telNo.isEmpty();
        if(StringUtils.isBlank(content) || isEmpty) {
            return "-1";
        }
        return super.toString();
    }
}
