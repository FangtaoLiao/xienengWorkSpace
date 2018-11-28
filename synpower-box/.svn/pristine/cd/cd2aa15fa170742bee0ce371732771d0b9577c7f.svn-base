package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysRole implements Serializable {
    /**
     * 用户唯一标识
     */
    private Integer id;

    private String roleName;

    private Long lastModifyTime;

    private Integer lastModifyUser;

    /**
     * 机构id
     */
    private Integer orgId;

    /**
     * 是否是super超级管理员 1是 0不是
     */
    private String supStatus;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 删除状态 1删除 0未删除
     */
    private String roleValid;

    /**
     * 启停用 1启用 0停用
     */
    private String roleStatus;

    private String fatherId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(Integer lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getSupStatus() {
        return supStatus;
    }

    public void setSupStatus(String supStatus) {
        this.supStatus = supStatus;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRoleValid() {
        return roleValid;
    }

    public void setRoleValid(String roleValid) {
        this.roleValid = roleValid;
    }

    public String getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(String roleStatus) {
        this.roleStatus = roleStatus;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
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
        SysRole other = (SysRole) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRoleName() == null ? other.getRoleName() == null : this.getRoleName().equals(other.getRoleName()))
            && (this.getLastModifyTime() == null ? other.getLastModifyTime() == null : this.getLastModifyTime().equals(other.getLastModifyTime()))
            && (this.getLastModifyUser() == null ? other.getLastModifyUser() == null : this.getLastModifyUser().equals(other.getLastModifyUser()))
            && (this.getOrgId() == null ? other.getOrgId() == null : this.getOrgId().equals(other.getOrgId()))
            && (this.getSupStatus() == null ? other.getSupStatus() == null : this.getSupStatus().equals(other.getSupStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRoleValid() == null ? other.getRoleValid() == null : this.getRoleValid().equals(other.getRoleValid()))
            && (this.getRoleStatus() == null ? other.getRoleStatus() == null : this.getRoleStatus().equals(other.getRoleStatus()))
            && (this.getFatherId() == null ? other.getFatherId() == null : this.getFatherId().equals(other.getFatherId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRoleName() == null) ? 0 : getRoleName().hashCode());
        result = prime * result + ((getLastModifyTime() == null) ? 0 : getLastModifyTime().hashCode());
        result = prime * result + ((getLastModifyUser() == null) ? 0 : getLastModifyUser().hashCode());
        result = prime * result + ((getOrgId() == null) ? 0 : getOrgId().hashCode());
        result = prime * result + ((getSupStatus() == null) ? 0 : getSupStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRoleValid() == null) ? 0 : getRoleValid().hashCode());
        result = prime * result + ((getRoleStatus() == null) ? 0 : getRoleStatus().hashCode());
        result = prime * result + ((getFatherId() == null) ? 0 : getFatherId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleName=").append(roleName);
        sb.append(", lastModifyTime=").append(lastModifyTime);
        sb.append(", lastModifyUser=").append(lastModifyUser);
        sb.append(", orgId=").append(orgId);
        sb.append(", supStatus=").append(supStatus);
        sb.append(", createTime=").append(createTime);
        sb.append(", roleValid=").append(roleValid);
        sb.append(", roleStatus=").append(roleStatus);
        sb.append(", fatherId=").append(fatherId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}