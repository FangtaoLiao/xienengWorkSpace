package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollModel implements Serializable {
    /**
     * 点表唯一标识
     */
    private Integer id;

    /**
     * 点表名称
     */
    private String modeName;
    
    private String tableName;

    /**
     * 适用于哪些厂商、型号，文本输入
     */
    private String applyTo;

    /**
     * 协议类型
     */
    private Byte protocolType;

    /**
     * 最后更改人
     */
    private Integer lastModifyUser;

    /**
     * 最后更改时间
     */
    private Long lastModifyTime;

    /**
     * 是否有效
     */
	private String valid;
	private Integer deviceModelId;
	private Integer deviceType;

    private static final long serialVersionUID = 1L;

    
    
    /**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the deviceType
	 */
	public Integer getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the deviceModelId
	 */
	public Integer getDeviceModelId() {
		return deviceModelId;
	}

	/**
	 * @param deviceModelId the deviceModelId to set
	 */
	public void setDeviceModelId(Integer deviceModelId) {
		this.deviceModelId = deviceModelId;
	}
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(String applyTo) {
        this.applyTo = applyTo;
    }

    public Byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(Byte protocolType) {
        this.protocolType = protocolType;
    }

    public Integer getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(Integer lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
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
        CollModel other = (CollModel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getModeName() == null ? other.getModeName() == null : this.getModeName().equals(other.getModeName()))
            && (this.getApplyTo() == null ? other.getApplyTo() == null : this.getApplyTo().equals(other.getApplyTo()))
            && (this.getProtocolType() == null ? other.getProtocolType() == null : this.getProtocolType().equals(other.getProtocolType()))
            && (this.getLastModifyUser() == null ? other.getLastModifyUser() == null : this.getLastModifyUser().equals(other.getLastModifyUser()))
            && (this.getLastModifyTime() == null ? other.getLastModifyTime() == null : this.getLastModifyTime().equals(other.getLastModifyTime()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModeName() == null) ? 0 : getModeName().hashCode());
        result = prime * result + ((getApplyTo() == null) ? 0 : getApplyTo().hashCode());
        result = prime * result + ((getProtocolType() == null) ? 0 : getProtocolType().hashCode());
        result = prime * result + ((getLastModifyUser() == null) ? 0 : getLastModifyUser().hashCode());
        result = prime * result + ((getLastModifyTime() == null) ? 0 : getLastModifyTime().hashCode());
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
        sb.append(", modeName=").append(modeName);
        sb.append(", applyTo=").append(applyTo);
        sb.append(", protocolType=").append(protocolType);
        sb.append(", lastModifyUser=").append(lastModifyUser);
        sb.append(", lastModifyTime=").append(lastModifyTime);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    
}