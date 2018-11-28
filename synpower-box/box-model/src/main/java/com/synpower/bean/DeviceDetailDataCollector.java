package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DeviceDetailDataCollector implements Serializable {
    /**
     * key
     */
    private Integer id;

    /**
     * 型号标识
     */
    private String modelIdentity;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 该型号设备图片，多张可用';'等分隔符
     */
    private String photoPath;

    /**
     * 是否可用，0表示正常，1表示停用
     */
    private String valid;

    private static final long serialVersionUID = 1L;

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
        this.modelIdentity = modelIdentity;
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
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
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
        DeviceDetailDataCollector other = (DeviceDetailDataCollector) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getModelIdentity() == null ? other.getModelIdentity() == null : this.getModelIdentity().equals(other.getModelIdentity()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()))
            && (this.getManufacturer() == null ? other.getManufacturer() == null : this.getManufacturer().equals(other.getManufacturer()))
            && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
            && (this.getProtocol() == null ? other.getProtocol() == null : this.getProtocol().equals(other.getProtocol()))
            && (this.getPhotoPath() == null ? other.getPhotoPath() == null : this.getPhotoPath().equals(other.getPhotoPath()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModelIdentity() == null) ? 0 : getModelIdentity().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        result = prime * result + ((getManufacturer() == null) ? 0 : getManufacturer().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getProtocol() == null) ? 0 : getProtocol().hashCode());
        result = prime * result + ((getPhotoPath() == null) ? 0 : getPhotoPath().hashCode());
        result = prime * result + ((getValid() == null) ? 0 : getValid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", modelIdentity=").append(modelIdentity);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append(", model=").append(model);
        sb.append(", protocol=").append(protocol);
        sb.append(", photoPath=").append(photoPath);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}