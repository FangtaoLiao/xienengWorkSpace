package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DataYx implements Serializable {
    /**
     * 遥信唯一标识
     */
    private Integer id;

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 遥信名称，引用coll_yx_expend中的id
     */
    private Integer yxId;

    /**
     * 变位时间
     */
    private Long changeTime;

    /**
     * 0表示未处理，1表示已确认
     */
    private String status;

    /**
     * 操作时间
     */
    private Long operationTime;

    /**
     * 操作人
     */
    private Integer operator;

    /**
     * 0表示已清除，1表示有效
     */
    private String valid;

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

    public Integer getYxId() {
        return yxId;
    }

    public void setYxId(Integer yxId) {
        this.yxId = yxId;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Long operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
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
        DataYx other = (DataYx) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getYxId() == null ? other.getYxId() == null : this.getYxId().equals(other.getYxId()))
            && (this.getChangeTime() == null ? other.getChangeTime() == null : this.getChangeTime().equals(other.getChangeTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getOperationTime() == null ? other.getOperationTime() == null : this.getOperationTime().equals(other.getOperationTime()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getYxId() == null) ? 0 : getYxId().hashCode());
        result = prime * result + ((getChangeTime() == null) ? 0 : getChangeTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getOperationTime() == null) ? 0 : getOperationTime().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
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
        sb.append(", deviceId=").append(deviceId);
        sb.append(", yxId=").append(yxId);
        sb.append(", changeTime=").append(changeTime);
        sb.append(", status=").append(status);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", operator=").append(operator);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}