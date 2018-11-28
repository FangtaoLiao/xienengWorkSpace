package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysSharedLinks implements Serializable {
    private Integer id;

    /**
     * 分享的链接编码
     */
    private String sharedLinkCode;

    /**
     * 组织id
     */
    private Integer orgId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 电站id集合
     */
    private String plantsId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 使用的用户
     */
    private String usedUser;

    /**
     * 使用的时间
     */
    private Long usedTime;

    /**
     * 0已失效1未使用2 已使用
     */
    private String status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSharedLinkCode() {
        return sharedLinkCode;
    }

    public void setSharedLinkCode(String sharedLinkCode) {
        this.sharedLinkCode = sharedLinkCode;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPlantsId() {
        return plantsId;
    }

    public void setPlantsId(String plantsId) {
        this.plantsId = plantsId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUsedUser() {
        return usedUser;
    }

    public void setUsedUser(String usedUser) {
        this.usedUser = usedUser;
    }

    public Long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Long usedTime) {
        this.usedTime = usedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        SysSharedLinks other = (SysSharedLinks) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSharedLinkCode() == null ? other.getSharedLinkCode() == null : this.getSharedLinkCode().equals(other.getSharedLinkCode()))
            && (this.getOrgId() == null ? other.getOrgId() == null : this.getOrgId().equals(other.getOrgId()))
            && (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getPlantsId() == null ? other.getPlantsId() == null : this.getPlantsId().equals(other.getPlantsId()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUsedUser() == null ? other.getUsedUser() == null : this.getUsedUser().equals(other.getUsedUser()))
            && (this.getUsedTime() == null ? other.getUsedTime() == null : this.getUsedTime().equals(other.getUsedTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSharedLinkCode() == null) ? 0 : getSharedLinkCode().hashCode());
        result = prime * result + ((getOrgId() == null) ? 0 : getOrgId().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getPlantsId() == null) ? 0 : getPlantsId().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUsedUser() == null) ? 0 : getUsedUser().hashCode());
        result = prime * result + ((getUsedTime() == null) ? 0 : getUsedTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sharedLinkCode=").append(sharedLinkCode);
        sb.append(", orgId=").append(orgId);
        sb.append(", roleId=").append(roleId);
        sb.append(", plantsId=").append(plantsId);
        sb.append(", userType=").append(userType);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", usedUser=").append(usedUser);
        sb.append(", usedTime=").append(usedTime);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}