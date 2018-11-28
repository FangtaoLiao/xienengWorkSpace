package com.synpowertech.dataCollectionJar.domain;

public class Device {
    private Integer id;

    private String deviceName;

    private String deviceSn;

    private Integer deviceType;

    private Integer deviceModelId;

    private String connAddr;

    private Integer connPort;

    private Integer plantId;

    private Long connTime;

    private Integer connProtocol;

    private Integer dataMode;

    private String manufacture;

    private Float power;

    private Integer supId;

    private Integer maxNum;

    private String deviceValid;

    private String deviceStatus;

    private String joinPlant;

    private String joinDevice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn == null ? null : deviceSn.trim();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceModelId(Integer deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public Integer getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getConnAddr() {
        return connAddr;
    }

    public void setConnAddr(String connAddr) {
        this.connAddr = connAddr == null ? null : connAddr.trim();
    }

    public Integer getConnPort() {
        return connPort;
    }

    public void setConnPort(Integer connPort) {
        this.connPort = connPort;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Long getConnTime() {
        return connTime;
    }

    public void setConnTime(Long connTime) {
        this.connTime = connTime;
    }

    public Integer getConnProtocol() {
        return connProtocol;
    }

    public void setConnProtocol(Integer connProtocol) {
        this.connProtocol = connProtocol;
    }

    public Integer getDataMode() {
        return dataMode;
    }

    public void setDataMode(Integer dataMode) {
        this.dataMode = dataMode;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture == null ? null : manufacture.trim();
    }

    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }

    public Integer getSupId() {
        return supId;
    }

    public void setSupId(Integer supId) {
        this.supId = supId;
    }

    public String getDeviceValid() {
        return deviceValid;
    }

    public void setDeviceValid(String deviceValid) {
        this.deviceValid = deviceValid == null ? null : deviceValid.trim();
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus == null ? null : deviceStatus.trim();
    }

    public String getJoinPlant() {
        return joinPlant;
    }

    public void setJoinPlant(String joinPlant) {
        this.joinPlant = joinPlant;
    }

    public String getJoinDevice() {
        return joinDevice;
    }

    public void setJoinDevice(String joinDevice) {
        this.joinDevice = joinDevice;
    }

    public Device(String deviceName, String deviceSn, Integer deviceType, Integer deviceModelId, String connAddr, Integer connPort,
                  Integer plantId, Long connTime, Integer connProtocol, Integer dataMode, String manufacture, Float power,
                  Integer supId, Integer maxNum, String deviceValid, String deviceStatus) {
        super();
        this.deviceName = deviceName;
        this.deviceSn = deviceSn;
        this.deviceType = deviceType;
        this.deviceModelId = deviceModelId;
        this.connAddr = connAddr;
        this.connPort = connPort;
        this.plantId = plantId;
        this.connTime = connTime;
        this.connProtocol = connProtocol;
        this.dataMode = dataMode;
        this.manufacture = manufacture;
        this.power = power;
        this.supId = supId;
        this.maxNum = maxNum;
        this.deviceValid = deviceValid;
        this.deviceStatus = deviceStatus;
    }

    public Device() {
    }


}