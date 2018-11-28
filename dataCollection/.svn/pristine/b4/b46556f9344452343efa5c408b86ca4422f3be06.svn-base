package com.synpowertech.dataCollectionJar.domain;

public class DataYx {
    private Integer id;

    private Integer deviceId;

    private Integer yxId;

    private Long changeTime;

    private String statusValue;
    
    private String status;

    private Long operationTime;

    private Integer operator;

    private String valid;


	public DataYx(Integer deviceId, Integer yxId, Long changeTime, String status) {
		this.deviceId = deviceId;
		this.yxId = yxId;
		this.changeTime = changeTime;
		this.status = status;
	}
	
	public DataYx(Integer deviceId, Integer yxId, Long changeTime, String statusValue , String status ,String valid) {
		this.deviceId = deviceId;
		this.yxId = yxId;
		this.changeTime = changeTime;
		this.statusValue = statusValue;
		this.status = status;
		this.valid = valid;
	}

	
	public DataYx() {}


	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getYxId() {
        return yxId;
    }

    public void setYxId(Integer yxId) {
        this.yxId = yxId;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }
    
    public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Long operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }
}