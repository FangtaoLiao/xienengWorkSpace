package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysPowerPriceDetail implements Serializable {
    private Integer id;
    /**
     * 上网电价
     */
    private Float posPrice;

    /**
     * 下网电价
     */
    private Float negPrice;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 1,2,3,4对应尖峰平谷
     */
    private Integer priceType;

    /**
     * 对应sys_power_price的id
     */
    private Integer priceId;

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

    public Float getPosPrice() {
        return posPrice;
    }

    public void setPosPrice(Float posPrice) {
        this.posPrice = posPrice;
    }

    public Float getNegPrice() {
        return negPrice;
    }

    public void setNegPrice(Float negPrice) {
        this.negPrice = negPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
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
        SysPowerPriceDetail other = (SysPowerPriceDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPosPrice() == null ? other.getPosPrice() == null : this.getPosPrice().equals(other.getPosPrice()))
            && (this.getNegPrice() == null ? other.getNegPrice() == null : this.getNegPrice().equals(other.getNegPrice()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getPriceType() == null ? other.getPriceType() == null : this.getPriceType().equals(other.getPriceType()))
            && (this.getPriceId() == null ? other.getPriceId() == null : this.getPriceId().equals(other.getPriceId()))
            && (this.getPriceValid() == null ? other.getPriceValid() == null : this.getPriceValid().equals(other.getPriceValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPosPrice() == null) ? 0 : getPosPrice().hashCode());
        result = prime * result + ((getNegPrice() == null) ? 0 : getNegPrice().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getPriceType() == null) ? 0 : getPriceType().hashCode());
        result = prime * result + ((getPriceId() == null) ? 0 : getPriceId().hashCode());
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
        sb.append(", posPrice=").append(posPrice);
        sb.append(", negPrice=").append(negPrice);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", priceType=").append(priceType);
        sb.append(", priceId=").append(priceId);
        sb.append(", priceValid=").append(priceValid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}