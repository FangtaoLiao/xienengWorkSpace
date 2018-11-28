package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollModelProtocolType implements Serializable {
    /**
     * 唯一识别码
     */
    private Byte id;

    /**
     * 协议名称
     */
    private String protocolName;

    /**
     * 1删除0未删除
     */
    private String protocolValid;

    /**
     * 1启用0禁用
     */
    private String protocolStatus;

    private static final long serialVersionUID = 1L;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolValid() {
        return protocolValid;
    }

    public void setProtocolValid(String protocolValid) {
        this.protocolValid = protocolValid;
    }

    public String getProtocolStatus() {
        return protocolStatus;
    }

    public void setProtocolStatus(String protocolStatus) {
        this.protocolStatus = protocolStatus;
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
        CollModelProtocolType other = (CollModelProtocolType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProtocolName() == null ? other.getProtocolName() == null : this.getProtocolName().equals(other.getProtocolName()))
            && (this.getProtocolValid() == null ? other.getProtocolValid() == null : this.getProtocolValid().equals(other.getProtocolValid()))
            && (this.getProtocolStatus() == null ? other.getProtocolStatus() == null : this.getProtocolStatus().equals(other.getProtocolStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProtocolName() == null) ? 0 : getProtocolName().hashCode());
        result = prime * result + ((getProtocolValid() == null) ? 0 : getProtocolValid().hashCode());
        result = prime * result + ((getProtocolStatus() == null) ? 0 : getProtocolStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", protocolName=").append(protocolName);
        sb.append(", protocolValid=").append(protocolValid);
        sb.append(", protocolStatus=").append(protocolStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}