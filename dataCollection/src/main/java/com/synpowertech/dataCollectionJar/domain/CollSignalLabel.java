package com.synpowertech.dataCollectionJar.domain;

public class CollSignalLabel {

	private Integer id;

    private String tablename;

    private String field;

    private Byte languageId;

    private String fieldLabel;
    
    private Integer deviceType;
    
    private Integer priority;
    
    private String unit;
    
    private String compare;
    
	private String valid;

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
        this.tablename = tablename == null ? null : tablename.trim();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field == null ? null : field.trim();
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
        this.fieldLabel = fieldLabel == null ? null : fieldLabel.trim();
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid == null ? null : valid.trim();
    }
    
    public CollSignalLabel(String tablename, String field, Byte languageId, String fieldLabel, Integer deviceType,
			Integer priority,String unit,String compare, String valid) {
		super();
		this.tablename = tablename;
		this.field = field;
		this.languageId = languageId;
		this.fieldLabel = fieldLabel;
		this.deviceType = deviceType;
		this.priority = priority;
		this.unit = unit;
		this.compare = compare;
		this.valid = valid;
	}
    public CollSignalLabel() {}
}