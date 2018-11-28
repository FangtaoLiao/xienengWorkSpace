package com.synpowertech.dataCollectionJar.domain;

public class PlantInfo {
    private Integer id;

    private String plantName;

    private Double capacity;

    private String unit;

    private String plantType;

    private String plantAddr;

    private Long gridConnectedDate;

    private String gridConnectedCategory;

    private String loaction;

    private String plantPhoto;

    private String contacts;

    private Long safelyRunStartDate;

    private Integer orgId;

    private String pinyinSearch;

    private Long lastModifyTime;

    private Integer lastModifyUser;

    private Long createTime;

    private String plantValid;

    private String plantStatus;

    private String plantIntroduction;

    private Integer plantArea;

    private String plantFullAddress;

    private Float plantSingleCapacity;

    private Float startYearElecValue;

    private Float startMonthElecValue;

    private Float voltageChangerCapacity;

    public Float getStartYearElecValue() {
        return startYearElecValue;
    }

    public void setStartYearElecValue(Float startYearElecValue) {
        this.startYearElecValue = startYearElecValue;
    }

    public Float getStartMonthElecValue() {
        return startMonthElecValue;
    }

    public void setStartMonthElecValue(Float startMonthElecValue) {
        this.startMonthElecValue = startMonthElecValue;
    }

    public Float getVoltageChangerCapacity() {
        return voltageChangerCapacity;
    }

    public void setVoltageChangerCapacity(Float voltageChangerCapacity) {
        this.voltageChangerCapacity = voltageChangerCapacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName == null ? null : plantName.trim();
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType == null ? null : plantType.trim();
    }

    public String getPlantAddr() {
        return plantAddr;
    }

    public void setPlantAddr(String plantAddr) {
        this.plantAddr = plantAddr == null ? null : plantAddr.trim();
    }

    public Long getGridConnectedDate() {
        return gridConnectedDate;
    }

    public void setGridConnectedDate(Long gridConnectedDate) {
        this.gridConnectedDate = gridConnectedDate;
    }

    public String getGridConnectedCategory() {
        return gridConnectedCategory;
    }

    public void setGridConnectedCategory(String gridConnectedCategory) {
        this.gridConnectedCategory = gridConnectedCategory == null ? null : gridConnectedCategory.trim();
    }

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction == null ? null : loaction.trim();
    }

    public String getPlantPhoto() {
        return plantPhoto;
    }

    public void setPlantPhoto(String plantPhoto) {
        this.plantPhoto = plantPhoto == null ? null : plantPhoto.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public Long getSafelyRunStartDate() {
        return safelyRunStartDate;
    }

    public void setSafelyRunStartDate(Long safelyRunStartDate) {
        this.safelyRunStartDate = safelyRunStartDate;
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
        this.pinyinSearch = pinyinSearch == null ? null : pinyinSearch.trim();
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPlantValid() {
        return plantValid;
    }

    public void setPlantValid(String plantValid) {
        this.plantValid = plantValid == null ? null : plantValid.trim();
    }

    public String getPlantStatus() {
        return plantStatus;
    }

    public void setPlantStatus(String plantStatus) {
        this.plantStatus = plantStatus == null ? null : plantStatus.trim();
    }

    public String getPlantIntroduction() {
        return plantIntroduction;
    }

    public void setPlantIntroduction(String plantIntroduction) {
        this.plantIntroduction = plantIntroduction == null ? null : plantIntroduction.trim();
    }

    public Integer getPlantArea() {
        return plantArea;
    }

    public void setPlantArea(Integer plantArea) {
        this.plantArea = plantArea;
    }

    public String getPlantFullAddress() {
        return plantFullAddress;
    }

    public void setPlantFullAddress(String plantFullAddress) {
        this.plantFullAddress = plantFullAddress == null ? null : plantFullAddress.trim();
    }

    public Float getPlantSingleCapacity() {
        return plantSingleCapacity;
    }

    public void setPlantSingleCapacity(Float plantSingleCapacity) {
        this.plantSingleCapacity = plantSingleCapacity;
    }
}