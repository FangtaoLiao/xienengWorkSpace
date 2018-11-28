package com.synpowertech.dataCollectionJar.domain;

public class DataBoxChange {
    private Integer id;

    private Integer deviceId;

    private Integer plant;

    private Long dataTime;

    private Byte runningStatus;

    private Float innerTemperature;

    private Float uAcA;

    private Float uAcB;

    private Float uAcC;

    private Float uLineAb;

    private Float uLineBc;

    private Float uLineCa;

    private Float iAcA;

    private Float iAcB;

    private Float iAcC;

    private Float activePower;

    private Float reactivePower;

    private Float powerFactor;

    private Float frequency;

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
}