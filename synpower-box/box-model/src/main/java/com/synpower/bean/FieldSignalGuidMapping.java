package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class FieldSignalGuidMapping implements Serializable {
    private Integer id;

    /**
     * key
     */
    private Integer collId;

    /**
     * 点表信号唯一id
     */
    private String signalGuid;

    private String valid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCollId() {
        return collId;
    }

    public void setCollId(Integer collId) {
        this.collId = collId;
    }

    public String getSignalGuid() {
        return signalGuid;
    }

    public void setSignalGuid(String signalGuid) {
        this.signalGuid = signalGuid;
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
        FieldSignalGuidMapping other = (FieldSignalGuidMapping) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCollId() == null ? other.getCollId() == null : this.getCollId().equals(other.getCollId()))
            && (this.getSignalGuid() == null ? other.getSignalGuid() == null : this.getSignalGuid().equals(other.getSignalGuid()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCollId() == null) ? 0 : getCollId().hashCode());
        result = prime * result + ((getSignalGuid() == null) ? 0 : getSignalGuid().hashCode());
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
        sb.append(", signalGuid=").append(signalGuid);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}