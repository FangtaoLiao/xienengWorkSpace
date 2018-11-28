package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DataElectricMeter implements Serializable {
    /**
     * 数据唯一标识
     */
    private Integer id;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 电站
     */
    private Integer plant;

    /**
     * 数据时间戳
     */
    private Long dataTime;

    /**
     * A相电压
     */
    private Float emUa;

    /**
     * B相电压
     */
    private Float emUb;

    /**
     * C相电压
     */
    private Float emUc;

    /**
     * AB电压
     */
    private Float emUab;

    /**
     * BC电压
     */
    private Float emUbc;

    /**
     * CA电压
     */
    private Float emUca;

    /**
     * A相电流
     */
    private Float emIa;

    /**
     * B相电流
     */
    private Float emIb;

    /**
     * C相电流
     */
    private Float emIc;

    /**
     * 电网频率
     */
    private Float emFrequency;

    /**
     * 有功功率
     */
    private Float emActivePower;

    /**
     * 无功功率
     */
    private Float emReactivePower;

    /**
     * 功率因素
     */
    private Float emPowerFactor;

    /**
     * 正向有功电能量
     */
    private Float emPosActiveEnergy;

    /**
     * 反向有功电能量
     */
    private Float emNegActiveEnergy;

    /**
     * 正向尖总有功电能
     */
    private Float emPosJianActiveEnergy;

    /**
     * 正向峰总有功电能
     */
    private Float emPosFengActiveEnergy;

    /**
     * 正向平总有功电能
     */
    private Float emPosPingActiveEnergy;

    /**
     * 正向谷总有功电能
     */
    private Float emPosGuActiveEnergy;

    /**
     * 反向尖总有功电能
     */
    private Float emNegJianActiveEnergy;

    /**
     * 反向峰总有功电能
     */
    private Float emNegFengActiveEnergy;

    /**
     * 反向平总有功电能
     */
    private Float emNegPingActiveEnergy;

    /**
     * 反向谷总有功电能
     */
    private Float emNegGuActiveEnergy;

    /**
     * 正向无功电能量
     */
    private Float emPosReactiveEnergy;

    /**
     * 反向无功电能量
     */
    private Float emNegReactiveEnergy;

    /**
     * 正向尖总无功电能
     */
    private Float emPosJianReactiveEnergy;

    /**
     * 正向峰总无功电能
     */
    private Float emPosFengReactiveEnergy;

    /**
     * 正向平总无功电能
     */
    private Float emPosPingReactiveEnergy;

    /**
     * 正向谷总无功电能
     */
    private Float emPosGuReactiveEnergy;

    /**
     * 反向尖总无功电能
     */
    private Float emNegJianReactiveEnergy;

    /**
     * 反向峰总无功电能
     */
    private Float emNegFengReactiveEnergy;

    /**
     * 反向平总无功电能
     */
    private Float emNegPingReactiveEnergy;

    /**
     * 反向谷总无功电能
     */
    private Float emNegGuReactiveEnergy;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPlant() {
        return plant;
    }

    public void setPlant(Integer plant) {
        this.plant = plant;
    }

    public Long getDataTime() {
        return dataTime;
    }

    public void setDataTime(Long dataTime) {
        this.dataTime = dataTime;
    }

    public Float getEmUa() {
        return emUa;
    }

    public void setEmUa(Float emUa) {
        this.emUa = emUa;
    }

    public Float getEmUb() {
        return emUb;
    }

    public void setEmUb(Float emUb) {
        this.emUb = emUb;
    }

    public Float getEmUc() {
        return emUc;
    }

    public void setEmUc(Float emUc) {
        this.emUc = emUc;
    }

    public Float getEmUab() {
        return emUab;
    }

    public void setEmUab(Float emUab) {
        this.emUab = emUab;
    }

    public Float getEmUbc() {
        return emUbc;
    }

    public void setEmUbc(Float emUbc) {
        this.emUbc = emUbc;
    }

    public Float getEmUca() {
        return emUca;
    }

    public void setEmUca(Float emUca) {
        this.emUca = emUca;
    }

    public Float getEmIa() {
        return emIa;
    }

    public void setEmIa(Float emIa) {
        this.emIa = emIa;
    }

    public Float getEmIb() {
        return emIb;
    }

    public void setEmIb(Float emIb) {
        this.emIb = emIb;
    }

    public Float getEmIc() {
        return emIc;
    }

    public void setEmIc(Float emIc) {
        this.emIc = emIc;
    }

    public Float getEmFrequency() {
        return emFrequency;
    }

    public void setEmFrequency(Float emFrequency) {
        this.emFrequency = emFrequency;
    }

    public Float getEmActivePower() {
        return emActivePower;
    }

    public void setEmActivePower(Float emActivePower) {
        this.emActivePower = emActivePower;
    }

    public Float getEmReactivePower() {
        return emReactivePower;
    }

    public void setEmReactivePower(Float emReactivePower) {
        this.emReactivePower = emReactivePower;
    }

    public Float getEmPowerFactor() {
        return emPowerFactor;
    }

    public void setEmPowerFactor(Float emPowerFactor) {
        this.emPowerFactor = emPowerFactor;
    }

    public Float getEmPosActiveEnergy() {
        return emPosActiveEnergy;
    }

    public void setEmPosActiveEnergy(Float emPosActiveEnergy) {
        this.emPosActiveEnergy = emPosActiveEnergy;
    }

    public Float getEmNegActiveEnergy() {
        return emNegActiveEnergy;
    }

    public void setEmNegActiveEnergy(Float emNegActiveEnergy) {
        this.emNegActiveEnergy = emNegActiveEnergy;
    }

    public Float getEmPosJianActiveEnergy() {
        return emPosJianActiveEnergy;
    }

    public void setEmPosJianActiveEnergy(Float emPosJianActiveEnergy) {
        this.emPosJianActiveEnergy = emPosJianActiveEnergy;
    }

    public Float getEmPosFengActiveEnergy() {
        return emPosFengActiveEnergy;
    }

    public void setEmPosFengActiveEnergy(Float emPosFengActiveEnergy) {
        this.emPosFengActiveEnergy = emPosFengActiveEnergy;
    }

    public Float getEmPosPingActiveEnergy() {
        return emPosPingActiveEnergy;
    }

    public void setEmPosPingActiveEnergy(Float emPosPingActiveEnergy) {
        this.emPosPingActiveEnergy = emPosPingActiveEnergy;
    }

    public Float getEmPosGuActiveEnergy() {
        return emPosGuActiveEnergy;
    }

    public void setEmPosGuActiveEnergy(Float emPosGuActiveEnergy) {
        this.emPosGuActiveEnergy = emPosGuActiveEnergy;
    }

    public Float getEmNegJianActiveEnergy() {
        return emNegJianActiveEnergy;
    }

    public void setEmNegJianActiveEnergy(Float emNegJianActiveEnergy) {
        this.emNegJianActiveEnergy = emNegJianActiveEnergy;
    }

    public Float getEmNegFengActiveEnergy() {
        return emNegFengActiveEnergy;
    }

    public void setEmNegFengActiveEnergy(Float emNegFengActiveEnergy) {
        this.emNegFengActiveEnergy = emNegFengActiveEnergy;
    }

    public Float getEmNegPingActiveEnergy() {
        return emNegPingActiveEnergy;
    }

    public void setEmNegPingActiveEnergy(Float emNegPingActiveEnergy) {
        this.emNegPingActiveEnergy = emNegPingActiveEnergy;
    }

    public Float getEmNegGuActiveEnergy() {
        return emNegGuActiveEnergy;
    }

    public void setEmNegGuActiveEnergy(Float emNegGuActiveEnergy) {
        this.emNegGuActiveEnergy = emNegGuActiveEnergy;
    }

    public Float getEmPosReactiveEnergy() {
        return emPosReactiveEnergy;
    }

    public void setEmPosReactiveEnergy(Float emPosReactiveEnergy) {
        this.emPosReactiveEnergy = emPosReactiveEnergy;
    }

    public Float getEmNegReactiveEnergy() {
        return emNegReactiveEnergy;
    }

    public void setEmNegReactiveEnergy(Float emNegReactiveEnergy) {
        this.emNegReactiveEnergy = emNegReactiveEnergy;
    }

    public Float getEmPosJianReactiveEnergy() {
        return emPosJianReactiveEnergy;
    }

    public void setEmPosJianReactiveEnergy(Float emPosJianReactiveEnergy) {
        this.emPosJianReactiveEnergy = emPosJianReactiveEnergy;
    }

    public Float getEmPosFengReactiveEnergy() {
        return emPosFengReactiveEnergy;
    }

    public void setEmPosFengReactiveEnergy(Float emPosFengReactiveEnergy) {
        this.emPosFengReactiveEnergy = emPosFengReactiveEnergy;
    }

    public Float getEmPosPingReactiveEnergy() {
        return emPosPingReactiveEnergy;
    }

    public void setEmPosPingReactiveEnergy(Float emPosPingReactiveEnergy) {
        this.emPosPingReactiveEnergy = emPosPingReactiveEnergy;
    }

    public Float getEmPosGuReactiveEnergy() {
        return emPosGuReactiveEnergy;
    }

    public void setEmPosGuReactiveEnergy(Float emPosGuReactiveEnergy) {
        this.emPosGuReactiveEnergy = emPosGuReactiveEnergy;
    }

    public Float getEmNegJianReactiveEnergy() {
        return emNegJianReactiveEnergy;
    }

    public void setEmNegJianReactiveEnergy(Float emNegJianReactiveEnergy) {
        this.emNegJianReactiveEnergy = emNegJianReactiveEnergy;
    }

    public Float getEmNegFengReactiveEnergy() {
        return emNegFengReactiveEnergy;
    }

    public void setEmNegFengReactiveEnergy(Float emNegFengReactiveEnergy) {
        this.emNegFengReactiveEnergy = emNegFengReactiveEnergy;
    }

    public Float getEmNegPingReactiveEnergy() {
        return emNegPingReactiveEnergy;
    }

    public void setEmNegPingReactiveEnergy(Float emNegPingReactiveEnergy) {
        this.emNegPingReactiveEnergy = emNegPingReactiveEnergy;
    }

    public Float getEmNegGuReactiveEnergy() {
        return emNegGuReactiveEnergy;
    }

    public void setEmNegGuReactiveEnergy(Float emNegGuReactiveEnergy) {
        this.emNegGuReactiveEnergy = emNegGuReactiveEnergy;
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
        DataElectricMeter other = (DataElectricMeter) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getPlant() == null ? other.getPlant() == null : this.getPlant().equals(other.getPlant()))
            && (this.getDataTime() == null ? other.getDataTime() == null : this.getDataTime().equals(other.getDataTime()))
            && (this.getEmUa() == null ? other.getEmUa() == null : this.getEmUa().equals(other.getEmUa()))
            && (this.getEmUb() == null ? other.getEmUb() == null : this.getEmUb().equals(other.getEmUb()))
            && (this.getEmUc() == null ? other.getEmUc() == null : this.getEmUc().equals(other.getEmUc()))
            && (this.getEmUab() == null ? other.getEmUab() == null : this.getEmUab().equals(other.getEmUab()))
            && (this.getEmUbc() == null ? other.getEmUbc() == null : this.getEmUbc().equals(other.getEmUbc()))
            && (this.getEmUca() == null ? other.getEmUca() == null : this.getEmUca().equals(other.getEmUca()))
            && (this.getEmIa() == null ? other.getEmIa() == null : this.getEmIa().equals(other.getEmIa()))
            && (this.getEmIb() == null ? other.getEmIb() == null : this.getEmIb().equals(other.getEmIb()))
            && (this.getEmIc() == null ? other.getEmIc() == null : this.getEmIc().equals(other.getEmIc()))
            && (this.getEmFrequency() == null ? other.getEmFrequency() == null : this.getEmFrequency().equals(other.getEmFrequency()))
            && (this.getEmActivePower() == null ? other.getEmActivePower() == null : this.getEmActivePower().equals(other.getEmActivePower()))
            && (this.getEmReactivePower() == null ? other.getEmReactivePower() == null : this.getEmReactivePower().equals(other.getEmReactivePower()))
            && (this.getEmPowerFactor() == null ? other.getEmPowerFactor() == null : this.getEmPowerFactor().equals(other.getEmPowerFactor()))
            && (this.getEmPosActiveEnergy() == null ? other.getEmPosActiveEnergy() == null : this.getEmPosActiveEnergy().equals(other.getEmPosActiveEnergy()))
            && (this.getEmNegActiveEnergy() == null ? other.getEmNegActiveEnergy() == null : this.getEmNegActiveEnergy().equals(other.getEmNegActiveEnergy()))
            && (this.getEmPosJianActiveEnergy() == null ? other.getEmPosJianActiveEnergy() == null : this.getEmPosJianActiveEnergy().equals(other.getEmPosJianActiveEnergy()))
            && (this.getEmPosFengActiveEnergy() == null ? other.getEmPosFengActiveEnergy() == null : this.getEmPosFengActiveEnergy().equals(other.getEmPosFengActiveEnergy()))
            && (this.getEmPosPingActiveEnergy() == null ? other.getEmPosPingActiveEnergy() == null : this.getEmPosPingActiveEnergy().equals(other.getEmPosPingActiveEnergy()))
            && (this.getEmPosGuActiveEnergy() == null ? other.getEmPosGuActiveEnergy() == null : this.getEmPosGuActiveEnergy().equals(other.getEmPosGuActiveEnergy()))
            && (this.getEmNegJianActiveEnergy() == null ? other.getEmNegJianActiveEnergy() == null : this.getEmNegJianActiveEnergy().equals(other.getEmNegJianActiveEnergy()))
            && (this.getEmNegFengActiveEnergy() == null ? other.getEmNegFengActiveEnergy() == null : this.getEmNegFengActiveEnergy().equals(other.getEmNegFengActiveEnergy()))
            && (this.getEmNegPingActiveEnergy() == null ? other.getEmNegPingActiveEnergy() == null : this.getEmNegPingActiveEnergy().equals(other.getEmNegPingActiveEnergy()))
            && (this.getEmNegGuActiveEnergy() == null ? other.getEmNegGuActiveEnergy() == null : this.getEmNegGuActiveEnergy().equals(other.getEmNegGuActiveEnergy()))
            && (this.getEmPosReactiveEnergy() == null ? other.getEmPosReactiveEnergy() == null : this.getEmPosReactiveEnergy().equals(other.getEmPosReactiveEnergy()))
            && (this.getEmNegReactiveEnergy() == null ? other.getEmNegReactiveEnergy() == null : this.getEmNegReactiveEnergy().equals(other.getEmNegReactiveEnergy()))
            && (this.getEmPosJianReactiveEnergy() == null ? other.getEmPosJianReactiveEnergy() == null : this.getEmPosJianReactiveEnergy().equals(other.getEmPosJianReactiveEnergy()))
            && (this.getEmPosFengReactiveEnergy() == null ? other.getEmPosFengReactiveEnergy() == null : this.getEmPosFengReactiveEnergy().equals(other.getEmPosFengReactiveEnergy()))
            && (this.getEmPosPingReactiveEnergy() == null ? other.getEmPosPingReactiveEnergy() == null : this.getEmPosPingReactiveEnergy().equals(other.getEmPosPingReactiveEnergy()))
            && (this.getEmPosGuReactiveEnergy() == null ? other.getEmPosGuReactiveEnergy() == null : this.getEmPosGuReactiveEnergy().equals(other.getEmPosGuReactiveEnergy()))
            && (this.getEmNegJianReactiveEnergy() == null ? other.getEmNegJianReactiveEnergy() == null : this.getEmNegJianReactiveEnergy().equals(other.getEmNegJianReactiveEnergy()))
            && (this.getEmNegFengReactiveEnergy() == null ? other.getEmNegFengReactiveEnergy() == null : this.getEmNegFengReactiveEnergy().equals(other.getEmNegFengReactiveEnergy()))
            && (this.getEmNegPingReactiveEnergy() == null ? other.getEmNegPingReactiveEnergy() == null : this.getEmNegPingReactiveEnergy().equals(other.getEmNegPingReactiveEnergy()))
            && (this.getEmNegGuReactiveEnergy() == null ? other.getEmNegGuReactiveEnergy() == null : this.getEmNegGuReactiveEnergy().equals(other.getEmNegGuReactiveEnergy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getPlant() == null) ? 0 : getPlant().hashCode());
        result = prime * result + ((getDataTime() == null) ? 0 : getDataTime().hashCode());
        result = prime * result + ((getEmUa() == null) ? 0 : getEmUa().hashCode());
        result = prime * result + ((getEmUb() == null) ? 0 : getEmUb().hashCode());
        result = prime * result + ((getEmUc() == null) ? 0 : getEmUc().hashCode());
        result = prime * result + ((getEmUab() == null) ? 0 : getEmUab().hashCode());
        result = prime * result + ((getEmUbc() == null) ? 0 : getEmUbc().hashCode());
        result = prime * result + ((getEmUca() == null) ? 0 : getEmUca().hashCode());
        result = prime * result + ((getEmIa() == null) ? 0 : getEmIa().hashCode());
        result = prime * result + ((getEmIb() == null) ? 0 : getEmIb().hashCode());
        result = prime * result + ((getEmIc() == null) ? 0 : getEmIc().hashCode());
        result = prime * result + ((getEmFrequency() == null) ? 0 : getEmFrequency().hashCode());
        result = prime * result + ((getEmActivePower() == null) ? 0 : getEmActivePower().hashCode());
        result = prime * result + ((getEmReactivePower() == null) ? 0 : getEmReactivePower().hashCode());
        result = prime * result + ((getEmPowerFactor() == null) ? 0 : getEmPowerFactor().hashCode());
        result = prime * result + ((getEmPosActiveEnergy() == null) ? 0 : getEmPosActiveEnergy().hashCode());
        result = prime * result + ((getEmNegActiveEnergy() == null) ? 0 : getEmNegActiveEnergy().hashCode());
        result = prime * result + ((getEmPosJianActiveEnergy() == null) ? 0 : getEmPosJianActiveEnergy().hashCode());
        result = prime * result + ((getEmPosFengActiveEnergy() == null) ? 0 : getEmPosFengActiveEnergy().hashCode());
        result = prime * result + ((getEmPosPingActiveEnergy() == null) ? 0 : getEmPosPingActiveEnergy().hashCode());
        result = prime * result + ((getEmPosGuActiveEnergy() == null) ? 0 : getEmPosGuActiveEnergy().hashCode());
        result = prime * result + ((getEmNegJianActiveEnergy() == null) ? 0 : getEmNegJianActiveEnergy().hashCode());
        result = prime * result + ((getEmNegFengActiveEnergy() == null) ? 0 : getEmNegFengActiveEnergy().hashCode());
        result = prime * result + ((getEmNegPingActiveEnergy() == null) ? 0 : getEmNegPingActiveEnergy().hashCode());
        result = prime * result + ((getEmNegGuActiveEnergy() == null) ? 0 : getEmNegGuActiveEnergy().hashCode());
        result = prime * result + ((getEmPosReactiveEnergy() == null) ? 0 : getEmPosReactiveEnergy().hashCode());
        result = prime * result + ((getEmNegReactiveEnergy() == null) ? 0 : getEmNegReactiveEnergy().hashCode());
        result = prime * result + ((getEmPosJianReactiveEnergy() == null) ? 0 : getEmPosJianReactiveEnergy().hashCode());
        result = prime * result + ((getEmPosFengReactiveEnergy() == null) ? 0 : getEmPosFengReactiveEnergy().hashCode());
        result = prime * result + ((getEmPosPingReactiveEnergy() == null) ? 0 : getEmPosPingReactiveEnergy().hashCode());
        result = prime * result + ((getEmPosGuReactiveEnergy() == null) ? 0 : getEmPosGuReactiveEnergy().hashCode());
        result = prime * result + ((getEmNegJianReactiveEnergy() == null) ? 0 : getEmNegJianReactiveEnergy().hashCode());
        result = prime * result + ((getEmNegFengReactiveEnergy() == null) ? 0 : getEmNegFengReactiveEnergy().hashCode());
        result = prime * result + ((getEmNegPingReactiveEnergy() == null) ? 0 : getEmNegPingReactiveEnergy().hashCode());
        result = prime * result + ((getEmNegGuReactiveEnergy() == null) ? 0 : getEmNegGuReactiveEnergy().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", plant=").append(plant);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", emUa=").append(emUa);
        sb.append(", emUb=").append(emUb);
        sb.append(", emUc=").append(emUc);
        sb.append(", emUab=").append(emUab);
        sb.append(", emUbc=").append(emUbc);
        sb.append(", emUca=").append(emUca);
        sb.append(", emIa=").append(emIa);
        sb.append(", emIb=").append(emIb);
        sb.append(", emIc=").append(emIc);
        sb.append(", emFrequency=").append(emFrequency);
        sb.append(", emActivePower=").append(emActivePower);
        sb.append(", emReactivePower=").append(emReactivePower);
        sb.append(", emPowerFactor=").append(emPowerFactor);
        sb.append(", emPosActiveEnergy=").append(emPosActiveEnergy);
        sb.append(", emNegActiveEnergy=").append(emNegActiveEnergy);
        sb.append(", emPosJianActiveEnergy=").append(emPosJianActiveEnergy);
        sb.append(", emPosFengActiveEnergy=").append(emPosFengActiveEnergy);
        sb.append(", emPosPingActiveEnergy=").append(emPosPingActiveEnergy);
        sb.append(", emPosGuActiveEnergy=").append(emPosGuActiveEnergy);
        sb.append(", emNegJianActiveEnergy=").append(emNegJianActiveEnergy);
        sb.append(", emNegFengActiveEnergy=").append(emNegFengActiveEnergy);
        sb.append(", emNegPingActiveEnergy=").append(emNegPingActiveEnergy);
        sb.append(", emNegGuActiveEnergy=").append(emNegGuActiveEnergy);
        sb.append(", emPosReactiveEnergy=").append(emPosReactiveEnergy);
        sb.append(", emNegReactiveEnergy=").append(emNegReactiveEnergy);
        sb.append(", emPosJianReactiveEnergy=").append(emPosJianReactiveEnergy);
        sb.append(", emPosFengReactiveEnergy=").append(emPosFengReactiveEnergy);
        sb.append(", emPosPingReactiveEnergy=").append(emPosPingReactiveEnergy);
        sb.append(", emPosGuReactiveEnergy=").append(emPosGuReactiveEnergy);
        sb.append(", emNegJianReactiveEnergy=").append(emNegJianReactiveEnergy);
        sb.append(", emNegFengReactiveEnergy=").append(emNegFengReactiveEnergy);
        sb.append(", emNegPingReactiveEnergy=").append(emNegPingReactiveEnergy);
        sb.append(", emNegGuReactiveEnergy=").append(emNegGuReactiveEnergy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}