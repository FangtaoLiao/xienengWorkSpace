package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysConfig implements Serializable {
    /**
     * 唯一标识
     */
    private Integer id;

    /**
     * 域名
     */
    private String domainname;

    /**
     * 登录图片
     */
    private String loginPhoto;

    /**
     * 登录logo
     */
    private String loginLogo;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 公众号二维码
     */
    private String publicnumber;

    /**
     * 版权
     */
    private String copyright;

    /**
     * 备案号
     */
    private String recordnumber;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomainname() {
        return domainname;
    }

    public void setDomainname(String domainname) {
        this.domainname = domainname;
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getPublicnumber() {
        return publicnumber;
    }

    public void setPublicnumber(String publicnumber) {
        this.publicnumber = publicnumber;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(String recordnumber) {
        this.recordnumber = recordnumber;
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
        SysConfig other = (SysConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDomainname() == null ? other.getDomainname() == null : this.getDomainname().equals(other.getDomainname()))
            && (this.getLoginPhoto() == null ? other.getLoginPhoto() == null : this.getLoginPhoto().equals(other.getLoginPhoto()))
            && (this.getLoginLogo() == null ? other.getLoginLogo() == null : this.getLoginLogo().equals(other.getLoginLogo()))
            && (this.getSystemName() == null ? other.getSystemName() == null : this.getSystemName().equals(other.getSystemName()))
            && (this.getPublicnumber() == null ? other.getPublicnumber() == null : this.getPublicnumber().equals(other.getPublicnumber()))
            && (this.getCopyright() == null ? other.getCopyright() == null : this.getCopyright().equals(other.getCopyright()))
            && (this.getRecordnumber() == null ? other.getRecordnumber() == null : this.getRecordnumber().equals(other.getRecordnumber()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDomainname() == null) ? 0 : getDomainname().hashCode());
        result = prime * result + ((getLoginPhoto() == null) ? 0 : getLoginPhoto().hashCode());
        result = prime * result + ((getLoginLogo() == null) ? 0 : getLoginLogo().hashCode());
        result = prime * result + ((getSystemName() == null) ? 0 : getSystemName().hashCode());
        result = prime * result + ((getPublicnumber() == null) ? 0 : getPublicnumber().hashCode());
        result = prime * result + ((getCopyright() == null) ? 0 : getCopyright().hashCode());
        result = prime * result + ((getRecordnumber() == null) ? 0 : getRecordnumber().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", domainname=").append(domainname);
        sb.append(", loginPhoto=").append(loginPhoto);
        sb.append(", loginLogo=").append(loginLogo);
        sb.append(", systemName=").append(systemName);
        sb.append(", publicnumber=").append(publicnumber);
        sb.append(", copyright=").append(copyright);
        sb.append(", recordnumber=").append(recordnumber);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}