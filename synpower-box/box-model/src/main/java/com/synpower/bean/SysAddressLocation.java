package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysAddressLocation implements Serializable {
    /**
     * 地址唯一性标示
     */
    private Integer id;

    /**
     * 地址名称
     */
    private String areaName;

    /**
     * 纬度
     */
    private String locationX;

    /**
     * 经度
     */
    private String locationY;

    /**
     * 父节点的id
     */
    private Integer fatherId;

    /**
     * 级别
     */
    private Integer level;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
        SysAddressLocation other = (SysAddressLocation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAreaName() == null ? other.getAreaName() == null : this.getAreaName().equals(other.getAreaName()))
            && (this.getLocationX() == null ? other.getLocationX() == null : this.getLocationX().equals(other.getLocationX()))
            && (this.getLocationY() == null ? other.getLocationY() == null : this.getLocationY().equals(other.getLocationY()))
            && (this.getFatherId() == null ? other.getFatherId() == null : this.getFatherId().equals(other.getFatherId()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAreaName() == null) ? 0 : getAreaName().hashCode());
        result = prime * result + ((getLocationX() == null) ? 0 : getLocationX().hashCode());
        result = prime * result + ((getLocationY() == null) ? 0 : getLocationY().hashCode());
        result = prime * result + ((getFatherId() == null) ? 0 : getFatherId().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", areaName=").append(areaName);
        sb.append(", locationX=").append(locationX);
        sb.append(", locationY=").append(locationY);
        sb.append(", fatherId=").append(fatherId);
        sb.append(", level=").append(level);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}