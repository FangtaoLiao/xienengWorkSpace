package com.synpowertech.dataCollectionJar.domain;

public class CollModel {
    private Integer id;

    private String modeName;

    private String applyTo;

    private Integer deviceModelId;

    private String tableName;

    private Integer deviceType;

    private String valid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName == null ? null : modeName.trim();
    }

    public String getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(String applyTo) {
        this.applyTo = applyTo == null ? null : applyTo.trim();
    }

    public Integer getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(Integer deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }
}