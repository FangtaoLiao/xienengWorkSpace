package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DeviceType implements Serializable {
    /**
     * 设备类型唯一标志
     */
    private Integer id;

    /**
     * 设备类型
     */
    private String typeName;

    /**
     * 1删除0未删除
     */
    private String typeValid;

    /**
     * 1启用0禁用
     */
    private String typeStatus;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeValid() {
        return typeValid;
    }

    public void setTypeValid(String typeValid) {
        this.typeValid = typeValid;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
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
        DeviceType other = (DeviceType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTypeName() == null ? other.getTypeName() == null : this.getTypeName().equals(other.getTypeName()))
            && (this.getTypeValid() == null ? other.getTypeValid() == null : this.getTypeValid().equals(other.getTypeValid()))
            && (this.getTypeStatus() == null ? other.getTypeStatus() == null : this.getTypeStatus().equals(other.getTypeStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTypeName() == null) ? 0 : getTypeName().hashCode());
        result = prime * result + ((getTypeValid() == null) ? 0 : getTypeValid().hashCode());
        result = prime * result + ((getTypeStatus() == null) ? 0 : getTypeStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", typeName=").append(typeName);
        sb.append(", typeValid=").append(typeValid);
        sb.append(", typeStatus=").append(typeStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}