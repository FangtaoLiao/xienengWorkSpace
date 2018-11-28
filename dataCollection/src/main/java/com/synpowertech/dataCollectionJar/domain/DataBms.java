package com.synpowertech.dataCollectionJar.domain;

public class DataBms {
    private Long id;

    private Integer deviceId;

    private Integer plant;

    private Long dataTime;

    private Float voltBus;

    private Float currBus;

    private Long lifeTime;

    private Long leftLifeTime;

    private Float bmsCapacity;

    private Float bmsChargedCapacity;

    private Float bmsLeftCapacity;

    private Float socCellCluster;

    private Integer numCellvoltmax;

    private Float voltCellmax;

    private Integer numCellvoltmin;

    private Float voltCellmin;

    private Integer numTempmax;

    private Float tempCellmax;

    private Integer numTempmin;

    private Float tempCellmin;

    private Float cellTotal;

    private Byte runningStatus;

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
}