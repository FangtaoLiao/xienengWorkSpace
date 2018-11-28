package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollModelDetailMqtt implements Serializable {
    /**
     * 信号点的唯一标识
     */
    private Integer id;

    /**
     * 信号名称
     */
    private String signalName;

    /**
     * 所属点表，引用coll_model
     */
    private Integer dataMode;

    /**
     * 解析的信号类型（YC,YK,YX）
     */
    private String signalType;

    /**
     * 每个点表中，地址序号
     */
    private Integer addressId;

    /**
     * 子信号序号
     */
    private Integer signalSubId;

    /**
     * 型号_信号类型_地址序号_子信号序号的组合，子序号初始是0
     */
    private String signalGuid;

    /**
     * 真实信号类型，兼容老数据
     */
    private String realType;

    /**
     * 数据类型，0有符号数，1表示无符号数
     */
    private Byte dataType;

    /**
     * 数据起始位
     */
    private Integer startBit;

    /**
     * 数据位长度
     */
    private Integer bitLength;

    /**
     * 数据增益
     */
    private Float dataGain;

    /**
     * 修正系数（加减运算）
     */
    private Float correctionFactor;

    /**
     * 最小值（不是必须）
     */
    private Float minval;

    /**
     * 最大值（不是必须）
     */
    private Float maxval;

    /**
     * 单位
     */
    private String dataUnit;

    /**
     * 是否单寄存器信号，0表示是单寄存器信号，1表示多寄存器合成信号
     */
    private String singleRegister;

    /**
     * 特殊处理
     */
    private String specialProcess;

    /**
     * 是否显示,0表示不可见，1表示可见，对遥测2表示直流，3表示交流，4表示其他
     */
    private String visibility;

    /**
     * 是否推送,0表示不推送，1表示推送
     */
    private String push;

    /**
     * 是否有效
     */
    private String valid;
    private Integer dId;
    private String alarmType;
    private Integer statusValue;
    private String statusName;
    private String alarmLevel;
    private Integer ycAlarm;
    private Integer alarmBitLength;
    private String alarmReason;
    private String suggest;
    private String faultPoint;
    
    public void setdId(Integer dId) {
		this.dId = dId;
	}
    public Integer getdId() {
		return dId;
	}
    private static final long serialVersionUID = 1L;

    
    
    /**
	 * @return the alarmType
	 */
	public String getAlarmType() {
		return alarmType;
	}
	/**
	 * @param alarmType the alarmType to set
	 */
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	/**
	 * @return the statusValue
	 */
	public Integer getStatusValue() {
		return statusValue;
	}
	/**
	 * @param statusValue the statusValue to set
	 */
	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}
	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	/**
	 * @return the alarmLevel
	 */
	public String getAlarmLevel() {
		return alarmLevel;
	}
	/**
	 * @param alarmLevel the alarmLevel to set
	 */
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	/**
	 * @return the ycAlarm
	 */
	public Integer getYcAlarm() {
		return ycAlarm;
	}
	/**
	 * @param ycAlarm the ycAlarm to set
	 */
	public void setYcAlarm(Integer ycAlarm) {
		this.ycAlarm = ycAlarm;
	}
	/**
	 * @return the alarmBitLength
	 */
	public Integer getAlarmBitLength() {
		return alarmBitLength;
	}
	/**
	 * @param alarmBitLength the alarmBitLength to set
	 */
	public void setAlarmBitLength(Integer alarmBitLength) {
		this.alarmBitLength = alarmBitLength;
	}
	/**
	 * @return the alarmReason
	 */
	public String getAlarmReason() {
		return alarmReason;
	}
	/**
	 * @param alarmReason the alarmReason to set
	 */
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	/**
	 * @return the suggest
	 */
	public String getSuggest() {
		return suggest;
	}
	/**
	 * @param suggest the suggest to set
	 */
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	/**
	 * @return the faultPoint
	 */
	public String getFaultPoint() {
		return faultPoint;
	}
	/**
	 * @param faultPoint the faultPoint to set
	 */
	public void setFaultPoint(String faultPoint) {
		this.faultPoint = faultPoint;
	}
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Integer getDataMode() {
        return dataMode;
    }

    public void setDataMode(Integer dataMode) {
        this.dataMode = dataMode;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getSignalSubId() {
        return signalSubId;
    }

    public void setSignalSubId(Integer signalSubId) {
        this.signalSubId = signalSubId;
    }

    public String getSignalGuid() {
        return signalGuid;
    }

    public void setSignalGuid(String signalGuid) {
        this.signalGuid = signalGuid;
    }

    public String getRealType() {
        return realType;
    }

    public void setRealType(String realType) {
        this.realType = realType;
    }

    public Byte getDataType() {
        return dataType;
    }

    public void setDataType(Byte dataType) {
        this.dataType = dataType;
    }

    public Integer getStartBit() {
        return startBit;
    }

    public void setStartBit(Integer startBit) {
        this.startBit = startBit;
    }

    public Integer getBitLength() {
        return bitLength;
    }

    public void setBitLength(Integer bitLength) {
        this.bitLength = bitLength;
    }

    public Float getDataGain() {
        return dataGain;
    }

    public void setDataGain(Float dataGain) {
        this.dataGain = dataGain;
    }

    public Float getCorrectionFactor() {
        return correctionFactor;
    }

    public void setCorrectionFactor(Float correctionFactor) {
        this.correctionFactor = correctionFactor;
    }

    public Float getMinval() {
        return minval;
    }

    public void setMinval(Float minval) {
        this.minval = minval;
    }

    public Float getMaxval() {
        return maxval;
    }

    public void setMaxval(Float maxval) {
        this.maxval = maxval;
    }

    public String getDataUnit() {
        return dataUnit;
    }

    public void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    public String getSingleRegister() {
        return singleRegister;
    }

    public void setSingleRegister(String singleRegister) {
        this.singleRegister = singleRegister;
    }

    public String getSpecialProcess() {
        return specialProcess;
    }

    public void setSpecialProcess(String specialProcess) {
        this.specialProcess = specialProcess;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
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
        CollModelDetailMqtt other = (CollModelDetailMqtt) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSignalName() == null ? other.getSignalName() == null : this.getSignalName().equals(other.getSignalName()))
            && (this.getDataMode() == null ? other.getDataMode() == null : this.getDataMode().equals(other.getDataMode()))
            && (this.getSignalType() == null ? other.getSignalType() == null : this.getSignalType().equals(other.getSignalType()))
            && (this.getAddressId() == null ? other.getAddressId() == null : this.getAddressId().equals(other.getAddressId()))
            && (this.getSignalSubId() == null ? other.getSignalSubId() == null : this.getSignalSubId().equals(other.getSignalSubId()))
            && (this.getSignalGuid() == null ? other.getSignalGuid() == null : this.getSignalGuid().equals(other.getSignalGuid()))
            && (this.getRealType() == null ? other.getRealType() == null : this.getRealType().equals(other.getRealType()))
            && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
            && (this.getStartBit() == null ? other.getStartBit() == null : this.getStartBit().equals(other.getStartBit()))
            && (this.getBitLength() == null ? other.getBitLength() == null : this.getBitLength().equals(other.getBitLength()))
            && (this.getDataGain() == null ? other.getDataGain() == null : this.getDataGain().equals(other.getDataGain()))
            && (this.getCorrectionFactor() == null ? other.getCorrectionFactor() == null : this.getCorrectionFactor().equals(other.getCorrectionFactor()))
            && (this.getMinval() == null ? other.getMinval() == null : this.getMinval().equals(other.getMinval()))
            && (this.getMaxval() == null ? other.getMaxval() == null : this.getMaxval().equals(other.getMaxval()))
            && (this.getDataUnit() == null ? other.getDataUnit() == null : this.getDataUnit().equals(other.getDataUnit()))
            && (this.getSingleRegister() == null ? other.getSingleRegister() == null : this.getSingleRegister().equals(other.getSingleRegister()))
            && (this.getSpecialProcess() == null ? other.getSpecialProcess() == null : this.getSpecialProcess().equals(other.getSpecialProcess()))
            && (this.getVisibility() == null ? other.getVisibility() == null : this.getVisibility().equals(other.getVisibility()))
            && (this.getPush() == null ? other.getPush() == null : this.getPush().equals(other.getPush()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSignalName() == null) ? 0 : getSignalName().hashCode());
        result = prime * result + ((getDataMode() == null) ? 0 : getDataMode().hashCode());
        result = prime * result + ((getSignalType() == null) ? 0 : getSignalType().hashCode());
        result = prime * result + ((getAddressId() == null) ? 0 : getAddressId().hashCode());
        result = prime * result + ((getSignalSubId() == null) ? 0 : getSignalSubId().hashCode());
        result = prime * result + ((getSignalGuid() == null) ? 0 : getSignalGuid().hashCode());
        result = prime * result + ((getRealType() == null) ? 0 : getRealType().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getStartBit() == null) ? 0 : getStartBit().hashCode());
        result = prime * result + ((getBitLength() == null) ? 0 : getBitLength().hashCode());
        result = prime * result + ((getDataGain() == null) ? 0 : getDataGain().hashCode());
        result = prime * result + ((getCorrectionFactor() == null) ? 0 : getCorrectionFactor().hashCode());
        result = prime * result + ((getMinval() == null) ? 0 : getMinval().hashCode());
        result = prime * result + ((getMaxval() == null) ? 0 : getMaxval().hashCode());
        result = prime * result + ((getDataUnit() == null) ? 0 : getDataUnit().hashCode());
        result = prime * result + ((getSingleRegister() == null) ? 0 : getSingleRegister().hashCode());
        result = prime * result + ((getSpecialProcess() == null) ? 0 : getSpecialProcess().hashCode());
        result = prime * result + ((getVisibility() == null) ? 0 : getVisibility().hashCode());
        result = prime * result + ((getPush() == null) ? 0 : getPush().hashCode());
        result = prime * result + ((getValid() == null) ? 0 : getValid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", signalName=").append(signalName);
        sb.append(", dataMode=").append(dataMode);
        sb.append(", signalType=").append(signalType);
        sb.append(", addressId=").append(addressId);
        sb.append(", signalSubId=").append(signalSubId);
        sb.append(", signalGuid=").append(signalGuid);
        sb.append(", realType=").append(realType);
        sb.append(", dataType=").append(dataType);
        sb.append(", startBit=").append(startBit);
        sb.append(", bitLength=").append(bitLength);
        sb.append(", dataGain=").append(dataGain);
        sb.append(", correctionFactor=").append(correctionFactor);
        sb.append(", minval=").append(minval);
        sb.append(", maxval=").append(maxval);
        sb.append(", dataUnit=").append(dataUnit);
        sb.append(", singleRegister=").append(singleRegister);
        sb.append(", specialProcess=").append(specialProcess);
        sb.append(", visibility=").append(visibility);
        sb.append(", push=").append(push);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}