package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysPowerPrice implements Serializable {
    private Integer id;

    /**
     * 电价名称
     */
    private String priceName;

    /**
     * 起始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 区分电站类型
     */
    private Integer plantType;

    /**
     * 1删除0未删除
     */
    private String priceValid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getPlantType() {
        return plantType;
    }

    public void setPlantType(Integer plantType) {
        this.plantType = plantType;
    }

    public String getPriceValid() {
        return priceValid;
    }

    public void setPriceValid(String priceValid) {
        this.priceValid = priceValid;
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
        SysPowerPrice other = (SysPowerPrice) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPriceName() == null ? other.getPriceName() == null : this.getPriceName().equals(other.getPriceName()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getPlantType() == null ? other.getPlantType() == null : this.getPlantType().equals(other.getPlantType()))
            && (this.getPriceValid() == null ? other.getPriceValid() == null : this.getPriceValid().equals(other.getPriceValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPriceName() == null) ? 0 : getPriceName().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getPlantType() == null) ? 0 : getPlantType().hashCode());
        result = prime * result + ((getPriceValid() == null) ? 0 : getPriceValid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", priceName=").append(priceName);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", plantType=").append(plantType);
        sb.append(", priceValid=").append(priceValid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}