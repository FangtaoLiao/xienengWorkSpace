package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollYxExpand implements Serializable {
    /**
     * 信号点的唯一标识
     */
    private Integer id;

    /**
     * 信号点全局唯一标识
     */
    private String signalGuid;

    /**
     * 名称
     */
    private String signalName;

    /**
     * 对应coll_model
     */
    private Integer dataModel;

    /**
     * 1表示遥测告警，0表示不是遥测告警
     */
    private Integer ycAlarm;

    /**
     * 告警bit位
     */
    private Integer alarmBit;

    /**
     * 告警bit位长度，从低位向高位算
     */
    private Integer alarmBitLength;

    /**
     * 告警类型，0表示事件，1表示告警
     */
    private String alarmType;

    /**
     * 状态名（遥信变位状态）
     */
    private String statusName;

    /**
     * 状态值
     */
    private Integer statusValue;

    /**
     * 告警级别
     */
    private String alarmLevel;

    /**
     * 告警建议
     */
    private String suggest;

    /**
     * 告警原因
     */
    private String alarmReason;

    /**
     * 是否故障点,0表示正常，1表示故障点
     */
    private String faultPoint;

    /**
     * 是否有效
     */
    private String valid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSignalGuid() {
        return signalGuid;
    }

    public void setSignalGuid(String signalGuid) {
        this.signalGuid = signalGuid;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Integer getDataModel() {
        return dataModel;
    }

    public void setDataModel(Integer dataModel) {
        this.dataModel = dataModel;
    }

    public Integer getYcAlarm() {
        return ycAlarm;
    }

    public void setYcAlarm(Integer ycAlarm) {
        this.ycAlarm = ycAlarm;
    }

    public Integer getAlarmBit() {
        return alarmBit;
    }

    public void setAlarmBit(Integer alarmBit) {
        this.alarmBit = alarmBit;
    }

    public Integer getAlarmBitLength() {
        return alarmBitLength;
    }

    public void setAlarmBitLength(Integer alarmBitLength) {
        this.alarmBitLength = alarmBitLength;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getAlarmReason() {
        return alarmReason;
    }

    public void setAlarmReason(String alarmReason) {
        this.alarmReason = alarmReason;
    }

    public String getFaultPoint() {
        return faultPoint;
    }

    public void setFaultPoint(String faultPoint) {
        this.faultPoint = faultPoint;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
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
        CollYxExpand other = (CollYxExpand) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSignalGuid() == null ? other.getSignalGuid() == null : this.getSignalGuid().equals(other.getSignalGuid()))
            && (this.getSignalName() == null ? other.getSignalName() == null : this.getSignalName().equals(other.getSignalName()))
            && (this.getDataModel() == null ? other.getDataModel() == null : this.getDataModel().equals(other.getDataModel()))
            && (this.getYcAlarm() == null ? other.getYcAlarm() == null : this.getYcAlarm().equals(other.getYcAlarm()))
            && (this.getAlarmBit() == null ? other.getAlarmBit() == null : this.getAlarmBit().equals(other.getAlarmBit()))
            && (this.getAlarmBitLength() == null ? other.getAlarmBitLength() == null : this.getAlarmBitLength().equals(other.getAlarmBitLength()))
            && (this.getAlarmType() == null ? other.getAlarmType() == null : this.getAlarmType().equals(other.getAlarmType()))
            && (this.getStatusName() == null ? other.getStatusName() == null : this.getStatusName().equals(other.getStatusName()))
            && (this.getStatusValue() == null ? other.getStatusValue() == null : this.getStatusValue().equals(other.getStatusValue()))
            && (this.getAlarmLevel() == null ? other.getAlarmLevel() == null : this.getAlarmLevel().equals(other.getAlarmLevel()))
            && (this.getSuggest() == null ? other.getSuggest() == null : this.getSuggest().equals(other.getSuggest()))
            && (this.getAlarmReason() == null ? other.getAlarmReason() == null : this.getAlarmReason().equals(other.getAlarmReason()))
            && (this.getFaultPoint() == null ? other.getFaultPoint() == null : this.getFaultPoint().equals(other.getFaultPoint()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSignalGuid() == null) ? 0 : getSignalGuid().hashCode());
        result = prime * result + ((getSignalName() == null) ? 0 : getSignalName().hashCode());
        result = prime * result + ((getDataModel() == null) ? 0 : getDataModel().hashCode());
        result = prime * result + ((getYcAlarm() == null) ? 0 : getYcAlarm().hashCode());
        result = prime * result + ((getAlarmBit() == null) ? 0 : getAlarmBit().hashCode());
        result = prime * result + ((getAlarmBitLength() == null) ? 0 : getAlarmBitLength().hashCode());
        result = prime * result + ((getAlarmType() == null) ? 0 : getAlarmType().hashCode());
        result = prime * result + ((getStatusName() == null) ? 0 : getStatusName().hashCode());
        result = prime * result + ((getStatusValue() == null) ? 0 : getStatusValue().hashCode());
        result = prime * result + ((getAlarmLevel() == null) ? 0 : getAlarmLevel().hashCode());
        result = prime * result + ((getSuggest() == null) ? 0 : getSuggest().hashCode());
        result = prime * result + ((getAlarmReason() == null) ? 0 : getAlarmReason().hashCode());
        result = prime * result + ((getFaultPoint() == null) ? 0 : getFaultPoint().hashCode());
        result = prime * result + ((getValid() == null) ? 0 : getValid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", signalGuid=").append(signalGuid);
        sb.append(", signalName=").append(signalName);
        sb.append(", dataModel=").append(dataModel);
        sb.append(", ycAlarm=").append(ycAlarm);
        sb.append(", alarmBit=").append(alarmBit);
        sb.append(", alarmBitLength=").append(alarmBitLength);
        sb.append(", alarmType=").append(alarmType);
        sb.append(", statusName=").append(statusName);
        sb.append(", statusValue=").append(statusValue);
        sb.append(", alarmLevel=").append(alarmLevel);
        sb.append(", suggest=").append(suggest);
        sb.append(", alarmReason=").append(alarmReason);
        sb.append(", faultPoint=").append(faultPoint);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}