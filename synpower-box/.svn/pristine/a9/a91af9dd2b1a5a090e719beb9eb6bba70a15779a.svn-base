package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DataBattery implements Serializable {
    /**
     * 数据唯一标识
     */
    private Long id;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 电站
     */
    private Integer plant;

    /**
     * 数据时间戳
     */
    private Long dataTime;

    /**
     * 电压
     */
    private Float uCell;

    /**
     * 电流
     */
    private Float iCell;

    /**
     * 内阻
     */
    private Float innerResistance;

    /**
     * 温度
     */
    private Float tempCell;

    /**
     * 剩余容量
     */
    private Float leftCapacity;

    /**
     * 剩余容量百分比
     */
    private Float leftCapacityPercent;

    /**
     * SOC
     */
    private Float socCell;

    /**
     * 充放电状态
     */
    private Byte runningStatus;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPlant() {
        return plant;
    }

    public void setPlant(Integer plant) {
        this.plant = plant;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
    }

    public Float getuCell() {
        return uCell;
    }

    public void setuCell(Float uCell) {
        this.uCell = uCell;
    }

    public Float getiCell() {
        return iCell;
    }

    public void setiCell(Float iCell) {
        this.iCell = iCell;
    }

    public Float getInnerResistance() {
        return innerResistance;
    }

    public void setInnerResistance(Float innerResistance) {
        this.innerResistance = innerResistance;
    }

    public Float getTempCell() {
        return tempCell;
    }

    public void setTempCell(Float tempCell) {
        this.tempCell = tempCell;
    }

    public Float getLeftCapacity() {
        return leftCapacity;
    }

    public void setLeftCapacity(Float leftCapacity) {
        this.leftCapacity = leftCapacity;
    }

    public Float getLeftCapacityPercent() {
        return leftCapacityPercent;
    }

    public void setLeftCapacityPercent(Float leftCapacityPercent) {
        this.leftCapacityPercent = leftCapacityPercent;
    }

    public Float getSocCell() {
        return socCell;
    }

    public void setSocCell(Float socCell) {
        this.socCell = socCell;
    }

    public Byte getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(Byte runningStatus) {
        this.runningStatus = runningStatus;
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
        DataBattery other = (DataBattery) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getPlant() == null ? other.getPlant() == null : this.getPlant().equals(other.getPlant()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getuCell() == null ? other.getuCell() == null : this.getuCell().equals(other.getuCell()))
            && (this.getiCell() == null ? other.getiCell() == null : this.getiCell().equals(other.getiCell()))
            && (this.getInnerResistance() == null ? other.getInnerResistance() == null : this.getInnerResistance().equals(other.getInnerResistance()))
            && (this.getTempCell() == null ? other.getTempCell() == null : this.getTempCell().equals(other.getTempCell()))
            && (this.getLeftCapacity() == null ? other.getLeftCapacity() == null : this.getLeftCapacity().equals(other.getLeftCapacity()))
            && (this.getLeftCapacityPercent() == null ? other.getLeftCapacityPercent() == null : this.getLeftCapacityPercent().equals(other.getLeftCapacityPercent()))
            && (this.getSocCell() == null ? other.getSocCell() == null : this.getSocCell().equals(other.getSocCell()))
            && (this.getRunningStatus() == null ? other.getRunningStatus() == null : this.getRunningStatus().equals(other.getRunningStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getPlant() == null) ? 0 : getPlant().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        result = prime * result + ((getuCell() == null) ? 0 : getuCell().hashCode());
        result = prime * result + ((getiCell() == null) ? 0 : getiCell().hashCode());
        result = prime * result + ((getInnerResistance() == null) ? 0 : getInnerResistance().hashCode());
        result = prime * result + ((getTempCell() == null) ? 0 : getTempCell().hashCode());
        result = prime * result + ((getLeftCapacity() == null) ? 0 : getLeftCapacity().hashCode());
        result = prime * result + ((getLeftCapacityPercent() == null) ? 0 : getLeftCapacityPercent().hashCode());
        result = prime * result + ((getSocCell() == null) ? 0 : getSocCell().hashCode());
        result = prime * result + ((getRunningStatus() == null) ? 0 : getRunningStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", plant=").append(plant);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", uCell=").append(uCell);
        sb.append(", iCell=").append(iCell);
        sb.append(", innerResistance=").append(innerResistance);
        sb.append(", tempCell=").append(tempCell);
        sb.append(", leftCapacity=").append(leftCapacity);
        sb.append(", leftCapacityPercent=").append(leftCapacityPercent);
        sb.append(", socCell=").append(socCell);
        sb.append(", runningStatus=").append(runningStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}