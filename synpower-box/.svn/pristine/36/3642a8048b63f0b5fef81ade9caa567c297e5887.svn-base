package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class SysSerialPort implements Serializable {
    /**
     * 串口表的唯一标识
     */
    private Integer id;

    /**
     * 串口名称
     */
    private String comName;

    /**
     * 最大连接数
     */
    private Integer maxNum;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
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
        SysSerialPort other = (SysSerialPort) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getComName() == null ? other.getComName() == null : this.getComName().equals(other.getComName()))
            && (this.getMaxNum() == null ? other.getMaxNum() == null : this.getMaxNum().equals(other.getMaxNum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getComName() == null) ? 0 : getComName().hashCode());
        result = prime * result + ((getMaxNum() == null) ? 0 : getMaxNum().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", comName=").append(comName);
        sb.append(", maxNum=").append(maxNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}