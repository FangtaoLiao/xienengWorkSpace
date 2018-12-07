package com.synpower.bean;

/**
 * @author SP0025
 * 所有参数类封装
 */
public class AllParam {

    private Integer length; //每页记录数
    private String orderName;//排序字段名
    private String order;    //排序规则
    private Integer start;  //起始页
    private String taskStatus; //任务状态
    private String tokenId;
    private String total;//总数
    private String taskName;  //任务名称
    private String breakTime; //创建时间
    private String updateTime; //更新任务时间
    private String addr;//电站地址
    private String plantName;//电站名称
    private String plantStatus;//电站状态
    private String devName;//设备名称
    private Integer dev_id;
    private Integer yx_id;
    private String devStatus;//设备状态
    private String status_name;//告警名称
    private String level;//告警等级
    private String avaTar;   //用户头像
    private String userName;//用户名
    private String userTell;//用户电话

    public Integer getDev_id() {
        return dev_id;
    }

    public Integer getYx_id() {
        return yx_id;
    }

    public void setYx_id(Integer yx_id) {
        this.yx_id = yx_id;
    }

    public void setDev_id(Integer dev_id) {
        this.dev_id = dev_id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantStatus() {
        return plantStatus;
    }

    public void setPlantStatus(String plantStatus) {
        this.plantStatus = plantStatus;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAvaTar() {
        return avaTar;
    }

    public void setAvaTar(String avaTar) {
        this.avaTar = avaTar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTell() {
        return userTell;
    }

    public void setUserTell(String userTell) {
        this.userTell = userTell;
    }




    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
