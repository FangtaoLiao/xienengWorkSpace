package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class CollectorMessageStructure implements Serializable {
    /**
     * 信号点的唯一标识
     */
    private Integer id;

    /**
     * 数据采集器设备ID
     */
    private Integer collectorId;

    /**
     * 数采唯一标识
     */
    private String collectorSn;

    /**
     * 对应coll_model
     */
    private Integer dataModel;

    /**
     * 关联的逆变器ID
     */
    private Integer deviceId;

    /**
     * 表名(逆变器信号点表)
     */
    private String tableName;

    /**
     * 解析顺序,从0开始
     */
    private Integer parseOrder;

    /**
     * 信号数量YC,YX,YK,YT，分隔符英文逗号
     */
    private String size;

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

    public Integer getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorSn() {
        return collectorSn;
    }

    public void setCollectorSn(String collectorSn) {
        this.collectorSn = collectorSn;
    }

    public Integer getDataModel() {
        return dataModel;
    }

    public void setDataModel(Integer dataModel) {
        this.dataModel = dataModel;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getParseOrder() {
        return parseOrder;
    }

    public void setParseOrder(Integer parseOrder) {
        this.parseOrder = parseOrder;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
        CollectorMessageStructure other = (CollectorMessageStructure) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCollectorId() == null ? other.getCollectorId() == null : this.getCollectorId().equals(other.getCollectorId()))
            && (this.getCollectorSn() == null ? other.getCollectorSn() == null : this.getCollectorSn().equals(other.getCollectorSn()))
            && (this.getDataModel() == null ? other.getDataModel() == null : this.getDataModel().equals(other.getDataModel()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getTableName() == null ? other.getTableName() == null : this.getTableName().equals(other.getTableName()))
            && (this.getParseOrder() == null ? other.getParseOrder() == null : this.getParseOrder().equals(other.getParseOrder()))
            && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCollectorId() == null) ? 0 : getCollectorId().hashCode());
        result = prime * result + ((getCollectorSn() == null) ? 0 : getCollectorSn().hashCode());
        result = prime * result + ((getDataModel() == null) ? 0 : getDataModel().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getParseOrder() == null) ? 0 : getParseOrder().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
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
        sb.append(", collectorId=").append(collectorId);
        sb.append(", collectorSn=").append(collectorSn);
        sb.append(", dataModel=").append(dataModel);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", tableName=").append(tableName);
        sb.append(", parseOrder=").append(parseOrder);
        sb.append(", size=").append(size);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}