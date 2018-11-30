package com.synpower.bean;

public class Task {
    private Integer id;

    private String taskName;

    private Integer yxId;

    private Integer alarmId;

    private Integer userUpId;

    private Integer userDownId;

    private Integer lastModifyUser;

    private Integer plantId;

    private Integer deviceId;

    private Long createTime;

    private Long updateTime;

    private String taskStatus;

    private String taskValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public Integer getYxId() {
        return yxId;
    }

    public void setYxId(Integer yxId) {
        this.yxId = yxId;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public Integer getUserUpId() {
        return userUpId;
    }

    public void setUserUpId(Integer userUpId) {
        this.userUpId = userUpId;
    }

    public Integer getUserDownId() {
        return userDownId;
    }

    public void setUserDownId(Integer userDownId) {
        this.userDownId = userDownId;
    }

    public Integer getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(Integer lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.trim();
    }

    public String getTaskValid() {
        return taskValid;
    }

    public void setTaskValid(String taskValid) {
        this.taskValid = taskValid == null ? null : taskValid.trim();
    }
}