package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DataBms implements Serializable {
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
     * 组端电压
     */
    private Float voltBus;

    /**
     * 组端电流
     */
    private Float currBus;

    /**
     * 总续航时间
     */
    private Long lifeTime;

    /**
     * 剩余续航时间
     */
    private Long leftLifeTime;

    /**
     * 容量
     */
    private Float bmsCapacity;

    /**
     * 可充电量
     */
    private Float bmsChargedCapacity;

    /**
     * 可放电量
     */
    private Float bmsLeftCapacity;

    /**
     * 电池组系统SOC值
     */
    private Float socCellCluster;

    /**
     * 最大电压单体序号
     */
    private Integer numCellvoltmax;

    /**
     * 单体最大电压
     */
    private Float voltCellmax;

    /**
     * 最小电压单体序号
     */
    private Integer numCellvoltmin;

    /**
     * 单体最小电压
     */
    private Float voltCellmin;

    /**
     * 温度最高单体序号
     */
    private Integer numTempmax;

    /**
     * 单体最高温度
     */
    private Float tempCellmax;

    /**
     * 温度最低单体序号
     */
    private Integer numTempmin;

    /**
     * 单体最低温度
     */
    private Float tempCellmin;

    /**
     * 单体个数
     */
    private Float cellTotal;

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

    public Float getVoltBus() {
        return voltBus;
    }

    public void setVoltBus(Float voltBus) {
        this.voltBus = voltBus;
    }

    public Float getCurrBus() {
        return currBus;
    }

    public void setCurrBus(Float currBus) {
        this.currBus = currBus;
    }

    public Long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(Long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Long getLeftLifeTime() {
        return leftLifeTime;
    }

    public void setLeftLifeTime(Long leftLifeTime) {
        this.leftLifeTime = leftLifeTime;
    }

    public Float getBmsCapacity() {
        return bmsCapacity;
    }

    public void setBmsCapacity(Float bmsCapacity) {
        this.bmsCapacity = bmsCapacity;
    }

    public Float getBmsChargedCapacity() {
        return bmsChargedCapacity;
    }

    public void setBmsChargedCapacity(Float bmsChargedCapacity) {
        this.bmsChargedCapacity = bmsChargedCapacity;
    }

    public Float getBmsLeftCapacity() {
        return bmsLeftCapacity;
    }

    public void setBmsLeftCapacity(Float bmsLeftCapacity) {
        this.bmsLeftCapacity = bmsLeftCapacity;
    }

    public Float getSocCellCluster() {
        return socCellCluster;
    }

    public void setSocCellCluster(Float socCellCluster) {
        this.socCellCluster = socCellCluster;
    }

    public Integer getNumCellvoltmax() {
        return numCellvoltmax;
    }

    public void setNumCellvoltmax(Integer numCellvoltmax) {
        this.numCellvoltmax = numCellvoltmax;
    }

    public Float getVoltCellmax() {
        return voltCellmax;
    }

    public void setVoltCellmax(Float voltCellmax) {
        this.voltCellmax = voltCellmax;
    }

    public Integer getNumCellvoltmin() {
        return numCellvoltmin;
    }

    public void setNumCellvoltmin(Integer numCellvoltmin) {
        this.numCellvoltmin = numCellvoltmin;
    }

    public Float getVoltCellmin() {
        return voltCellmin;
    }

    public void setVoltCellmin(Float voltCellmin) {
        this.voltCellmin = voltCellmin;
    }

    public Integer getNumTempmax() {
        return numTempmax;
    }

    public void setNumTempmax(Integer numTempmax) {
        this.numTempmax = numTempmax;
    }

    public Float getTempCellmax() {
        return tempCellmax;
    }

    public void setTempCellmax(Float tempCellmax) {
        this.tempCellmax = tempCellmax;
    }

    public Integer getNumTempmin() {
        return numTempmin;
    }

    public void setNumTempmin(Integer numTempmin) {
        this.numTempmin = numTempmin;
    }

    public Float getTempCellmin() {
        return tempCellmin;
    }

    public void setTempCellmin(Float tempCellmin) {
        this.tempCellmin = tempCellmin;
    }

    public Float getCellTotal() {
        return cellTotal;
    }

    public void setCellTotal(Float cellTotal) {
        this.cellTotal = cellTotal;
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
        DataBms other = (DataBms) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getPlant() == null ? other.getPlant() == null : this.getPlant().equals(other.getPlant()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getVoltBus() == null ? other.getVoltBus() == null : this.getVoltBus().equals(other.getVoltBus()))
            && (this.getCurrBus() == null ? other.getCurrBus() == null : this.getCurrBus().equals(other.getCurrBus()))
            && (this.getLifeTime() == null ? other.getLifeTime() == null : this.getLifeTime().equals(other.getLifeTime()))
            && (this.getLeftLifeTime() == null ? other.getLeftLifeTime() == null : this.getLeftLifeTime().equals(other.getLeftLifeTime()))
            && (this.getBmsCapacity() == null ? other.getBmsCapacity() == null : this.getBmsCapacity().equals(other.getBmsCapacity()))
            && (this.getBmsChargedCapacity() == null ? other.getBmsChargedCapacity() == null : this.getBmsChargedCapacity().equals(other.getBmsChargedCapacity()))
            && (this.getBmsLeftCapacity() == null ? other.getBmsLeftCapacity() == null : this.getBmsLeftCapacity().equals(other.getBmsLeftCapacity()))
            && (this.getSocCellCluster() == null ? other.getSocCellCluster() == null : this.getSocCellCluster().equals(other.getSocCellCluster()))
            && (this.getNumCellvoltmax() == null ? other.getNumCellvoltmax() == null : this.getNumCellvoltmax().equals(other.getNumCellvoltmax()))
            && (this.getVoltCellmax() == null ? other.getVoltCellmax() == null : this.getVoltCellmax().equals(other.getVoltCellmax()))
            && (this.getNumCellvoltmin() == null ? other.getNumCellvoltmin() == null : this.getNumCellvoltmin().equals(other.getNumCellvoltmin()))
            && (this.getVoltCellmin() == null ? other.getVoltCellmin() == null : this.getVoltCellmin().equals(other.getVoltCellmin()))
            && (this.getNumTempmax() == null ? other.getNumTempmax() == null : this.getNumTempmax().equals(other.getNumTempmax()))
            && (this.getTempCellmax() == null ? other.getTempCellmax() == null : this.getTempCellmax().equals(other.getTempCellmax()))
            && (this.getNumTempmin() == null ? other.getNumTempmin() == null : this.getNumTempmin().equals(other.getNumTempmin()))
            && (this.getTempCellmin() == null ? other.getTempCellmin() == null : this.getTempCellmin().equals(other.getTempCellmin()))
            && (this.getCellTotal() == null ? other.getCellTotal() == null : this.getCellTotal().equals(other.getCellTotal()))
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
        result = prime * result + ((getVoltBus() == null) ? 0 : getVoltBus().hashCode());
        result = prime * result + ((getCurrBus() == null) ? 0 : getCurrBus().hashCode());
        result = prime * result + ((getLifeTime() == null) ? 0 : getLifeTime().hashCode());
        result = prime * result + ((getLeftLifeTime() == null) ? 0 : getLeftLifeTime().hashCode());
        result = prime * result + ((getBmsCapacity() == null) ? 0 : getBmsCapacity().hashCode());
        result = prime * result + ((getBmsChargedCapacity() == null) ? 0 : getBmsChargedCapacity().hashCode());
        result = prime * result + ((getBmsLeftCapacity() == null) ? 0 : getBmsLeftCapacity().hashCode());
        result = prime * result + ((getSocCellCluster() == null) ? 0 : getSocCellCluster().hashCode());
        result = prime * result + ((getNumCellvoltmax() == null) ? 0 : getNumCellvoltmax().hashCode());
        result = prime * result + ((getVoltCellmax() == null) ? 0 : getVoltCellmax().hashCode());
        result = prime * result + ((getNumCellvoltmin() == null) ? 0 : getNumCellvoltmin().hashCode());
        result = prime * result + ((getVoltCellmin() == null) ? 0 : getVoltCellmin().hashCode());
        result = prime * result + ((getNumTempmax() == null) ? 0 : getNumTempmax().hashCode());
        result = prime * result + ((getTempCellmax() == null) ? 0 : getTempCellmax().hashCode());
        result = prime * result + ((getNumTempmin() == null) ? 0 : getNumTempmin().hashCode());
        result = prime * result + ((getTempCellmin() == null) ? 0 : getTempCellmin().hashCode());
        result = prime * result + ((getCellTotal() == null) ? 0 : getCellTotal().hashCode());
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
        sb.append(", voltBus=").append(voltBus);
        sb.append(", currBus=").append(currBus);
        sb.append(", lifeTime=").append(lifeTime);
        sb.append(", leftLifeTime=").append(leftLifeTime);
        sb.append(", bmsCapacity=").append(bmsCapacity);
        sb.append(", bmsChargedCapacity=").append(bmsChargedCapacity);
        sb.append(", bmsLeftCapacity=").append(bmsLeftCapacity);
        sb.append(", socCellCluster=").append(socCellCluster);
        sb.append(", numCellvoltmax=").append(numCellvoltmax);
        sb.append(", voltCellmax=").append(voltCellmax);
        sb.append(", numCellvoltmin=").append(numCellvoltmin);
        sb.append(", voltCellmin=").append(voltCellmin);
        sb.append(", numTempmax=").append(numTempmax);
        sb.append(", tempCellmax=").append(tempCellmax);
        sb.append(", numTempmin=").append(numTempmin);
        sb.append(", tempCellmin=").append(tempCellmin);
        sb.append(", cellTotal=").append(cellTotal);
        sb.append(", runningStatus=").append(runningStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}