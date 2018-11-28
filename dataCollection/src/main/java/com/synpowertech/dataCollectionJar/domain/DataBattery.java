package com.synpowertech.dataCollectionJar.domain;

public class DataBattery {
    private Long id;

    private Integer deviceId;

    private Integer plant;

    private Long dataTime;

    private Float uCell;

    private Float iCell;

    private Float innerResistance;

    private Float tempCell;

    private Float leftCapacity;

    private Float leftCapacityPercent;

    private Float socCell;

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
}