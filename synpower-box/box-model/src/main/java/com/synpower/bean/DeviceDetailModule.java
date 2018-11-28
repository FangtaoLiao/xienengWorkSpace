package com.synpower.bean;

import java.io.Serializable;

/**
 * @author 
 */
public class DeviceDetailModule implements Serializable {
    /**
     * key
     */
    private Integer id;

    /**
     * 型号标识
     */
    private String modelIdentity;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 组件类型
     */
    private String subassemblyType;

    /**
     * 功率[Wp]
     */
    private Float power;

    /**
     * 效率[%]
     */
    private String efficiency;

    /**
     * Voc[V]
     */
    private Float voc;

    /**
     * Isc[A]
     */
    private Float isc;

    /**
     * Vm[V]
     */
    private Float vm;

    /**
     * Im[A]
     */
    private Float im;

    /**
     * FF[%]
     */
    private Float ff;

    /**
     * Pmax温度系数[%]
     */
    private Float pmaxTemperatureCoefficient;

    /**
     * Voc温度系数[%]
     */
    private Float vocTemperatureCoefficient;

    /**
     * Isc温度系数[%]
     */
    private Float iscTemperatureCoefficient;

    /**
     * 工作温度[oC]
     */
    private String workingTemperature;

    /**
     * 该型号设备图片，多张可用';'等分隔符
     */
    private String photoPath;

    /**
     * 是否可用，0表示正常，1表示停用
     */
    private String valid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelIdentity() {
        return modelIdentity;
    }

    public void setModelIdentity(String modelIdentity) {
        this.modelIdentity = modelIdentity;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSubassemblyType() {
        return subassemblyType;
    }

    public void setSubassemblyType(String subassemblyType) {
        this.subassemblyType = subassemblyType;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public Float getVoc() {
        return voc;
    }

    public void setVoc(Float voc) {
        this.voc = voc;
    }

    public Float getIsc() {
        return isc;
    }

    public void setIsc(Float isc) {
        this.isc = isc;
    }

    public Float getVm() {
        return vm;
    }

    public void setVm(Float vm) {
        this.vm = vm;
    }

    public Float getIm() {
        return im;
    }

    public void setIm(Float im) {
        this.im = im;
    }

    public Float getFf() {
        return ff;
    }

    public void setFf(Float ff) {
        this.ff = ff;
    }

    public Float getPmaxTemperatureCoefficient() {
        return pmaxTemperatureCoefficient;
    }

    public void setPmaxTemperatureCoefficient(Float pmaxTemperatureCoefficient) {
        this.pmaxTemperatureCoefficient = pmaxTemperatureCoefficient;
    }

    public Float getVocTemperatureCoefficient() {
        return vocTemperatureCoefficient;
    }

    public void setVocTemperatureCoefficient(Float vocTemperatureCoefficient) {
        this.vocTemperatureCoefficient = vocTemperatureCoefficient;
    }

    public Float getIscTemperatureCoefficient() {
        return iscTemperatureCoefficient;
    }

    public void setIscTemperatureCoefficient(Float iscTemperatureCoefficient) {
        this.iscTemperatureCoefficient = iscTemperatureCoefficient;
    }

    public String getWorkingTemperature() {
        return workingTemperature;
    }

    public void setWorkingTemperature(String workingTemperature) {
        this.workingTemperature = workingTemperature;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
        DeviceDetailModule other = (DeviceDetailModule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getModelIdentity() == null ? other.getModelIdentity() == null : this.getModelIdentity().equals(other.getModelIdentity()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()))
            && (this.getManufacturer() == null ? other.getManufacturer() == null : this.getManufacturer().equals(other.getManufacturer()))
            && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
            && (this.getSubassemblyType() == null ? other.getSubassemblyType() == null : this.getSubassemblyType().equals(other.getSubassemblyType()))
            && (this.getPower() == null ? other.getPower() == null : this.getPower().equals(other.getPower()))
            && (this.getEfficiency() == null ? other.getEfficiency() == null : this.getEfficiency().equals(other.getEfficiency()))
            && (this.getVoc() == null ? other.getVoc() == null : this.getVoc().equals(other.getVoc()))
            && (this.getIsc() == null ? other.getIsc() == null : this.getIsc().equals(other.getIsc()))
            && (this.getVm() == null ? other.getVm() == null : this.getVm().equals(other.getVm()))
            && (this.getIm() == null ? other.getIm() == null : this.getIm().equals(other.getIm()))
            && (this.getFf() == null ? other.getFf() == null : this.getFf().equals(other.getFf()))
            && (this.getPmaxTemperatureCoefficient() == null ? other.getPmaxTemperatureCoefficient() == null : this.getPmaxTemperatureCoefficient().equals(other.getPmaxTemperatureCoefficient()))
            && (this.getVocTemperatureCoefficient() == null ? other.getVocTemperatureCoefficient() == null : this.getVocTemperatureCoefficient().equals(other.getVocTemperatureCoefficient()))
            && (this.getIscTemperatureCoefficient() == null ? other.getIscTemperatureCoefficient() == null : this.getIscTemperatureCoefficient().equals(other.getIscTemperatureCoefficient()))
            && (this.getWorkingTemperature() == null ? other.getWorkingTemperature() == null : this.getWorkingTemperature().equals(other.getWorkingTemperature()))
            && (this.getPhotoPath() == null ? other.getPhotoPath() == null : this.getPhotoPath().equals(other.getPhotoPath()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModelIdentity() == null) ? 0 : getModelIdentity().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        result = prime * result + ((getManufacturer() == null) ? 0 : getManufacturer().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getSubassemblyType() == null) ? 0 : getSubassemblyType().hashCode());
        result = prime * result + ((getPower() == null) ? 0 : getPower().hashCode());
        result = prime * result + ((getEfficiency() == null) ? 0 : getEfficiency().hashCode());
        result = prime * result + ((getVoc() == null) ? 0 : getVoc().hashCode());
        result = prime * result + ((getIsc() == null) ? 0 : getIsc().hashCode());
        result = prime * result + ((getVm() == null) ? 0 : getVm().hashCode());
        result = prime * result + ((getIm() == null) ? 0 : getIm().hashCode());
        result = prime * result + ((getFf() == null) ? 0 : getFf().hashCode());
        result = prime * result + ((getPmaxTemperatureCoefficient() == null) ? 0 : getPmaxTemperatureCoefficient().hashCode());
        result = prime * result + ((getVocTemperatureCoefficient() == null) ? 0 : getVocTemperatureCoefficient().hashCode());
        result = prime * result + ((getIscTemperatureCoefficient() == null) ? 0 : getIscTemperatureCoefficient().hashCode());
        result = prime * result + ((getWorkingTemperature() == null) ? 0 : getWorkingTemperature().hashCode());
        result = prime * result + ((getPhotoPath() == null) ? 0 : getPhotoPath().hashCode());
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
        sb.append(", modelIdentity=").append(modelIdentity);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append(", model=").append(model);
        sb.append(", subassemblyType=").append(subassemblyType);
        sb.append(", power=").append(power);
        sb.append(", efficiency=").append(efficiency);
        sb.append(", voc=").append(voc);
        sb.append(", isc=").append(isc);
        sb.append(", vm=").append(vm);
        sb.append(", im=").append(im);
        sb.append(", ff=").append(ff);
        sb.append(", pmaxTemperatureCoefficient=").append(pmaxTemperatureCoefficient);
        sb.append(", vocTemperatureCoefficient=").append(vocTemperatureCoefficient);
        sb.append(", iscTemperatureCoefficient=").append(iscTemperatureCoefficient);
        sb.append(", workingTemperature=").append(workingTemperature);
        sb.append(", photoPath=").append(photoPath);
        sb.append(", valid=").append(valid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}