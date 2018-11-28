package com.synpowertech.dataCollectionJar.domain;

public class CollJsonDatacollSet {
    private Long id;

    private String name;

    private Long collId;

    private String reset;

    private String reboot;

    private String connAddr;

    private String userId;

    private String pwd;

    private String sn;

    private String timeOut;

    private String heartBeat;

    private String sendFreq;

    private String sendPeriod;

    private String valid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getCollId() {
        return collId;
    }

    public void setCollId(Long collId) {
        this.collId = collId;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset == null ? null : reset.trim();
    }

    public String getReboot() {
        return reboot;
    }

    public void setReboot(String reboot) {
        this.reboot = reboot == null ? null : reboot.trim();
    }

    public String getConnAddr() {
        return connAddr;
    }

    public void setConnAddr(String connAddr) {
        this.connAddr = connAddr == null ? null : connAddr.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut == null ? null : timeOut.trim();
    }

    public String getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat == null ? null : heartBeat.trim();
    }

    public String getSendFreq() {
        return sendFreq;
    }

    public void setSendFreq(String sendFreq) {
        this.sendFreq = sendFreq == null ? null : sendFreq.trim();
    }

    public String getSendPeriod() {
        return sendPeriod;
    }

    public void setSendPeriod(String sendPeriod) {
        this.sendPeriod = sendPeriod == null ? null : sendPeriod.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }
}