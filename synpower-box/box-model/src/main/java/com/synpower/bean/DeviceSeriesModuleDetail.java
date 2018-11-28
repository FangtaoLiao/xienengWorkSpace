package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DeviceSeriesModuleDetail implements Serializable {
    /**
     * key
     */
    private Integer id;

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 组件id
     */
    private Integer moduleId;

    /**
     * 组件个数
     */
    private Integer moduleNum;

    /**
     * 组串名字(用户后端反差)
     */
    private String seriesName;

    /**
     * 组串别名(页面显示)
     */
    private String seriesAlias;

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

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getModuleNum() {
        return moduleNum;
    }

    public void setModuleNum(Integer moduleNum) {
        this.moduleNum = moduleNum;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesAlias() {
        return seriesAlias;
    }

    public void setSeriesAlias(String seriesAlias) {
        this.seriesAlias = seriesAlias;
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
        DeviceSeriesModuleDetail other = (DeviceSeriesModuleDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getModuleId() == null ? other.getModuleId() == null : this.getModuleId().equals(other.getModuleId()))
            && (this.getModuleNum() == null ? other.getModuleNum() == null : this.getModuleNum().equals(other.getModuleNum()))
            && (this.getSeriesName() == null ? other.getSeriesName() == null : this.getSeriesName().equals(other.getSeriesName()))
            && (this.getSeriesAlias() == null ? other.getSeriesAlias() == null : this.getSeriesAlias().equals(other.getSeriesAlias()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getModuleId() == null) ? 0 : getModuleId().hashCode());
        result = prime * result + ((getModuleNum() == null) ? 0 : getModuleNum().hashCode());
        result = prime * result + ((getSeriesName() == null) ? 0 : getSeriesName().hashCode());
        result = prime * result + ((getSeriesAlias() == null) ? 0 : getSeriesAlias().hashCode());
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
        sb.append(", deviceId=").append(deviceId);
        sb.append(", moduleId=").append(moduleId);
        sb.append(", moduleNum=").append(moduleNum);
        sb.append(", seriesName=").append(seriesName);
        sb.append(", seriesAlias=").append(seriesAlias);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}