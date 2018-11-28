package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysWeather implements Serializable {
    /**
     * 天气唯一标识
     */
    private Integer id;

    /**
     * 天气
     */
    private String weather;

    /**
     * 天气图标地址
     */
    private String dayPictureUrl;

    private String wind;

    /**
     * 温度范围
     */
    private String temperature;

    /**
     * 当前温度
     */
    private String currentTemperature;

    /**
     * 电站id
     */
    private Integer plantId;

    private Long dataTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDayPictureUrl() {
        return dayPictureUrl;
    }

    public void setDayPictureUrl(String dayPictureUrl) {
        this.dayPictureUrl = dayPictureUrl;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(String currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
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
        SysWeather other = (SysWeather) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWeather() == null ? other.getWeather() == null : this.getWeather().equals(other.getWeather()))
            && (this.getDayPictureUrl() == null ? other.getDayPictureUrl() == null : this.getDayPictureUrl().equals(other.getDayPictureUrl()))
            && (this.getWind() == null ? other.getWind() == null : this.getWind().equals(other.getWind()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getCurrentTemperature() == null ? other.getCurrentTemperature() == null : this.getCurrentTemperature().equals(other.getCurrentTemperature()))
            && (this.getPlantId() == null ? other.getPlantId() == null : this.getPlantId().equals(other.getPlantId()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWeather() == null) ? 0 : getWeather().hashCode());
        result = prime * result + ((getDayPictureUrl() == null) ? 0 : getDayPictureUrl().hashCode());
        result = prime * result + ((getWind() == null) ? 0 : getWind().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getCurrentTemperature() == null) ? 0 : getCurrentTemperature().hashCode());
        result = prime * result + ((getPlantId() == null) ? 0 : getPlantId().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", weather=").append(weather);
        sb.append(", dayPictureUrl=").append(dayPictureUrl);
        sb.append(", wind=").append(wind);
        sb.append(", temperature=").append(temperature);
        sb.append(", currentTemperature=").append(currentTemperature);
        sb.append(", plantId=").append(plantId);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}