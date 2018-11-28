package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysAddress implements Serializable {
    /**
     * 地址唯一性标示
     */
    private Integer id;

    /**
     * 关联的电站id
     */
    private Integer plantId;

    /**
     * 地址名称
     */
    private String areaName;

    /**
     * 经度
     */
    private String locationX;

    /**
     * 纬度
     */
    private String locationY;

    /**
     * 父节点的id
     */
    private Integer fatherId;

    /**
     * 区域路径以“/”为间隔
     */
    private String areaPath;

    /**
     * 级别
     */
    private Integer level;
    
    private String name;
    private String fatherName;
    private Integer count;
    
    /**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
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

    public String getAreaPath() {
        return areaPath;
    }

    public void setAreaPath(String areaPath) {
        this.areaPath = areaPath;
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
        SysAddress other = (SysAddress) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPlantId() == null ? other.getPlantId() == null : this.getPlantId().equals(other.getPlantId()))
            && (this.getAreaName() == null ? other.getAreaName() == null : this.getAreaName().equals(other.getAreaName()))
            && (this.getLocationX() == null ? other.getLocationX() == null : this.getLocationX().equals(other.getLocationX()))
            && (this.getLocationY() == null ? other.getLocationY() == null : this.getLocationY().equals(other.getLocationY()))
            && (this.getFatherId() == null ? other.getFatherId() == null : this.getFatherId().equals(other.getFatherId()))
            && (this.getAreaPath() == null ? other.getAreaPath() == null : this.getAreaPath().equals(other.getAreaPath()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPlantId() == null) ? 0 : getPlantId().hashCode());
        result = prime * result + ((getAreaName() == null) ? 0 : getAreaName().hashCode());
        result = prime * result + ((getLocationX() == null) ? 0 : getLocationX().hashCode());
        result = prime * result + ((getLocationY() == null) ? 0 : getLocationY().hashCode());
        result = prime * result + ((getFatherId() == null) ? 0 : getFatherId().hashCode());
        result = prime * result + ((getAreaPath() == null) ? 0 : getAreaPath().hashCode());
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
        sb.append(", plantId=").append(plantId);
        sb.append(", areaName=").append(areaName);
        sb.append(", locationX=").append(locationX);
        sb.append(", locationY=").append(locationY);
        sb.append(", fatherId=").append(fatherId);
        sb.append(", areaPath=").append(areaPath);
        sb.append(", level=").append(level);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}