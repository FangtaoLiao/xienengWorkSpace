package com.synpowertech.dataCollectionJar.domain;

public class CollYxExpand {
    private Integer id;

    private String signalGuid;

    private String signalName;

    private Integer dataModel;

    private Integer ycAlarm;

    private Integer alarmBit;

    private Integer alarmBitLength;

    private String alarmType;

    private String statusName;

    private Integer statusValue;

    private String alarmLevel;

    private String suggest;

    private String alarmReason;

    private String faultPoint;

    private String valid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSignalGuid() {
        return signalGuid;
    }

    public void setSignalGuid(String signalGuid) {
        this.signalGuid = signalGuid == null ? null : signalGuid.trim();
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName == null ? null : signalName.trim();
    }

    public Integer getDataModel() {
        return dataModel;
    }

    public void setDataModel(Integer dataModel) {
        this.dataModel = dataModel;
    }

    public Integer getYcAlarm() {
        return ycAlarm;
    }

    public void setYcAlarm(Integer ycAlarm) {
        this.ycAlarm = ycAlarm;
    }

    public Integer getAlarmBit() {
        return alarmBit;
    }

    public void setAlarmBit(Integer alarmBit) {
        this.alarmBit = alarmBit;
    }

    public Integer getAlarmBitLength() {
        return alarmBitLength;
    }

    public void setAlarmBitLength(Integer alarmBitLength) {
        this.alarmBitLength = alarmBitLength;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType == null ? null : alarmType.trim();
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName == null ? null : statusName.trim();
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel == null ? null : alarmLevel.trim();
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest == null ? null : suggest.trim();
    }

    public String getAlarmReason() {
        return alarmReason;
    }

    public void setAlarmReason(String alarmReason) {
        this.alarmReason = alarmReason == null ? null : alarmReason.trim();
    }

    public String getFaultPoint() {
        return faultPoint;
    }

    public void setFaultPoint(String faultPoint) {
        this.faultPoint = faultPoint == null ? null : faultPoint.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }

	public CollYxExpand(String signalGuid, String signalName, Integer dataModel, Integer ycAlarm, Integer alarmBit,
			Integer alarmBitLength, String alarmType, String statusName, Integer statusValue, String alarmLevel,
			String suggest, String alarmReason, String faultPoint, String valid) {
		super();
		this.signalGuid = signalGuid;
		this.signalName = signalName;
		this.dataModel = dataModel;
		this.ycAlarm = ycAlarm;
		this.alarmBit = alarmBit;
		this.alarmBitLength = alarmBitLength;
		this.alarmType = alarmType;
		this.statusName = statusName;
		this.statusValue = statusValue;
		this.alarmLevel = alarmLevel;
		this.suggest = suggest;
		this.alarmReason = alarmReason;
		this.faultPoint = faultPoint;
		this.valid = valid;
	}

	public CollYxExpand() {}
    
}