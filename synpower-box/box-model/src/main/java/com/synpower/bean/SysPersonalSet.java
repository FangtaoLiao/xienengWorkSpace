package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysPersonalSet implements Serializable {
    /**
     * 用户唯一标识
     */
    private Integer id;

    /**
     * 关注者ID（可能是用户ID 也可能是组织ID）
     */
    private Integer belongId;

    /**
     * 操作对象id
     */
    private String obejectId;

    /**
     * 个性化类型
     */
    private Integer personType;

    /**
     * 置顶时间
     */
    private Long createTime;

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

    public Integer getBelongId() {
        return belongId;
    }

    public void setBelongId(Integer belongId) {
        this.belongId = belongId;
    }

	 public void setObejectId(String obejectId) {
		this.obejectId = obejectId;
	}
	 public String getObejectId() {
		return obejectId;
	}
    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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
        SysPersonalSet other = (SysPersonalSet) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBelongId() == null ? other.getBelongId() == null : this.getBelongId().equals(other.getBelongId()))
            && (this.getObejectId() == null ? other.getObejectId() == null : this.getObejectId().equals(other.getObejectId()))
            && (this.getPersonType() == null ? other.getPersonType() == null : this.getPersonType().equals(other.getPersonType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBelongId() == null) ? 0 : getBelongId().hashCode());
        result = prime * result + ((getObejectId() == null) ? 0 : getObejectId().hashCode());
        result = prime * result + ((getPersonType() == null) ? 0 : getPersonType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
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
        sb.append(", belongId=").append(belongId);
        sb.append(", obejectId=").append(obejectId);
        sb.append(", personType=").append(personType);
        sb.append(", createTime=").append(createTime);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}