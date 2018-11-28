package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollJsonDevSet implements Serializable {
    private Long id;

    /**
     * 数据采集器设备id
     */
    private Integer collId;

    /**
     * com口id
     */
    private String comId;

    /**
     * 设备id
     */
    private Integer devId;

    /**
     * 从设备地址
     */
    private String slaveId;

    /**
     * 校验码模式，支持crc和lrc
     */
    private String verifyMode;

    /**
     * 校验码字节序，LH低前高后，HL：高前低后
     */
    private String verifyOrder;

    /**
     * 0表示无效，1表示有效
     */
    private String valid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCollId() {
        return collId;
    }

    public void setCollId(Integer collId) {
        this.collId = collId;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }

    public String getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(String slaveId) {
        this.slaveId = slaveId;
    }

    public String getVerifyMode() {
        return verifyMode;
    }

    public void setVerifyMode(String verifyMode) {
        this.verifyMode = verifyMode;
    }

    public String getVerifyOrder() {
        return verifyOrder;
    }

    public void setVerifyOrder(String verifyOrder) {
        this.verifyOrder = verifyOrder;
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
        CollJsonDevSet other = (CollJsonDevSet) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCollId() == null ? other.getCollId() == null : this.getCollId().equals(other.getCollId()))
            && (this.getComId() == null ? other.getComId() == null : this.getComId().equals(other.getComId()))
            && (this.getDevId() == null ? other.getDevId() == null : this.getDevId().equals(other.getDevId()))
            && (this.getSlaveId() == null ? other.getSlaveId() == null : this.getSlaveId().equals(other.getSlaveId()))
            && (this.getVerifyMode() == null ? other.getVerifyMode() == null : this.getVerifyMode().equals(other.getVerifyMode()))
            && (this.getVerifyOrder() == null ? other.getVerifyOrder() == null : this.getVerifyOrder().equals(other.getVerifyOrder()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCollId() == null) ? 0 : getCollId().hashCode());
        result = prime * result + ((getComId() == null) ? 0 : getComId().hashCode());
        result = prime * result + ((getDevId() == null) ? 0 : getDevId().hashCode());
        result = prime * result + ((getSlaveId() == null) ? 0 : getSlaveId().hashCode());
        result = prime * result + ((getVerifyMode() == null) ? 0 : getVerifyMode().hashCode());
        result = prime * result + ((getVerifyOrder() == null) ? 0 : getVerifyOrder().hashCode());
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
        sb.append(", collId=").append(collId);
        sb.append(", comId=").append(comId);
        sb.append(", devId=").append(devId);
        sb.append(", slaveId=").append(slaveId);
        sb.append(", verifyMode=").append(verifyMode);
        sb.append(", verifyOrder=").append(verifyOrder);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}