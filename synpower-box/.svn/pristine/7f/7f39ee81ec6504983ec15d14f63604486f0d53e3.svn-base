package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class InventerStorageData implements Serializable {
    private Integer id;

    /**
     * 日发电量
     */
    private Double dailyEnergy;

    /**
     * 累计发电量
     */
    private Double totalEnergy;
    /**
     * 累计收益
     */
    private Double totalPrice;

    /**
     * 每日收益
     */
    private Double dailyPrice;

    /**
     * 时间戳
     */
    private Long dataTime;

    /**
     * 电站id
     */
    private Integer plantId;

    private static final long serialVersionUID = 1L;

    public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDailyEnergy() {
        return dailyEnergy;
    }

    public void setDailyEnergy(Double dailyEnergy) {
        this.dailyEnergy = dailyEnergy;
    }

    public Double getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(Double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public Double getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(Double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
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
        InventerStorageData other = (InventerStorageData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDailyEnergy() == null ? other.getDailyEnergy() == null : this.getDailyEnergy().equals(other.getDailyEnergy()))
            && (this.getTotalEnergy() == null ? other.getTotalEnergy() == null : this.getTotalEnergy().equals(other.getTotalEnergy()))
            && (this.getDailyPrice() == null ? other.getDailyPrice() == null : this.getDailyPrice().equals(other.getDailyPrice()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getPlantId() == null ? other.getPlantId() == null : this.getPlantId().equals(other.getPlantId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDailyEnergy() == null) ? 0 : getDailyEnergy().hashCode());
        result = prime * result + ((getTotalEnergy() == null) ? 0 : getTotalEnergy().hashCode());
        result = prime * result + ((getDailyPrice() == null) ? 0 : getDailyPrice().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        result = prime * result + ((getPlantId() == null) ? 0 : getPlantId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dailyEnergy=").append(dailyEnergy);
        sb.append(", totalEnergy=").append(totalEnergy);
        sb.append(", dailyPrice=").append(dailyPrice);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", plantId=").append(plantId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}