package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class AlertorSetting implements Serializable {
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 永久关闭声音
     */
    private String soundCloseEver;

    /**
     * 永久关闭告警设备
     */
    private String deviceCloseEver;

    /**
     * 串口号
     */
    private String serialPort;

    /**
     * 设备型号id
     */
    private Integer deviceDetailId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSoundCloseEver() {
        return soundCloseEver;
    }

    public void setSoundCloseEver(String soundCloseEver) {
        this.soundCloseEver = soundCloseEver;
    }

    public String getDeviceCloseEver() {
        return deviceCloseEver;
    }

    public void setDeviceCloseEver(String deviceCloseEver) {
        this.deviceCloseEver = deviceCloseEver;
    }

    public String getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }

    public Integer getDeviceDetailId() {
        return deviceDetailId;
    }

    public void setDeviceDetailId(Integer deviceDetailId) {
        this.deviceDetailId = deviceDetailId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AlertorSetting other = (AlertorSetting) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getSoundCloseEver() == null ? other.getSoundCloseEver() == null : this.getSoundCloseEver().equals(other.getSoundCloseEver()))
            && (this.getDeviceCloseEver() == null ? other.getDeviceCloseEver() == null : this.getDeviceCloseEver().equals(other.getDeviceCloseEver()))
            && (this.getSerialPort() == null ? other.getSerialPort() == null : this.getSerialPort().equals(other.getSerialPort()))
            && (this.getDeviceDetailId() == null ? other.getDeviceDetailId() == null : this.getDeviceDetailId().equals(other.getDeviceDetailId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getSoundCloseEver() == null) ? 0 : getSoundCloseEver().hashCode());
        result = prime * result + ((getDeviceCloseEver() == null) ? 0 : getDeviceCloseEver().hashCode());
        result = prime * result + ((getSerialPort() == null) ? 0 : getSerialPort().hashCode());
        result = prime * result + ((getDeviceDetailId() == null) ? 0 : getDeviceDetailId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", soundCloseEver=").append(soundCloseEver);
        sb.append(", deviceCloseEver=").append(deviceCloseEver);
        sb.append(", serialPort=").append(serialPort);
        sb.append(", deviceDetailId=").append(deviceDetailId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}