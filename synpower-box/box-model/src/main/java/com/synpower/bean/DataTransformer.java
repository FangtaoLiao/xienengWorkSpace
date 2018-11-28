package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DataTransformer implements Serializable {
    /**
     * 数据唯一标识
     */
    private Integer id;

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
     * 电网A相交流电压
     */
    private Float uOutputA;

    /**
     * 电网B相交流电压
     */
    private Float uOutputB;

    /**
     * 电网C相交流电压
     */
    private Float uOutputC;

    /**
     * 电网A相交流电流
     */
    private Float iOutputA;

    /**
     * 电网B相交流电流
     */
    private Float iOutputB;

    /**
     * 电网C相交流电流
     */
    private Float iOutputC;

    /**
     * 运行状态
     */
    private Byte runningStatus;

    /**
     * 电网频率
     */
    private Float gridFrequency;

    /**
     * 有功功率
     */
    private Float activePower;

    /**
     * 无功功率
     */
    private Float reactivePower;

    /**
     * 转换效率（厂家）
     */
    private Float transferEfficiency;

    /**
     * 功率因数
     */
    private Float powerFactor;

    /**
     * 温度
     */
    private Float innerTemperature;

    /**
     * 第二路A相输入交流电压
     */
    private Float uInputA2;

    /**
     * 第二路B相输入交流电压
     */
    private Float uInputB2;

    /**
     * 第二路C相输入交流电压
     */
    private Float uInputC2;

    /**
     * 第二路A相输入交流电流
     */
    private Float iInputA2;

    /**
     * 第二路B相输入交流电流
     */
    private Float iInputB2;

    /**
     * 第二路C相输入交流电流
     */
    private Float iInputC2;

    /**
     * 第一路A相输入交流电压
     */
    private Float uInputA1;

    /**
     * 第一路B相输入交流电压
     */
    private Float uInputB1;

    /**
     * 第一路C相输入交流电压
     */
    private Float uInputC1;

    /**
     * 第一路A相输入交流电流
     */
    private Float iInputA1;

    /**
     * 第一路B相输入交流电流
     */
    private Float iInputB1;

    /**
     * 第一路C相输入交流电流
     */
    private Float iInputC1;

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

    public Float getuOutputA() {
        return uOutputA;
    }

    public void setuOutputA(Float uOutputA) {
        this.uOutputA = uOutputA;
    }

    public Float getuOutputB() {
        return uOutputB;
    }

    public void setuOutputB(Float uOutputB) {
        this.uOutputB = uOutputB;
    }

    public Float getuOutputC() {
        return uOutputC;
    }

    public void setuOutputC(Float uOutputC) {
        this.uOutputC = uOutputC;
    }

    public Float getiOutputA() {
        return iOutputA;
    }

    public void setiOutputA(Float iOutputA) {
        this.iOutputA = iOutputA;
    }

    public Float getiOutputB() {
        return iOutputB;
    }

    public void setiOutputB(Float iOutputB) {
        this.iOutputB = iOutputB;
    }

    public Float getiOutputC() {
        return iOutputC;
    }

    public void setiOutputC(Float iOutputC) {
        this.iOutputC = iOutputC;
    }

    public Byte getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(Byte runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Float getGridFrequency() {
        return gridFrequency;
    }

    public void setGridFrequency(Float gridFrequency) {
        this.gridFrequency = gridFrequency;
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

    public Float getTransferEfficiency() {
        return transferEfficiency;
    }

    public void setTransferEfficiency(Float transferEfficiency) {
        this.transferEfficiency = transferEfficiency;
    }

    public Float getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(Float powerFactor) {
        this.powerFactor = powerFactor;
    }

    public Float getInnerTemperature() {
        return innerTemperature;
    }

    public void setInnerTemperature(Float innerTemperature) {
        this.innerTemperature = innerTemperature;
    }

    public Float getuInputA2() {
        return uInputA2;
    }

    public void setuInputA2(Float uInputA2) {
        this.uInputA2 = uInputA2;
    }

    public Float getuInputB2() {
        return uInputB2;
    }

    public void setuInputB2(Float uInputB2) {
        this.uInputB2 = uInputB2;
    }

    public Float getuInputC2() {
        return uInputC2;
    }

    public void setuInputC2(Float uInputC2) {
        this.uInputC2 = uInputC2;
    }

    public Float getiInputA2() {
        return iInputA2;
    }

    public void setiInputA2(Float iInputA2) {
        this.iInputA2 = iInputA2;
    }

    public Float getiInputB2() {
        return iInputB2;
    }

    public void setiInputB2(Float iInputB2) {
        this.iInputB2 = iInputB2;
    }

    public Float getiInputC2() {
        return iInputC2;
    }

    public void setiInputC2(Float iInputC2) {
        this.iInputC2 = iInputC2;
    }

    public Float getuInputA1() {
        return uInputA1;
    }

    public void setuInputA1(Float uInputA1) {
        this.uInputA1 = uInputA1;
    }

    public Float getuInputB1() {
        return uInputB1;
    }

    public void setuInputB1(Float uInputB1) {
        this.uInputB1 = uInputB1;
    }

    public Float getuInputC1() {
        return uInputC1;
    }

    public void setuInputC1(Float uInputC1) {
        this.uInputC1 = uInputC1;
    }

    public Float getiInputA1() {
        return iInputA1;
    }

    public void setiInputA1(Float iInputA1) {
        this.iInputA1 = iInputA1;
    }

    public Float getiInputB1() {
        return iInputB1;
    }

    public void setiInputB1(Float iInputB1) {
        this.iInputB1 = iInputB1;
    }

    public Float getiInputC1() {
        return iInputC1;
    }

    public void setiInputC1(Float iInputC1) {
        this.iInputC1 = iInputC1;
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
        DataTransformer other = (DataTransformer) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getPlant() == null ? other.getPlant() == null : this.getPlant().equals(other.getPlant()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getuOutputA() == null ? other.getuOutputA() == null : this.getuOutputA().equals(other.getuOutputA()))
            && (this.getuOutputB() == null ? other.getuOutputB() == null : this.getuOutputB().equals(other.getuOutputB()))
            && (this.getuOutputC() == null ? other.getuOutputC() == null : this.getuOutputC().equals(other.getuOutputC()))
            && (this.getiOutputA() == null ? other.getiOutputA() == null : this.getiOutputA().equals(other.getiOutputA()))
            && (this.getiOutputB() == null ? other.getiOutputB() == null : this.getiOutputB().equals(other.getiOutputB()))
            && (this.getiOutputC() == null ? other.getiOutputC() == null : this.getiOutputC().equals(other.getiOutputC()))
            && (this.getRunningStatus() == null ? other.getRunningStatus() == null : this.getRunningStatus().equals(other.getRunningStatus()))
            && (this.getGridFrequency() == null ? other.getGridFrequency() == null : this.getGridFrequency().equals(other.getGridFrequency()))
            && (this.getActivePower() == null ? other.getActivePower() == null : this.getActivePower().equals(other.getActivePower()))
            && (this.getReactivePower() == null ? other.getReactivePower() == null : this.getReactivePower().equals(other.getReactivePower()))
            && (this.getTransferEfficiency() == null ? other.getTransferEfficiency() == null : this.getTransferEfficiency().equals(other.getTransferEfficiency()))
            && (this.getPowerFactor() == null ? other.getPowerFactor() == null : this.getPowerFactor().equals(other.getPowerFactor()))
            && (this.getInnerTemperature() == null ? other.getInnerTemperature() == null : this.getInnerTemperature().equals(other.getInnerTemperature()))
            && (this.getuInputA2() == null ? other.getuInputA2() == null : this.getuInputA2().equals(other.getuInputA2()))
            && (this.getuInputB2() == null ? other.getuInputB2() == null : this.getuInputB2().equals(other.getuInputB2()))
            && (this.getuInputC2() == null ? other.getuInputC2() == null : this.getuInputC2().equals(other.getuInputC2()))
            && (this.getiInputA2() == null ? other.getiInputA2() == null : this.getiInputA2().equals(other.getiInputA2()))
            && (this.getiInputB2() == null ? other.getiInputB2() == null : this.getiInputB2().equals(other.getiInputB2()))
            && (this.getiInputC2() == null ? other.getiInputC2() == null : this.getiInputC2().equals(other.getiInputC2()))
            && (this.getuInputA1() == null ? other.getuInputA1() == null : this.getuInputA1().equals(other.getuInputA1()))
            && (this.getuInputB1() == null ? other.getuInputB1() == null : this.getuInputB1().equals(other.getuInputB1()))
            && (this.getuInputC1() == null ? other.getuInputC1() == null : this.getuInputC1().equals(other.getuInputC1()))
            && (this.getiInputA1() == null ? other.getiInputA1() == null : this.getiInputA1().equals(other.getiInputA1()))
            && (this.getiInputB1() == null ? other.getiInputB1() == null : this.getiInputB1().equals(other.getiInputB1()))
            && (this.getiInputC1() == null ? other.getiInputC1() == null : this.getiInputC1().equals(other.getiInputC1()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getPlant() == null) ? 0 : getPlant().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        result = prime * result + ((getuOutputA() == null) ? 0 : getuOutputA().hashCode());
        result = prime * result + ((getuOutputB() == null) ? 0 : getuOutputB().hashCode());
        result = prime * result + ((getuOutputC() == null) ? 0 : getuOutputC().hashCode());
        result = prime * result + ((getiOutputA() == null) ? 0 : getiOutputA().hashCode());
        result = prime * result + ((getiOutputB() == null) ? 0 : getiOutputB().hashCode());
        result = prime * result + ((getiOutputC() == null) ? 0 : getiOutputC().hashCode());
        result = prime * result + ((getRunningStatus() == null) ? 0 : getRunningStatus().hashCode());
        result = prime * result + ((getGridFrequency() == null) ? 0 : getGridFrequency().hashCode());
        result = prime * result + ((getActivePower() == null) ? 0 : getActivePower().hashCode());
        result = prime * result + ((getReactivePower() == null) ? 0 : getReactivePower().hashCode());
        result = prime * result + ((getTransferEfficiency() == null) ? 0 : getTransferEfficiency().hashCode());
        result = prime * result + ((getPowerFactor() == null) ? 0 : getPowerFactor().hashCode());
        result = prime * result + ((getInnerTemperature() == null) ? 0 : getInnerTemperature().hashCode());
        result = prime * result + ((getuInputA2() == null) ? 0 : getuInputA2().hashCode());
        result = prime * result + ((getuInputB2() == null) ? 0 : getuInputB2().hashCode());
        result = prime * result + ((getuInputC2() == null) ? 0 : getuInputC2().hashCode());
        result = prime * result + ((getiInputA2() == null) ? 0 : getiInputA2().hashCode());
        result = prime * result + ((getiInputB2() == null) ? 0 : getiInputB2().hashCode());
        result = prime * result + ((getiInputC2() == null) ? 0 : getiInputC2().hashCode());
        result = prime * result + ((getuInputA1() == null) ? 0 : getuInputA1().hashCode());
        result = prime * result + ((getuInputB1() == null) ? 0 : getuInputB1().hashCode());
        result = prime * result + ((getuInputC1() == null) ? 0 : getuInputC1().hashCode());
        result = prime * result + ((getiInputA1() == null) ? 0 : getiInputA1().hashCode());
        result = prime * result + ((getiInputB1() == null) ? 0 : getiInputB1().hashCode());
        result = prime * result + ((getiInputC1() == null) ? 0 : getiInputC1().hashCode());
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
        sb.append(", uOutputA=").append(uOutputA);
        sb.append(", uOutputB=").append(uOutputB);
        sb.append(", uOutputC=").append(uOutputC);
        sb.append(", iOutputA=").append(iOutputA);
        sb.append(", iOutputB=").append(iOutputB);
        sb.append(", iOutputC=").append(iOutputC);
        sb.append(", runningStatus=").append(runningStatus);
        sb.append(", gridFrequency=").append(gridFrequency);
        sb.append(", activePower=").append(activePower);
        sb.append(", reactivePower=").append(reactivePower);
        sb.append(", transferEfficiency=").append(transferEfficiency);
        sb.append(", powerFactor=").append(powerFactor);
        sb.append(", innerTemperature=").append(innerTemperature);
        sb.append(", uInputA2=").append(uInputA2);
        sb.append(", uInputB2=").append(uInputB2);
        sb.append(", uInputC2=").append(uInputC2);
        sb.append(", iInputA2=").append(iInputA2);
        sb.append(", iInputB2=").append(iInputB2);
        sb.append(", iInputC2=").append(iInputC2);
        sb.append(", uInputA1=").append(uInputA1);
        sb.append(", uInputB1=").append(uInputB1);
        sb.append(", uInputC1=").append(uInputC1);
        sb.append(", iInputA1=").append(iInputA1);
        sb.append(", iInputB1=").append(iInputB1);
        sb.append(", iInputC1=").append(iInputC1);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}