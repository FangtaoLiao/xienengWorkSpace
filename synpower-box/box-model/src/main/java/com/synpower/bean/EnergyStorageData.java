package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class EnergyStorageData implements Serializable {
    private Integer id;

    private Double emPosJianEnergy;

    private Double emPosJianPrice;

    private Double emNegJianEnergy;

    private Double emNegJianPrice;

    private Double emPosFengEnergy;

    private Double emPosFengPrice;

    private Double emNegFengEnergy;

    private Double emNegFengPrice;

    private Double emPosPingEnergy;

    private Double emPosPingPrice;

    private Double emNegPingEnergy;

    private Double emNegPingPrice;

    private Double emPosGuEnergy;

    private Double emPosGuPrice;

    private Double emNegGuEnergy;

    private Double emNegGuPrice;

    /**
     * 当日放电总电量
     */
    private Double dailyPosEnergy;

    /**
     * 当日放电收益
     */
    private Double dailyPosPrice;

    /**
     * 当日充电总电量
     */
    private Double dailyNegEnergy;

    /**
     * 当日充电总电量
     */
    private Double dailyNegPrice;

    /**
     * 每日净上网电量
     */
    private Double dailyEnergy;

    /**
     * 每日净收益
     */
    private Double dailyPrice;

    /**
     * 总上网电量
     */
    private Double totalPosEnergy;

    /**
     * 总上网收益
     */
    private Double totalPosPrice;

    /**
     * 总下网电量
     */
    private Double totalNegEnergy;

    /**
     * 总下网收益
     */
    private Double totalNegPrice;

    /**
     * 净上网电量
     */
    private Double totalEnergy;

    /**
     * 总收益
     */
    private Double totalPrice;

    /**
     * 时间戳
     */
    private Long dataTime;

    /**
     * 电站id
     */
    private Integer plantId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getEmPosJianEnergy() {
        return emPosJianEnergy;
    }

    public void setEmPosJianEnergy(Double emPosJianEnergy) {
        this.emPosJianEnergy = emPosJianEnergy;
    }

    public Double getEmPosJianPrice() {
        return emPosJianPrice;
    }

    public void setEmPosJianPrice(Double emPosJianPrice) {
        this.emPosJianPrice = emPosJianPrice;
    }

    public Double getEmNegJianEnergy() {
        return emNegJianEnergy;
    }

    public void setEmNegJianEnergy(Double emNegJianEnergy) {
        this.emNegJianEnergy = emNegJianEnergy;
    }

    public Double getEmNegJianPrice() {
        return emNegJianPrice;
    }

    public void setEmNegJianPrice(Double emNegJianPrice) {
        this.emNegJianPrice = emNegJianPrice;
    }

    public Double getEmPosFengEnergy() {
        return emPosFengEnergy;
    }

    public void setEmPosFengEnergy(Double emPosFengEnergy) {
        this.emPosFengEnergy = emPosFengEnergy;
    }

    public Double getEmPosFengPrice() {
        return emPosFengPrice;
    }

    public void setEmPosFengPrice(Double emPosFengPrice) {
        this.emPosFengPrice = emPosFengPrice;
    }

    public Double getEmNegFengEnergy() {
        return emNegFengEnergy;
    }

    public void setEmNegFengEnergy(Double emNegFengEnergy) {
        this.emNegFengEnergy = emNegFengEnergy;
    }

    public Double getEmNegFengPrice() {
        return emNegFengPrice;
    }

    public void setEmNegFengPrice(Double emNegFengPrice) {
        this.emNegFengPrice = emNegFengPrice;
    }

    public Double getEmPosPingEnergy() {
        return emPosPingEnergy;
    }

    public void setEmPosPingEnergy(Double emPosPingEnergy) {
        this.emPosPingEnergy = emPosPingEnergy;
    }

    public Double getEmPosPingPrice() {
        return emPosPingPrice;
    }

    public void setEmPosPingPrice(Double emPosPingPrice) {
        this.emPosPingPrice = emPosPingPrice;
    }

    public Double getEmNegPingEnergy() {
        return emNegPingEnergy;
    }

    public void setEmNegPingEnergy(Double emNegPingEnergy) {
        this.emNegPingEnergy = emNegPingEnergy;
    }

    public Double getEmNegPingPrice() {
        return emNegPingPrice;
    }

    public void setEmNegPingPrice(Double emNegPingPrice) {
        this.emNegPingPrice = emNegPingPrice;
    }

    public Double getEmPosGuEnergy() {
        return emPosGuEnergy;
    }

    public void setEmPosGuEnergy(Double emPosGuEnergy) {
        this.emPosGuEnergy = emPosGuEnergy;
    }

    public Double getEmPosGuPrice() {
        return emPosGuPrice;
    }

    public void setEmPosGuPrice(Double emPosGuPrice) {
        this.emPosGuPrice = emPosGuPrice;
    }

    public Double getEmNegGuEnergy() {
        return emNegGuEnergy;
    }

    public void setEmNegGuEnergy(Double emNegGuEnergy) {
        this.emNegGuEnergy = emNegGuEnergy;
    }

    public Double getEmNegGuPrice() {
        return emNegGuPrice;
    }

    public void setEmNegGuPrice(Double emNegGuPrice) {
        this.emNegGuPrice = emNegGuPrice;
    }

    public Double getDailyPosEnergy() {
        return dailyPosEnergy;
    }

    public void setDailyPosEnergy(Double dailyPosEnergy) {
        this.dailyPosEnergy = dailyPosEnergy;
    }

    public Double getDailyPosPrice() {
        return dailyPosPrice;
    }

    public void setDailyPosPrice(Double dailyPosPrice) {
        this.dailyPosPrice = dailyPosPrice;
    }

    public Double getDailyNegEnergy() {
        return dailyNegEnergy;
    }

    public void setDailyNegEnergy(Double dailyNegEnergy) {
        this.dailyNegEnergy = dailyNegEnergy;
    }

    public Double getDailyNegPrice() {
        return dailyNegPrice;
    }

    public void setDailyNegPrice(Double dailyNegPrice) {
        this.dailyNegPrice = dailyNegPrice;
    }

    public Double getDailyEnergy() {
        return dailyEnergy;
    }

    public void setDailyEnergy(Double dailyEnergy) {
        this.dailyEnergy = dailyEnergy;
    }

    public Double getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(Double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Double getTotalPosEnergy() {
        return totalPosEnergy;
    }

    public void setTotalPosEnergy(Double totalPosEnergy) {
        this.totalPosEnergy = totalPosEnergy;
    }

    public Double getTotalPosPrice() {
        return totalPosPrice;
    }

    public void setTotalPosPrice(Double totalPosPrice) {
        this.totalPosPrice = totalPosPrice;
    }

    public Double getTotalNegEnergy() {
        return totalNegEnergy;
    }

    public void setTotalNegEnergy(Double totalNegEnergy) {
        this.totalNegEnergy = totalNegEnergy;
    }

    public Double getTotalNegPrice() {
        return totalNegPrice;
    }

    public void setTotalNegPrice(Double totalNegPrice) {
        this.totalNegPrice = totalNegPrice;
    }

    public Double getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(Double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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
    
    public EnergyStorageData() {
		super();
	}

	public EnergyStorageData(Double emPosJianEnergy, Double emPosJianPrice, Double emNegJianEnergy,
			Double emNegJianPrice, Double emPosFengEnergy, Double emPosFengPrice, Double emNegFengEnergy,
			Double emNegFengPrice, Double emPosPingEnergy, Double emPosPingPrice, Double emNegPingEnergy,
			Double emNegPingPrice, Double emPosGuEnergy, Double emPosGuPrice, Double emNegGuEnergy, Double emNegGuPrice,
			Double dailyPosEnergy, Double dailyPosPrice, Double dailyNegEnergy, Double dailyNegPrice,
			Double dailyEnergy, Double dailyPrice, Double totalPosEnergy, Double totalPosPrice, Double totalNegEnergy,
			Double totalNegPrice, Double totalEnergy, Double totalPrice, Long dataTime, Integer plantId) {
		super();
		this.emPosJianEnergy = emPosJianEnergy;
		this.emPosJianPrice = emPosJianPrice;
		this.emNegJianEnergy = emNegJianEnergy;
		this.emNegJianPrice = emNegJianPrice;
		this.emPosFengEnergy = emPosFengEnergy;
		this.emPosFengPrice = emPosFengPrice;
		this.emNegFengEnergy = emNegFengEnergy;
		this.emNegFengPrice = emNegFengPrice;
		this.emPosPingEnergy = emPosPingEnergy;
		this.emPosPingPrice = emPosPingPrice;
		this.emNegPingEnergy = emNegPingEnergy;
		this.emNegPingPrice = emNegPingPrice;
		this.emPosGuEnergy = emPosGuEnergy;
		this.emPosGuPrice = emPosGuPrice;
		this.emNegGuEnergy = emNegGuEnergy;
		this.emNegGuPrice = emNegGuPrice;
		this.dailyPosEnergy = dailyPosEnergy;
		this.dailyPosPrice = dailyPosPrice;
		this.dailyNegEnergy = dailyNegEnergy;
		this.dailyNegPrice = dailyNegPrice;
		this.dailyEnergy = dailyEnergy;
		this.dailyPrice = dailyPrice;
		this.totalPosEnergy = totalPosEnergy;
		this.totalPosPrice = totalPosPrice;
		this.totalNegEnergy = totalNegEnergy;
		this.totalNegPrice = totalNegPrice;
		this.totalEnergy = totalEnergy;
		this.totalPrice = totalPrice;
		this.dataTime = dataTime;
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
        EnergyStorageData other = (EnergyStorageData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getEmPosJianEnergy() == null ? other.getEmPosJianEnergy() == null : this.getEmPosJianEnergy().equals(other.getEmPosJianEnergy()))
            && (this.getEmPosJianPrice() == null ? other.getEmPosJianPrice() == null : this.getEmPosJianPrice().equals(other.getEmPosJianPrice()))
            && (this.getEmNegJianEnergy() == null ? other.getEmNegJianEnergy() == null : this.getEmNegJianEnergy().equals(other.getEmNegJianEnergy()))
            && (this.getEmNegJianPrice() == null ? other.getEmNegJianPrice() == null : this.getEmNegJianPrice().equals(other.getEmNegJianPrice()))
            && (this.getEmPosFengEnergy() == null ? other.getEmPosFengEnergy() == null : this.getEmPosFengEnergy().equals(other.getEmPosFengEnergy()))
            && (this.getEmPosFengPrice() == null ? other.getEmPosFengPrice() == null : this.getEmPosFengPrice().equals(other.getEmPosFengPrice()))
            && (this.getEmNegFengEnergy() == null ? other.getEmNegFengEnergy() == null : this.getEmNegFengEnergy().equals(other.getEmNegFengEnergy()))
            && (this.getEmNegFengPrice() == null ? other.getEmNegFengPrice() == null : this.getEmNegFengPrice().equals(other.getEmNegFengPrice()))
            && (this.getEmPosPingEnergy() == null ? other.getEmPosPingEnergy() == null : this.getEmPosPingEnergy().equals(other.getEmPosPingEnergy()))
            && (this.getEmPosPingPrice() == null ? other.getEmPosPingPrice() == null : this.getEmPosPingPrice().equals(other.getEmPosPingPrice()))
            && (this.getEmNegPingEnergy() == null ? other.getEmNegPingEnergy() == null : this.getEmNegPingEnergy().equals(other.getEmNegPingEnergy()))
            && (this.getEmNegPingPrice() == null ? other.getEmNegPingPrice() == null : this.getEmNegPingPrice().equals(other.getEmNegPingPrice()))
            && (this.getEmPosGuEnergy() == null ? other.getEmPosGuEnergy() == null : this.getEmPosGuEnergy().equals(other.getEmPosGuEnergy()))
            && (this.getEmPosGuPrice() == null ? other.getEmPosGuPrice() == null : this.getEmPosGuPrice().equals(other.getEmPosGuPrice()))
            && (this.getEmNegGuEnergy() == null ? other.getEmNegGuEnergy() == null : this.getEmNegGuEnergy().equals(other.getEmNegGuEnergy()))
            && (this.getEmNegGuPrice() == null ? other.getEmNegGuPrice() == null : this.getEmNegGuPrice().equals(other.getEmNegGuPrice()))
            && (this.getDailyPosEnergy() == null ? other.getDailyPosEnergy() == null : this.getDailyPosEnergy().equals(other.getDailyPosEnergy()))
            && (this.getDailyPosPrice() == null ? other.getDailyPosPrice() == null : this.getDailyPosPrice().equals(other.getDailyPosPrice()))
            && (this.getDailyNegEnergy() == null ? other.getDailyNegEnergy() == null : this.getDailyNegEnergy().equals(other.getDailyNegEnergy()))
            && (this.getDailyNegPrice() == null ? other.getDailyNegPrice() == null : this.getDailyNegPrice().equals(other.getDailyNegPrice()))
            && (this.getDailyEnergy() == null ? other.getDailyEnergy() == null : this.getDailyEnergy().equals(other.getDailyEnergy()))
            && (this.getDailyPrice() == null ? other.getDailyPrice() == null : this.getDailyPrice().equals(other.getDailyPrice()))
            && (this.getTotalPosEnergy() == null ? other.getTotalPosEnergy() == null : this.getTotalPosEnergy().equals(other.getTotalPosEnergy()))
            && (this.getTotalPosPrice() == null ? other.getTotalPosPrice() == null : this.getTotalPosPrice().equals(other.getTotalPosPrice()))
            && (this.getTotalNegEnergy() == null ? other.getTotalNegEnergy() == null : this.getTotalNegEnergy().equals(other.getTotalNegEnergy()))
            && (this.getTotalNegPrice() == null ? other.getTotalNegPrice() == null : this.getTotalNegPrice().equals(other.getTotalNegPrice()))
            && (this.getTotalEnergy() == null ? other.getTotalEnergy() == null : this.getTotalEnergy().equals(other.getTotalEnergy()))
            && (this.getTotalPrice() == null ? other.getTotalPrice() == null : this.getTotalPrice().equals(other.getTotalPrice()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getPlantId() == null ? other.getPlantId() == null : this.getPlantId().equals(other.getPlantId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getEmPosJianEnergy() == null) ? 0 : getEmPosJianEnergy().hashCode());
        result = prime * result + ((getEmPosJianPrice() == null) ? 0 : getEmPosJianPrice().hashCode());
        result = prime * result + ((getEmNegJianEnergy() == null) ? 0 : getEmNegJianEnergy().hashCode());
        result = prime * result + ((getEmNegJianPrice() == null) ? 0 : getEmNegJianPrice().hashCode());
        result = prime * result + ((getEmPosFengEnergy() == null) ? 0 : getEmPosFengEnergy().hashCode());
        result = prime * result + ((getEmPosFengPrice() == null) ? 0 : getEmPosFengPrice().hashCode());
        result = prime * result + ((getEmNegFengEnergy() == null) ? 0 : getEmNegFengEnergy().hashCode());
        result = prime * result + ((getEmNegFengPrice() == null) ? 0 : getEmNegFengPrice().hashCode());
        result = prime * result + ((getEmPosPingEnergy() == null) ? 0 : getEmPosPingEnergy().hashCode());
        result = prime * result + ((getEmPosPingPrice() == null) ? 0 : getEmPosPingPrice().hashCode());
        result = prime * result + ((getEmNegPingEnergy() == null) ? 0 : getEmNegPingEnergy().hashCode());
        result = prime * result + ((getEmNegPingPrice() == null) ? 0 : getEmNegPingPrice().hashCode());
        result = prime * result + ((getEmPosGuEnergy() == null) ? 0 : getEmPosGuEnergy().hashCode());
        result = prime * result + ((getEmPosGuPrice() == null) ? 0 : getEmPosGuPrice().hashCode());
        result = prime * result + ((getEmNegGuEnergy() == null) ? 0 : getEmNegGuEnergy().hashCode());
        result = prime * result + ((getEmNegGuPrice() == null) ? 0 : getEmNegGuPrice().hashCode());
        result = prime * result + ((getDailyPosEnergy() == null) ? 0 : getDailyPosEnergy().hashCode());
        result = prime * result + ((getDailyPosPrice() == null) ? 0 : getDailyPosPrice().hashCode());
        result = prime * result + ((getDailyNegEnergy() == null) ? 0 : getDailyNegEnergy().hashCode());
        result = prime * result + ((getDailyNegPrice() == null) ? 0 : getDailyNegPrice().hashCode());
        result = prime * result + ((getDailyEnergy() == null) ? 0 : getDailyEnergy().hashCode());
        result = prime * result + ((getDailyPrice() == null) ? 0 : getDailyPrice().hashCode());
        result = prime * result + ((getTotalPosEnergy() == null) ? 0 : getTotalPosEnergy().hashCode());
        result = prime * result + ((getTotalPosPrice() == null) ? 0 : getTotalPosPrice().hashCode());
        result = prime * result + ((getTotalNegEnergy() == null) ? 0 : getTotalNegEnergy().hashCode());
        result = prime * result + ((getTotalNegPrice() == null) ? 0 : getTotalNegPrice().hashCode());
        result = prime * result + ((getTotalEnergy() == null) ? 0 : getTotalEnergy().hashCode());
        result = prime * result + ((getTotalPrice() == null) ? 0 : getTotalPrice().hashCode());
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
        sb.append(", emPosJianEnergy=").append(emPosJianEnergy);
        sb.append(", emPosJianPrice=").append(emPosJianPrice);
        sb.append(", emNegJianEnergy=").append(emNegJianEnergy);
        sb.append(", emNegJianPrice=").append(emNegJianPrice);
        sb.append(", emPosFengEnergy=").append(emPosFengEnergy);
        sb.append(", emPosFengPrice=").append(emPosFengPrice);
        sb.append(", emNegFengEnergy=").append(emNegFengEnergy);
        sb.append(", emNegFengPrice=").append(emNegFengPrice);
        sb.append(", emPosPingEnergy=").append(emPosPingEnergy);
        sb.append(", emPosPingPrice=").append(emPosPingPrice);
        sb.append(", emNegPingEnergy=").append(emNegPingEnergy);
        sb.append(", emNegPingPrice=").append(emNegPingPrice);
        sb.append(", emPosGuEnergy=").append(emPosGuEnergy);
        sb.append(", emPosGuPrice=").append(emPosGuPrice);
        sb.append(", emNegGuEnergy=").append(emNegGuEnergy);
        sb.append(", emNegGuPrice=").append(emNegGuPrice);
        sb.append(", dailyPosEnergy=").append(dailyPosEnergy);
        sb.append(", dailyPosPrice=").append(dailyPosPrice);
        sb.append(", dailyNegEnergy=").append(dailyNegEnergy);
        sb.append(", dailyNegPrice=").append(dailyNegPrice);
        sb.append(", dailyEnergy=").append(dailyEnergy);
        sb.append(", dailyPrice=").append(dailyPrice);
        sb.append(", totalPosEnergy=").append(totalPosEnergy);
        sb.append(", totalPosPrice=").append(totalPosPrice);
        sb.append(", totalNegEnergy=").append(totalNegEnergy);
        sb.append(", totalNegPrice=").append(totalNegPrice);
        sb.append(", totalEnergy=").append(totalEnergy);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", plantId=").append(plantId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}