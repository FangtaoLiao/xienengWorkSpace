package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysPersonalType implements Serializable {
    /**
     * 用户唯一标识
     */
    private Integer id;

    /**
     * 操作对象id
     */
    private String personName;

    /**
     * 个性化类型
     */
    private Integer personType;

    /**
     * 删除状态 1 删除0未删除
     */
    private String valid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
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
        SysPersonalType other = (SysPersonalType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPersonName() == null ? other.getPersonName() == null : this.getPersonName().equals(other.getPersonName()))
            && (this.getPersonType() == null ? other.getPersonType() == null : this.getPersonType().equals(other.getPersonType()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPersonName() == null) ? 0 : getPersonName().hashCode());
        result = prime * result + ((getPersonType() == null) ? 0 : getPersonType().hashCode());
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
        sb.append(", personName=").append(personName);
        sb.append(", personType=").append(personType);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}