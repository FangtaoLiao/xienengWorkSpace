package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class PowerPrice implements Serializable {
    private Integer id;

    /**
     * 尖电价
     */
    private Float jianPrice;

    /**
     * 峰电价
     */
    private Float fengPrice;

    /**
     * 平电价
     */
    private Float pingPrice;

    /**
     * 谷电价
     */
    private Float guPrice;

    /**
     * 电站id
     */
    private Integer plantId;

    /**
     * 1删除0未删除
     */
    private String priceValid;

    /**
     * 1启用0禁用
     */
    private String priceStatus;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getJianPrice() {
        return jianPrice;
    }

    public void setJianPrice(Float jianPrice) {
        this.jianPrice = jianPrice;
    }

    public Float getFengPrice() {
        return fengPrice;
    }

    public void setFengPrice(Float fengPrice) {
        this.fengPrice = fengPrice;
    }

    public Float getPingPrice() {
        return pingPrice;
    }

    public void setPingPrice(Float pingPrice) {
        this.pingPrice = pingPrice;
    }

    public Float getGuPrice() {
        return guPrice;
    }

    public void setGuPrice(Float guPrice) {
        this.guPrice = guPrice;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getPriceValid() {
        return priceValid;
    }

    public void setPriceValid(String priceValid) {
        this.priceValid = priceValid;
    }

    public String getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(String priceStatus) {
        this.priceStatus = priceStatus;
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
        PowerPrice other = (PowerPrice) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getJianPrice() == null ? other.getJianPrice() == null : this.getJianPrice().equals(other.getJianPrice()))
            && (this.getFengPrice() == null ? other.getFengPrice() == null : this.getFengPrice().equals(other.getFengPrice()))
            && (this.getPingPrice() == null ? other.getPingPrice() == null : this.getPingPrice().equals(other.getPingPrice()))
            && (this.getGuPrice() == null ? other.getGuPrice() == null : this.getGuPrice().equals(other.getGuPrice()))
            && (this.getPlantId() == null ? other.getPlantId() == null : this.getPlantId().equals(other.getPlantId()))
            && (this.getPriceValid() == null ? other.getPriceValid() == null : this.getPriceValid().equals(other.getPriceValid()))
            && (this.getPriceStatus() == null ? other.getPriceStatus() == null : this.getPriceStatus().equals(other.getPriceStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getJianPrice() == null) ? 0 : getJianPrice().hashCode());
        result = prime * result + ((getFengPrice() == null) ? 0 : getFengPrice().hashCode());
        result = prime * result + ((getPingPrice() == null) ? 0 : getPingPrice().hashCode());
        result = prime * result + ((getGuPrice() == null) ? 0 : getGuPrice().hashCode());
        result = prime * result + ((getPlantId() == null) ? 0 : getPlantId().hashCode());
        result = prime * result + ((getPriceValid() == null) ? 0 : getPriceValid().hashCode());
        result = prime * result + ((getPriceStatus() == null) ? 0 : getPriceStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", jianPrice=").append(jianPrice);
        sb.append(", fengPrice=").append(fengPrice);
        sb.append(", pingPrice=").append(pingPrice);
        sb.append(", guPrice=").append(guPrice);
        sb.append(", plantId=").append(plantId);
        sb.append(", priceValid=").append(priceValid);
        sb.append(", priceStatus=").append(priceStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}