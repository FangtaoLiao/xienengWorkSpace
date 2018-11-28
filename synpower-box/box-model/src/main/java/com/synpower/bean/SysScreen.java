package com.synpower.bean;


public class SysScreen {
	private Integer id;

    /**
     * 区域级别
     */
    private Integer level;

    /**
     * 组织id
     */
    private Integer orgId;

    /**
     * 人为输入的电站规模参数
     */
    private Float plantArtificialCapacity;
   
    /**
     * 单位
     */
    private String capacityUnit;

    /**
     * 是否开启系统自动计算（0为不开启，1为开启）
     */
    private String plantAutomaticCalculation;

    /**
     * 系统logo
     */
    private String systemLogo;

    /**
     * 系统显示名称
     */
    private String systemName;

    /**
     * 大屏logo
     */
    private String screenLogo;

    /**
     * 登录页图片
     */
    private String loginPhoto;

    /**
     * 登录logo
     */
    private String loginLogo;

    /**
     * 登录二维码
     */
    private String qrCode;
    /**
     * 大屏名称
     */
    private String screenName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Float getPlantArtificialCapacity() {
        return plantArtificialCapacity;
    }

    public void setPlantArtificialCapacity(Float plantArtificialCapacity) {
        this.plantArtificialCapacity = plantArtificialCapacity;
    }

    public String getCapacityUnit() {
		return capacityUnit;
	}

	public void setCapacityUnit(String capacityUnit) {
		this.capacityUnit = capacityUnit;
	}

	public String getPlantAutomaticCalculation() {
        return plantAutomaticCalculation;
    }

    public void setPlantAutomaticCalculation(String plantAutomaticCalculation) {
        this.plantAutomaticCalculation = plantAutomaticCalculation;
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

    public String getLoginPhoto() {
        return loginPhoto;
    }

    public void setLoginPhoto(String loginPhoto) {
        this.loginPhoto = loginPhoto;
    }

    public String getLoginLogo() {
        return loginLogo;
    }

    public void setLoginLogo(String loginLogo) {
        this.loginLogo = loginLogo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
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
        SysScreen other = (SysScreen) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getOrgId() == null ? other.getOrgId() == null : this.getOrgId().equals(other.getOrgId()))
            && (this.getPlantArtificialCapacity() == null ? other.getPlantArtificialCapacity() == null : this.getPlantArtificialCapacity().equals(other.getPlantArtificialCapacity()))
            && (this.getPlantAutomaticCalculation() == null ? other.getPlantAutomaticCalculation() == null : this.getPlantAutomaticCalculation().equals(other.getPlantAutomaticCalculation()))
            && (this.getSystemLogo() == null ? other.getSystemLogo() == null : this.getSystemLogo().equals(other.getSystemLogo()))
            && (this.getSystemName() == null ? other.getSystemName() == null : this.getSystemName().equals(other.getSystemName()))
            && (this.getScreenLogo() == null ? other.getScreenLogo() == null : this.getScreenLogo().equals(other.getScreenLogo()))
            && (this.getLoginPhoto() == null ? other.getLoginPhoto() == null : this.getLoginPhoto().equals(other.getLoginPhoto()))
            && (this.getLoginLogo() == null ? other.getLoginLogo() == null : this.getLoginLogo().equals(other.getLoginLogo()))
            && (this.getQrCode() == null ? other.getQrCode() == null : this.getQrCode().equals(other.getQrCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getOrgId() == null) ? 0 : getOrgId().hashCode());
        result = prime * result + ((getPlantArtificialCapacity() == null) ? 0 : getPlantArtificialCapacity().hashCode());
        result = prime * result + ((getPlantAutomaticCalculation() == null) ? 0 : getPlantAutomaticCalculation().hashCode());
        result = prime * result + ((getSystemLogo() == null) ? 0 : getSystemLogo().hashCode());
        result = prime * result + ((getSystemName() == null) ? 0 : getSystemName().hashCode());
        result = prime * result + ((getScreenLogo() == null) ? 0 : getScreenLogo().hashCode());
        result = prime * result + ((getLoginPhoto() == null) ? 0 : getLoginPhoto().hashCode());
        result = prime * result + ((getLoginLogo() == null) ? 0 : getLoginLogo().hashCode());
        result = prime * result + ((getQrCode() == null) ? 0 : getQrCode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", level=").append(level);
        sb.append(", orgId=").append(orgId);
        sb.append(", plantArtificialCapacity=").append(plantArtificialCapacity);
        sb.append(", plantAutomaticCalculation=").append(plantAutomaticCalculation);
        sb.append(", systemLogo=").append(systemLogo);
        sb.append(", systemName=").append(systemName);
        sb.append(", screenLogo=").append(screenLogo);
        sb.append(", loginPhoto=").append(loginPhoto);
        sb.append(", loginLogo=").append(loginLogo);
        sb.append(", qrCode=").append(qrCode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
