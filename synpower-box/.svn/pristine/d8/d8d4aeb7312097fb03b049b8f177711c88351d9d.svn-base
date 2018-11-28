package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollYkytExpand implements Serializable {
    /**
     * 信号点的唯一标识
     */
    private Integer id;

    /**
     * 对应coll_model
     */
    private Integer dataModel;

    /**
     * 信号点唯一标识
     */
    private String signalGuid;

    /**
     * 名称
     */
    private String signalName;

    /**
     * 1表示遥测控制，0表示不是
     */
    private Integer ycControl;

    /**
     * 控制bit位
     */
    private Integer controlBit;

    /**
     * 状态值
     */
    private Integer statusValue;

    /**
     * 是否有效
     */
    private String valid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataModel() {
        return dataModel;
    }

    public void setDataModel(Integer dataModel) {
        this.dataModel = dataModel;
    }

    public String getSignalGuid() {
        return signalGuid;
    }

    public void setSignalGuid(String signalGuid) {
        this.signalGuid = signalGuid;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Integer getYcControl() {
        return ycControl;
    }

    public void setYcControl(Integer ycControl) {
        this.ycControl = ycControl;
    }

    public Integer getControlBit() {
        return controlBit;
    }

    public void setControlBit(Integer controlBit) {
        this.controlBit = controlBit;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
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
        CollYkytExpand other = (CollYkytExpand) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDataModel() == null ? other.getDataModel() == null : this.getDataModel().equals(other.getDataModel()))
            && (this.getSignalGuid() == null ? other.getSignalGuid() == null : this.getSignalGuid().equals(other.getSignalGuid()))
            && (this.getSignalName() == null ? other.getSignalName() == null : this.getSignalName().equals(other.getSignalName()))
            && (this.getYcControl() == null ? other.getYcControl() == null : this.getYcControl().equals(other.getYcControl()))
            && (this.getControlBit() == null ? other.getControlBit() == null : this.getControlBit().equals(other.getControlBit()))
            && (this.getStatusValue() == null ? other.getStatusValue() == null : this.getStatusValue().equals(other.getStatusValue()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDataModel() == null) ? 0 : getDataModel().hashCode());
        result = prime * result + ((getSignalGuid() == null) ? 0 : getSignalGuid().hashCode());
        result = prime * result + ((getSignalName() == null) ? 0 : getSignalName().hashCode());
        result = prime * result + ((getYcControl() == null) ? 0 : getYcControl().hashCode());
        result = prime * result + ((getControlBit() == null) ? 0 : getControlBit().hashCode());
        result = prime * result + ((getStatusValue() == null) ? 0 : getStatusValue().hashCode());
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
        sb.append(", dataModel=").append(dataModel);
        sb.append(", signalGuid=").append(signalGuid);
        sb.append(", signalName=").append(signalName);
        sb.append(", ycControl=").append(ycControl);
        sb.append(", controlBit=").append(controlBit);
        sb.append(", statusValue=").append(statusValue);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}