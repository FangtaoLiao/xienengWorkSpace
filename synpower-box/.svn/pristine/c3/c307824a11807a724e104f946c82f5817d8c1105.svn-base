package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysPoorInfo implements Serializable {
    private Integer id;

    /**
     * 总贫困户数量
     */
    private Integer totalNum;

    /**
     * 已覆盖数量
     */
    private Integer coverNum;

    /**
     * 增收
     */
    private Double increaseIncome;

    /**
     * 投资
     */
    private Double invest;

    /**
     * 公司id
     */
    private Integer orgId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(Integer coverNum) {
        this.coverNum = coverNum;
    }

    public Double getIncreaseIncome() {
        return increaseIncome;
    }

    public void setIncreaseIncome(Double increaseIncome) {
        this.increaseIncome = increaseIncome;
    }

    public Double getInvest() {
        return invest;
    }

    public void setInvest(Double invest) {
        this.invest = invest;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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
        SysPoorInfo other = (SysPoorInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTotalNum() == null ? other.getTotalNum() == null : this.getTotalNum().equals(other.getTotalNum()))
            && (this.getCoverNum() == null ? other.getCoverNum() == null : this.getCoverNum().equals(other.getCoverNum()))
            && (this.getIncreaseIncome() == null ? other.getIncreaseIncome() == null : this.getIncreaseIncome().equals(other.getIncreaseIncome()))
            && (this.getInvest() == null ? other.getInvest() == null : this.getInvest().equals(other.getInvest()))
            && (this.getOrgId() == null ? other.getOrgId() == null : this.getOrgId().equals(other.getOrgId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTotalNum() == null) ? 0 : getTotalNum().hashCode());
        result = prime * result + ((getCoverNum() == null) ? 0 : getCoverNum().hashCode());
        result = prime * result + ((getIncreaseIncome() == null) ? 0 : getIncreaseIncome().hashCode());
        result = prime * result + ((getInvest() == null) ? 0 : getInvest().hashCode());
        result = prime * result + ((getOrgId() == null) ? 0 : getOrgId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", totalNum=").append(totalNum);
        sb.append(", coverNum=").append(coverNum);
        sb.append(", increaseIncome=").append(increaseIncome);
        sb.append(", invest=").append(invest);
        sb.append(", orgId=").append(orgId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}