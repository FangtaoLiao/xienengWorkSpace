package com.synpowertech.dataCollectionJar.domain;

public class YxInfo {
    private Integer device_id;
    private String  device_name;
    private Integer plant_id;
    private String  plant_name;
    private String  signal_name;
    private Integer yx_id;

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public Integer getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(Integer plant_id) {
        this.plant_id = plant_id;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getSignal_name() {
        return signal_name;
    }

    public void setSignal_name(String signal_name) {
        this.signal_name = signal_name;
    }

    public Integer getYx_id() {
        return yx_id;
    }

    public void setYx_id(Integer yx_id) {
        this.yx_id = yx_id;
    }
}
