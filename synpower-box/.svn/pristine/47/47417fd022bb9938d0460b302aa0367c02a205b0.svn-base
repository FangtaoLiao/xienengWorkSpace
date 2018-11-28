package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DeviceControlData implements Serializable {
    private Integer id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 操作时间
     */
    private Long operationTime;

    /**
     * 信号点id
     */
    private String signalGuid;

    private String signalName;
    /**
     * 下发值
     */
    private Integer value;

    /**
     * 操作者
     */
    private Integer operator;

    private static final long serialVersionUID = 1L;
	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}
	public String getSignalName() {
		return signalName;
	}
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Long operationTime) {
        this.operationTime = operationTime;
    }

    public String getSignalGuid() {
        return signalGuid;
    }

    public void setSignalGuid(String signalGuid) {
        this.signalGuid = signalGuid;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
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
        DeviceControlData other = (DeviceControlData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getOperationTime() == null ? other.getOperationTime() == null : this.getOperationTime().equals(other.getOperationTime()))
            && (this.getSignalGuid() == null ? other.getSignalGuid() == null : this.getSignalGuid().equals(other.getSignalGuid()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getOperationTime() == null) ? 0 : getOperationTime().hashCode());
        result = prime * result + ((getSignalGuid() == null) ? 0 : getSignalGuid().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
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
        sb.append(", operationTime=").append(operationTime);
        sb.append(", signalGuid=").append(signalGuid);
        sb.append(", value=").append(value);
        sb.append(", operator=").append(operator);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}