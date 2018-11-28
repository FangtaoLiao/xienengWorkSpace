package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DataBoxChange implements Serializable {
    /**
     * 箱变唯一标识
     */
    private Integer id;

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 电站id
     */
    private Integer plant;

    /**
     * 数据时间戳
     */
    private Long dataTime;

    /**
     * 运行状态
     */
    private Byte runningStatus;

    /**
     * 温度
     */
    private Float innerTemperature;

    /**
     * A箱交流电压
     */
    private Float uAcA;

    /**
     * B箱交流电压
     */
    private Float uAcB;

    /**
     * C箱交流电压
     */
    private Float uAcC;

    /**
     * 电网AB线电压
     */
    private Float uLineAb;

    /**
     * 电网BC线电压
     */
    private Float uLineBc;

    /**
     * 电网CA线电压
     */
    private Float uLineCa;

    /**
     * 电网A相电流(IA)
     */
    private Float iAcA;

    /**
     * 电网B相电流(IA)
     */
    private Float iAcB;

    /**
     * 电网C相电流(IA)
     */
    private Float iAcC;

    /**
     * 有功功率
     */
    private Float activePower;

    /**
     * 无功功率
     */
    private Float reactivePower;

    /**
     * 功率因数
     */
    private Float powerFactor;

    /**
     * 电网频率
     */
    private Float frequency;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Byte getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(Byte runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Float getInnerTemperature() {
        return innerTemperature;
    }

    public void setInnerTemperature(Float innerTemperature) {
        this.innerTemperature = innerTemperature;
    }

    public Float getuAcA() {
        return uAcA;
    }

    public void setuAcA(Float uAcA) {
        this.uAcA = uAcA;
    }

    public Float getuAcB() {
        return uAcB;
    }

    public void setuAcB(Float uAcB) {
        this.uAcB = uAcB;
    }

    public Float getuAcC() {
        return uAcC;
    }

    public void setuAcC(Float uAcC) {
        this.uAcC = uAcC;
    }

    public Float getuLineAb() {
        return uLineAb;
    }

    public void setuLineAb(Float uLineAb) {
        this.uLineAb = uLineAb;
    }

    public Float getuLineBc() {
        return uLineBc;
    }

    public void setuLineBc(Float uLineBc) {
        this.uLineBc = uLineBc;
    }

    public Float getuLineCa() {
        return uLineCa;
    }

    public void setuLineCa(Float uLineCa) {
        this.uLineCa = uLineCa;
    }

    public Float getiAcA() {
        return iAcA;
    }

    public void setiAcA(Float iAcA) {
        this.iAcA = iAcA;
    }

    public Float getiAcB() {
        return iAcB;
    }

    public void setiAcB(Float iAcB) {
        this.iAcB = iAcB;
    }

    public Float getiAcC() {
        return iAcC;
    }

    public void setiAcC(Float iAcC) {
        this.iAcC = iAcC;
    }

    public Float getActivePower() {
        return activePower;
    }

    public void setActivePower(Float activePower) {
        this.activePower = activePower;
    }

    public Float getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(Float reactivePower) {
        this.reactivePower = reactivePower;
    }

    public Float getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(Float powerFactor) {
        this.powerFactor = powerFactor;
    }

    public Float getFrequency() {
        return frequency;
    }

    public void setFrequency(Float frequency) {
        this.frequency = frequency;
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
        DataBoxChange other = (DataBoxChange) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getPlant() == null ? other.getPlant() == null : this.getPlant().equals(other.getPlant()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getRunningStatus() == null ? other.getRunningStatus() == null : this.getRunningStatus().equals(other.getRunningStatus()))
            && (this.getInnerTemperature() == null ? other.getInnerTemperature() == null : this.getInnerTemperature().equals(other.getInnerTemperature()))
            && (this.getuAcA() == null ? other.getuAcA() == null : this.getuAcA().equals(other.getuAcA()))
            && (this.getuAcB() == null ? other.getuAcB() == null : this.getuAcB().equals(other.getuAcB()))
            && (this.getuAcC() == null ? other.getuAcC() == null : this.getuAcC().equals(other.getuAcC()))
            && (this.getuLineAb() == null ? other.getuLineAb() == null : this.getuLineAb().equals(other.getuLineAb()))
            && (this.getuLineBc() == null ? other.getuLineBc() == null : this.getuLineBc().equals(other.getuLineBc()))
            && (this.getuLineCa() == null ? other.getuLineCa() == null : this.getuLineCa().equals(other.getuLineCa()))
            && (this.getiAcA() == null ? other.getiAcA() == null : this.getiAcA().equals(other.getiAcA()))
            && (this.getiAcB() == null ? other.getiAcB() == null : this.getiAcB().equals(other.getiAcB()))
            && (this.getiAcC() == null ? other.getiAcC() == null : this.getiAcC().equals(other.getiAcC()))
            && (this.getActivePower() == null ? other.getActivePower() == null : this.getActivePower().equals(other.getActivePower()))
            && (this.getReactivePower() == null ? other.getReactivePower() == null : this.getReactivePower().equals(other.getReactivePower()))
            && (this.getPowerFactor() == null ? other.getPowerFactor() == null : this.getPowerFactor().equals(other.getPowerFactor()))
            && (this.getFrequency() == null ? other.getFrequency() == null : this.getFrequency().equals(other.getFrequency()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getPlant() == null) ? 0 : getPlant().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        result = prime * result + ((getRunningStatus() == null) ? 0 : getRunningStatus().hashCode());
        result = prime * result + ((getInnerTemperature() == null) ? 0 : getInnerTemperature().hashCode());
        result = prime * result + ((getuAcA() == null) ? 0 : getuAcA().hashCode());
        result = prime * result + ((getuAcB() == null) ? 0 : getuAcB().hashCode());
        result = prime * result + ((getuAcC() == null) ? 0 : getuAcC().hashCode());
        result = prime * result + ((getuLineAb() == null) ? 0 : getuLineAb().hashCode());
        result = prime * result + ((getuLineBc() == null) ? 0 : getuLineBc().hashCode());
        result = prime * result + ((getuLineCa() == null) ? 0 : getuLineCa().hashCode());
        result = prime * result + ((getiAcA() == null) ? 0 : getiAcA().hashCode());
        result = prime * result + ((getiAcB() == null) ? 0 : getiAcB().hashCode());
        result = prime * result + ((getiAcC() == null) ? 0 : getiAcC().hashCode());
        result = prime * result + ((getActivePower() == null) ? 0 : getActivePower().hashCode());
        result = prime * result + ((getReactivePower() == null) ? 0 : getReactivePower().hashCode());
        result = prime * result + ((getPowerFactor() == null) ? 0 : getPowerFactor().hashCode());
        result = prime * result + ((getFrequency() == null) ? 0 : getFrequency().hashCode());
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
        sb.append(", runningStatus=").append(runningStatus);
        sb.append(", innerTemperature=").append(innerTemperature);
        sb.append(", uAcA=").append(uAcA);
        sb.append(", uAcB=").append(uAcB);
        sb.append(", uAcC=").append(uAcC);
        sb.append(", uLineAb=").append(uLineAb);
        sb.append(", uLineBc=").append(uLineBc);
        sb.append(", uLineCa=").append(uLineCa);
        sb.append(", iAcA=").append(iAcA);
        sb.append(", iAcB=").append(iAcB);
        sb.append(", iAcC=").append(iAcC);
        sb.append(", activePower=").append(activePower);
        sb.append(", reactivePower=").append(reactivePower);
        sb.append(", powerFactor=").append(powerFactor);
        sb.append(", frequency=").append(frequency);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}