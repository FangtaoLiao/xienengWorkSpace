package com.synpower.bean;

import java.io.Serializable;
import java.util.List;

/*****************************************************************************
 * @Package: com.synpower.bean
 * ClassName: SysUser
 * @Description: 用户列表
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年10月20日上午10:27:57   SP0009             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/

/*****************************************************************************
 * @Package: com.synpower.bean
 * ClassName: SysUser
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年2月11日上午11:22:06   SP0007             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
/*****************************************************************************
 * @Package: com.synpower.bean
 * ClassName: SysUser
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年2月11日上午11:22:11   SP0007             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
public class SysUser implements Serializable {
    /**
     * 用户唯一标识
     */
    private Integer id;

    /**
     * 登陆账号
     */
    private String loginId;

    /**
     * 密码
     */
    private String password;

    /**
     * 工号
     */
    private String workCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 头像
     */
    private String iconUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机号码
     */
    private String userTel;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 岗位
     */
    private Integer userPost;

    /**
     * 组织
     */
    private Integer orgId;

    /**
     * 拼音搜索
     */
    private String pinyinSearch;

    /**
     * 最后更改时间
     */
    private Long lastModifyTime;

    /**
     * 最后更改人
     */
    private Integer lastModifyUser;

    /**
     * 最后登陆时间
     */
    private Long lastLoginTime;

    /**
     * 创建时间
     */
    private Long createTime;

    private String loginIp;

    /**
     * 删除 1删除 0未删除
     */
    private String userValid;

    /**
     * 启停用 1启用 0停用
     */
    private String userStatus;

    /**
     * 用户类型，0企业用户，1个人用户
     */
    private String userType;
    
    /**
     * 角色
     */
    private SysRole userRole;
    /**
     * 权限
     */
    private List<SysRights> rightList ;
    /**
     * 机构
     */
    private SysOrg userOrg;
    /**
     * 1账号密码登录 0 扫码登录
     */
    private String requestType;
    private String userWeixin;
    private String defaultType;
    private String useTel;
    private String weixinUUID;
    
    public void setWeixinUUID(String weixinUUID) {
		this.weixinUUID = weixinUUID;
	}
    public String getWeixinUUID() {
		return weixinUUID;
	}
    public String getUseTel() {
		return useTel;
	}
	public void setUseTel(String useTel) {
		this.useTel = useTel;
	}
	public void setUserWeixin(String userWeixin) {
		this.userWeixin = userWeixin;
	}
    public String getUserWeixin() {
		return userWeixin;
	}
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserPost() {
        return userPost;
    }

    public void setUserPost(Integer userPost) {
        this.userPost = userPost;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getPinyinSearch() {
        return pinyinSearch;
    }

    public void setPinyinSearch(String pinyinSearch) {
        this.pinyinSearch = pinyinSearch;
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

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUserValid() {
        return userValid;
    }

    public void setUserValid(String userValid) {
        this.userValid = userValid;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public SysRole getUserRole() {
		return userRole;
	}

	public void setUserRole(SysRole userRole) {
		this.userRole = userRole;
	}

	public List<SysRights> getRightList() {
		return rightList;
	}

	public void setRightList(List<SysRights> rightList) {
		this.rightList = rightList;
	}

	public SysOrg getUserOrg() {
		return userOrg;
	}

	public void setUserOrg(SysOrg userOrg) {
		this.userOrg = userOrg;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getDefaultType() {
		return defaultType;
	}
	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((iconUrl == null) ? 0 : iconUrl.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastLoginTime == null) ? 0 : lastLoginTime.hashCode());
		result = prime * result + ((lastModifyTime == null) ? 0 : lastModifyTime.hashCode());
		result = prime * result + ((lastModifyUser == null) ? 0 : lastModifyUser.hashCode());
		result = prime * result + ((loginId == null) ? 0 : loginId.hashCode());
		result = prime * result + ((loginIp == null) ? 0 : loginIp.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((pinyinSearch == null) ? 0 : pinyinSearch.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((rightList == null) ? 0 : rightList.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userOrg == null) ? 0 : userOrg.hashCode());
		result = prime * result + ((userPost == null) ? 0 : userPost.hashCode());
		result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
		result = prime * result + ((userStatus == null) ? 0 : userStatus.hashCode());
		result = prime * result + ((userTel == null) ? 0 : userTel.hashCode());
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
		result = prime * result + ((userValid == null) ? 0 : userValid.hashCode());
		result = prime * result + ((workCode == null) ? 0 : workCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysUser other = (SysUser) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (iconUrl == null) {
			if (other.iconUrl != null)
				return false;
		} else if (!iconUrl.equals(other.iconUrl))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastLoginTime == null) {
			if (other.lastLoginTime != null)
				return false;
		} else if (!lastLoginTime.equals(other.lastLoginTime))
			return false;
		if (lastModifyTime == null) {
			if (other.lastModifyTime != null)
				return false;
		} else if (!lastModifyTime.equals(other.lastModifyTime))
			return false;
		if (lastModifyUser == null) {
			if (other.lastModifyUser != null)
				return false;
		} else if (!lastModifyUser.equals(other.lastModifyUser))
			return false;
		if (loginId == null) {
			if (other.loginId != null)
				return false;
		} else if (!loginId.equals(other.loginId))
			return false;
		if (loginIp == null) {
			if (other.loginIp != null)
				return false;
		} else if (!loginIp.equals(other.loginIp))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (pinyinSearch == null) {
			if (other.pinyinSearch != null)
				return false;
		} else if (!pinyinSearch.equals(other.pinyinSearch))
			return false;
		if (requestType == null) {
			if (other.requestType != null)
				return false;
		} else if (!requestType.equals(other.requestType))
			return false;
		if (rightList == null) {
			if (other.rightList != null)
				return false;
		} else if (!rightList.equals(other.rightList))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userOrg == null) {
			if (other.userOrg != null)
				return false;
		} else if (!userOrg.equals(other.userOrg))
			return false;
		if (userPost == null) {
			if (other.userPost != null)
				return false;
		} else if (!userPost.equals(other.userPost))
			return false;
		if (userRole == null) {
			if (other.userRole != null)
				return false;
		} else if (!userRole.equals(other.userRole))
			return false;
		if (userStatus == null) {
			if (other.userStatus != null)
				return false;
		} else if (!userStatus.equals(other.userStatus))
			return false;
		if (userTel == null) {
			if (other.userTel != null)
				return false;
		} else if (!userTel.equals(other.userTel))
			return false;
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!userType.equals(other.userType))
			return false;
		if (userValid == null) {
			if (other.userValid != null)
				return false;
		} else if (!userValid.equals(other.userValid))
			return false;
		if (workCode == null) {
			if (other.workCode != null)
				return false;
		} else if (!workCode.equals(other.workCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", loginId=" + loginId + ", password=" + password + ", workCode=" + workCode
				+ ", userName=" + userName + ", iconUrl=" + iconUrl + ", gender=" + gender + ", userTel=" + userTel
				+ ", email=" + email + ", userPost=" + userPost + ", orgId=" + orgId + ", pinyinSearch=" + pinyinSearch
				+ ", lastModifyTime=" + lastModifyTime + ", lastModifyUser=" + lastModifyUser + ", lastLoginTime="
				+ lastLoginTime + ", createTime=" + createTime + ", loginIp=" + loginIp + ", userValid=" + userValid
				+ ", userStatus=" + userStatus + ", userType=" + userType + ", userRole=" + userRole + ", rightList="
				+ rightList + ", userOrg=" + userOrg + ", requestType=" + requestType + "]";
	}

	
}