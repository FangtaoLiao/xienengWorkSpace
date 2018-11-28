package com.synpower.bean;

import java.io.Serializable;

/**
 * @author
 */
public class SysOrg implements Serializable {
	/**
	 * 用户唯一标识
	 */
	private Integer id;

	/**
	 * 公司名称
	 */
	private String orgName;

	/**
	 * 公司logo
	 */
	private String orgLogo;

	/**
	 * 公司描述
	 */
	private String orgDesc;

	/**
	 * 组织类型1经销商,2 部门或者区域
	 */
	private String orgType;

	/**
	 * 公司图片
	 */
	private String orgPhoto;

	/**
	 * 父节点
	 */
	private Integer fatherId;

	private Long lastModifyTime;

	private Integer lastModifyUser;

	/**
	 * 组织机构代码
	 */
	private String orgCode;

	/**
	 * 验证码
	 */
	private String orgValiCode;

	/**
	 * 机构代表人
	 */
	private String orgAgentorName;

	/**
	 * 机构代表人id
	 */
	private Integer orgAngetorId;

	/**
	 * 机构代表人电话
	 */
	private String orgAngetorTel;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 创建者id
	 */
	private Integer createUser;

	/**
	 * 启停用 1启用 0停用
	 */
	private String orgStatus;

	/**
	 * 删除 1删除 0未删除
	 */
	private String orgValid;

	/**
	 * 组织人员统计：监控、运营、售后、角色
	 */
	private String controlName;
	private String controlNum;
	private String businessName;
	private String businessNum;
	private String serviceName;
	private String serviceNum;
	private String roleName;
	private String roleNum;
	private String totalNum;

	/**
	 * 系统logo
	 */
	private String systemLogo;

	/**
	 * 系统名称
	 */
	private String systemName;

	/**
	 * 大屏logo
	 */
	private String screenLogo;

	/**
	 * 大屏名称
	 */
	private String screenName;

	/**
	 * 宣传图片
	 */
	private String propagandaPhoto;

	/**
	 * 能效logo
	 */
	private String systemLogoEe;

	public String getSystemLogoEe() {
		return systemLogoEe;
	}

	public void setSystemLogoEe(String systemLogoEe) {
		this.systemLogoEe = systemLogoEe;
	}

	public String getPropagandaPhoto() {
		return propagandaPhoto;
	}

	public void setPropagandaPhoto(String propagandaPhoto) {
		this.propagandaPhoto = propagandaPhoto;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getControlNum() {
		return controlNum;
	}

	public void setControlNum(String controlNum) {
		this.controlNum = controlNum;
	}

	public String getBusinessNum() {
		return businessNum;
	}

	public void setBusinessNum(String businessNum) {
		this.businessNum = businessNum;
	}

	public String getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(String serviceNum) {
		this.serviceNum = serviceNum;
	}

	public String getRoleNum() {
		return roleNum;
	}

	public void setRoleNum(String roleNum) {
		this.roleNum = roleNum;
	}

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo;
	}

	public String getOrgDesc() {
		return orgDesc;
	}

	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgPhoto() {
		return orgPhoto;
	}

	public void setOrgPhoto(String orgPhoto) {
		this.orgPhoto = orgPhoto;
	}

	public Integer getFatherId() {
		return fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgValiCode() {
		return orgValiCode;
	}

	public void setOrgValiCode(String orgValiCode) {
		this.orgValiCode = orgValiCode;
	}

	public String getOrgAgentorName() {
		return orgAgentorName;
	}

	public void setOrgAgentorName(String orgAgentorName) {
		this.orgAgentorName = orgAgentorName;
	}

	public Integer getOrgAngetorId() {
		return orgAngetorId;
	}

	public void setOrgAngetorId(Integer orgAngetorId) {
		this.orgAngetorId = orgAngetorId;
	}

	public String getOrgAngetorTel() {
		return orgAngetorTel;
	}

	public void setOrgAngetorTel(String orgAngetorTel) {
		this.orgAngetorTel = orgAngetorTel;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public String getOrgStatus() {
		return orgStatus;
	}

	public void setOrgStatus(String orgStatus) {
		this.orgStatus = orgStatus;
	}

	public String getOrgValid() {
		return orgValid;
	}

	public void setOrgValid(String orgValid) {
		this.orgValid = orgValid;
	}

	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSystemLogo() {
		return systemLogo;
	}

	public void setSystemLogo(String systemLogo) {
		this.systemLogo = systemLogo;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getScreenLogo() {
		return screenLogo;
	}

	public void setScreenLogo(String screenLogo) {
		this.screenLogo = screenLogo;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
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
		SysOrg other = (SysOrg) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getOrgName() == null ? other.getOrgName() == null
						: this.getOrgName().equals(other.getOrgName()))
				&& (this.getOrgLogo() == null ? other.getOrgLogo() == null
						: this.getOrgLogo().equals(other.getOrgLogo()))
				&& (this.getOrgDesc() == null ? other.getOrgDesc() == null
						: this.getOrgDesc().equals(other.getOrgDesc()))
				&& (this.getOrgType() == null ? other.getOrgType() == null
						: this.getOrgType().equals(other.getOrgType()))
				&& (this.getFatherId() == null ? other.getFatherId() == null
						: this.getFatherId().equals(other.getFatherId()))
				&& (this.getLastModifyTime() == null ? other.getLastModifyTime() == null
						: this.getLastModifyTime().equals(other.getLastModifyTime()))
				&& (this.getLastModifyUser() == null ? other.getLastModifyUser() == null
						: this.getLastModifyUser().equals(other.getLastModifyUser()))
				&& (this.getOrgCode() == null ? other.getOrgCode() == null
						: this.getOrgCode().equals(other.getOrgCode()))
				&& (this.getOrgValiCode() == null ? other.getOrgValiCode() == null
						: this.getOrgValiCode().equals(other.getOrgValiCode()))
				&& (this.getOrgAgentorName() == null ? other.getOrgAgentorName() == null
						: this.getOrgAgentorName().equals(other.getOrgAgentorName()))
				&& (this.getOrgAngetorId() == null ? other.getOrgAngetorId() == null
						: this.getOrgAngetorId().equals(other.getOrgAngetorId()))
				&& (this.getOrgAngetorTel() == null ? other.getOrgAngetorTel() == null
						: this.getOrgAngetorTel().equals(other.getOrgAngetorTel()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()))
				&& (this.getCreateUser() == null ? other.getCreateUser() == null
						: this.getCreateUser().equals(other.getCreateUser()))
				&& (this.getOrgStatus() == null ? other.getOrgStatus() == null
						: this.getOrgStatus().equals(other.getOrgStatus()))
				&& (this.getOrgValid() == null ? other.getOrgValid() == null
						: this.getOrgValid().equals(other.getOrgValid()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getOrgName() == null) ? 0 : getOrgName().hashCode());
		result = prime * result + ((getOrgLogo() == null) ? 0 : getOrgLogo().hashCode());
		result = prime * result + ((getOrgDesc() == null) ? 0 : getOrgDesc().hashCode());
		result = prime * result + ((getOrgType() == null) ? 0 : getOrgType().hashCode());
		result = prime * result + ((getFatherId() == null) ? 0 : getFatherId().hashCode());
		result = prime * result + ((getLastModifyTime() == null) ? 0 : getLastModifyTime().hashCode());
		result = prime * result + ((getLastModifyUser() == null) ? 0 : getLastModifyUser().hashCode());
		result = prime * result + ((getOrgCode() == null) ? 0 : getOrgCode().hashCode());
		result = prime * result + ((getOrgValiCode() == null) ? 0 : getOrgValiCode().hashCode());
		result = prime * result + ((getOrgAgentorName() == null) ? 0 : getOrgAgentorName().hashCode());
		result = prime * result + ((getOrgAngetorId() == null) ? 0 : getOrgAngetorId().hashCode());
		result = prime * result + ((getOrgAngetorTel() == null) ? 0 : getOrgAngetorTel().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
		result = prime * result + ((getOrgStatus() == null) ? 0 : getOrgStatus().hashCode());
		result = prime * result + ((getOrgValid() == null) ? 0 : getOrgValid().hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", orgName=").append(orgName);
		sb.append(", orgLogo=").append(orgLogo);
		sb.append(", orgDesc=").append(orgDesc);
		sb.append(", orgType=").append(orgType);
		sb.append(", fatherId=").append(fatherId);
		sb.append(", lastModifyTime=").append(lastModifyTime);
		sb.append(", lastModifyUser=").append(lastModifyUser);
		sb.append(", orgCode=").append(orgCode);
		sb.append(", orgValiCode=").append(orgValiCode);
		sb.append(", orgAgentorName=").append(orgAgentorName);
		sb.append(", orgAngetorId=").append(orgAngetorId);
		sb.append(", orgAngetorTel=").append(orgAngetorTel);
		sb.append(", createTime=").append(createTime);
		sb.append(", createUser=").append(createUser);
		sb.append(", orgStatus=").append(orgStatus);
		sb.append(", orgValid=").append(orgValid);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}
}