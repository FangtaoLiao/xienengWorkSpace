package com.synpowertech.dataCollectionJar.domain;

public class CollectorMessageStructure {
    private Integer id;

    private Integer collectorId;
    
    private String collectorSn;

 	private Integer dataModel;

    private Integer deviceId;

    private String tableName;

    private Integer parseOrder;

    private String size;

    private String valid;

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
        this.collectorSn = collectorSn == null ? null : collectorSn.trim();
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
        this.tableName = tableName == null ? null : tableName.trim();
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
        this.size = size == null ? null : size.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }

	public CollectorMessageStructure(Integer collectorId, String collectorSn, Integer dataModel, Integer deviceId,
			String tableName, Integer parseOrder, String size, String valid) {
		super();
		this.collectorId = collectorId;
		this.collectorSn = collectorSn;
		this.dataModel = dataModel;
		this.deviceId = deviceId;
		this.tableName = tableName;
		this.parseOrder = parseOrder;
		this.size = size;
		this.valid = valid;
	}

	public CollectorMessageStructure() {}
    
}