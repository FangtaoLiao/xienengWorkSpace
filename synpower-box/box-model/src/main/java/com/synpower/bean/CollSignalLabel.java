package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollSignalLabel implements Serializable {
    /**
     * key
     */
    private Integer id;

    /**
     * 表名
     */
    private String tablename;

    /**
     * 字段名
     */
    private String field;

    /**
     * 语言(sys_language)
     */
    private Byte languageId;

    /**
     * 显示名
     */
    private String fieldLabel;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 显示名优先级
     */
    private Integer priority;

    private String unit;

    /**
     * 1比较 0不比较
     */
    private String compare;

    /**
     * 是否有效
     */
    private String valid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Byte getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Byte languageId) {
        this.languageId = languageId;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
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
        CollSignalLabel other = (CollSignalLabel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTablename() == null ? other.getTablename() == null : this.getTablename().equals(other.getTablename()))
            && (this.getField() == null ? other.getField() == null : this.getField().equals(other.getField()))
            && (this.getLanguageId() == null ? other.getLanguageId() == null : this.getLanguageId().equals(other.getLanguageId()))
            && (this.getFieldLabel() == null ? other.getFieldLabel() == null : this.getFieldLabel().equals(other.getFieldLabel()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()))
            && (this.getPriority() == null ? other.getPriority() == null : this.getPriority().equals(other.getPriority()))
            && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
            && (this.getCompare() == null ? other.getCompare() == null : this.getCompare().equals(other.getCompare()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTablename() == null) ? 0 : getTablename().hashCode());
        result = prime * result + ((getField() == null) ? 0 : getField().hashCode());
        result = prime * result + ((getLanguageId() == null) ? 0 : getLanguageId().hashCode());
        result = prime * result + ((getFieldLabel() == null) ? 0 : getFieldLabel().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
        result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
        result = prime * result + ((getCompare() == null) ? 0 : getCompare().hashCode());
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
        sb.append(", tablename=").append(tablename);
        sb.append(", field=").append(field);
        sb.append(", languageId=").append(languageId);
        sb.append(", fieldLabel=").append(fieldLabel);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", priority=").append(priority);
        sb.append(", unit=").append(unit);
        sb.append(", compare=").append(compare);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}