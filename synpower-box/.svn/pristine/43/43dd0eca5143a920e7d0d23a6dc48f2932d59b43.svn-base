package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysRights implements Serializable {
    /**
     * 用户唯一标识
     */
    private Integer id;

    private String rightsName;

    /**
     * 权限标识
     */
    private String rightsMark;

    private Integer fatherId;

    private Integer lastModifyTime;

    private Integer lastModifyUser;

    private Long createTime;

    /**
     * 删除状态 1删除 0未删除
     */
    private String rightsValid;

    /**
     * 启停用 1启用 0停用
     */
    private String rightsStatus;

    /**
     * 权限描述
     */
    private String rightsDesc;
    private String editLook;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRightsName() {
        return rightsName;
    }

    public void setRightsName(String rightsName) {
        this.rightsName = rightsName;
    }

    public String getEditLook() {
		return editLook;
	}

	public void setEditLook(String editLook) {
		this.editLook = editLook;
	}

	public String getRightsMark() {
        return rightsMark;
    }

    public void setRightsMark(String rightsMark) {
        this.rightsMark = rightsMark;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Integer lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(Integer lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRightsValid() {
        return rightsValid;
    }

    public void setRightsValid(String rightsValid) {
        this.rightsValid = rightsValid;
    }

    public String getRightsStatus() {
        return rightsStatus;
    }

    public void setRightsStatus(String rightsStatus) {
        this.rightsStatus = rightsStatus;
    }

    public String getRightsDesc() {
        return rightsDesc;
    }

    public void setRightsDesc(String rightsDesc) {
        this.rightsDesc = rightsDesc;
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
        SysRights other = (SysRights) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRightsName() == null ? other.getRightsName() == null : this.getRightsName().equals(other.getRightsName()))
            && (this.getRightsMark() == null ? other.getRightsMark() == null : this.getRightsMark().equals(other.getRightsMark()))
            && (this.getFatherId() == null ? other.getFatherId() == null : this.getFatherId().equals(other.getFatherId()))
            && (this.getLastModifyTime() == null ? other.getLastModifyTime() == null : this.getLastModifyTime().equals(other.getLastModifyTime()))
            && (this.getLastModifyUser() == null ? other.getLastModifyUser() == null : this.getLastModifyUser().equals(other.getLastModifyUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRightsValid() == null ? other.getRightsValid() == null : this.getRightsValid().equals(other.getRightsValid()))
            && (this.getRightsStatus() == null ? other.getRightsStatus() == null : this.getRightsStatus().equals(other.getRightsStatus()))
            && (this.getRightsDesc() == null ? other.getRightsDesc() == null : this.getRightsDesc().equals(other.getRightsDesc()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRightsName() == null) ? 0 : getRightsName().hashCode());
        result = prime * result + ((getRightsMark() == null) ? 0 : getRightsMark().hashCode());
        result = prime * result + ((getFatherId() == null) ? 0 : getFatherId().hashCode());
        result = prime * result + ((getLastModifyTime() == null) ? 0 : getLastModifyTime().hashCode());
        result = prime * result + ((getLastModifyUser() == null) ? 0 : getLastModifyUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRightsValid() == null) ? 0 : getRightsValid().hashCode());
        result = prime * result + ((getRightsStatus() == null) ? 0 : getRightsStatus().hashCode());
        result = prime * result + ((getRightsDesc() == null) ? 0 : getRightsDesc().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", rightsName=").append(rightsName);
        sb.append(", rightsMark=").append(rightsMark);
        sb.append(", fatherId=").append(fatherId);
        sb.append(", lastModifyTime=").append(lastModifyTime);
        sb.append(", lastModifyUser=").append(lastModifyUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", rightsValid=").append(rightsValid);
        sb.append(", rightsStatus=").append(rightsStatus);
        sb.append(", rightsDesc=").append(rightsDesc);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}