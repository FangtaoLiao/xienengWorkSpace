package com.synpowertech.dataCollectionJar.domain;

public class DeviceDetailEnvMonitor {
    private Integer id;

    private String modelIdentity;

    private Integer deviceType;

    private String manufacturer;

    private String model;

    private Integer power;

    private String photoPath;

    private String valid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelIdentity() {
        return modelIdentity;
    }

    public void setModelIdentity(String modelIdentity) {
        this.modelIdentity = modelIdentity == null ? null : modelIdentity.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath == null ? null : photoPath.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }
}