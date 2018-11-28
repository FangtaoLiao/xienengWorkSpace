package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysPoorDetail implements Serializable {
    /**
     * 贫困户的唯一标识
     */
    private Integer id;

    /**
     * 贫困户的名称
     */
    private String name;

    /**
     * 贫困户的人口
     */
    private Integer population;

    /**
     * 扶贫增收
     */
    private Double increaseIncome;

    /**
     * 对应的电站id
     */
    private Integer plantId;

    /**
     * 对应公司id
     */
    private Integer orgId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getIncreaseIncome() {
        return increaseIncome;
    }

    public void setIncreaseIncome(Double increaseIncome) {
        this.increaseIncome = increaseIncome;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
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
        SysPoorDetail other = (SysPoorDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPopulation() == null ? other.getPopulation() == null : this.getPopulation().equals(other.getPopulation()))
            && (this.getIncreaseIncome() == null ? other.getIncreaseIncome() == null : this.getIncreaseIncome().equals(other.getIncreaseIncome()))
            && (this.getPlantId() == null ? other.getPlantId() == null : this.getPlantId().equals(other.getPlantId()))
            && (this.getOrgId() == null ? other.getOrgId() == null : this.getOrgId().equals(other.getOrgId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPopulation() == null) ? 0 : getPopulation().hashCode());
        result = prime * result + ((getIncreaseIncome() == null) ? 0 : getIncreaseIncome().hashCode());
        result = prime * result + ((getPlantId() == null) ? 0 : getPlantId().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", population=").append(population);
        sb.append(", increaseIncome=").append(increaseIncome);
        sb.append(", plantId=").append(plantId);
        sb.append(", orgId=").append(orgId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}