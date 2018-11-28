/**
 * 
 */
package com.synpower.bean;

import java.math.BigDecimal;

/*****************************************************************************
 * @Package: com.synpower.bean
 * @ClassName: PlantListOrder
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月18日下午3:50:33   SP0012             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
public class PlantListOrder {
	
	private String addr;
	private double capacity;
	private double distribution;
	private double genToday;
	private String name;
	private String pic;
	private int plantId;
	private int type;
	private String contacts;
	private String contectsTel;
	private double genDay;
	private double genTotal;
	private int id;
	private String location;
	private double ppr;
	private int status;
	private String weather;
	private String weatherPic;
	private String unit;
	private double powerOutToday;
	private double powerInToday;
	private String soc;
	private String systemEfficace;
	private int plantTypeB;
	private int plantType;
	private double energyStorage;
	private String energyStorageUnit;
	private double phoCap;
	private String phoCapUnit;
	private String genTodayUnit;
	public void setGenTodayUnit(String genTodayUnit) {
		this.genTodayUnit = genTodayUnit;
	}
	public String getGenTodayUnit() {
		return genTodayUnit;
	}
	public void setWeatherPic(String weatherPic) {
		this.weatherPic = weatherPic;
	}
	public String getWeatherPic() {
		return weatherPic;
	}
	public double getEnergyStorage() {
		return energyStorage;
	}
	public void setEnergyStorage(double energyStorage) {
		this.energyStorage = energyStorage;
	}
	public double getPhoCap() {
		return phoCap;
	}
	public void setPhoCap(double phoCap) {
		this.phoCap = phoCap;
	}
	
	public void setEnergyStorageUnit(String energyStorageUnit) {
		this.energyStorageUnit = energyStorageUnit;
	}
	public String getEnergyStorageUnit() {
		return energyStorageUnit;
	}
	public String getPhoCapUnit() {
		return phoCapUnit;
	}
	public void setPhoCapUnit(String phoCapUnit) {
		this.phoCapUnit = phoCapUnit;
	}
	public void setPlantType(int plantType) {
		this.plantType = plantType;
	}
	public int getPlantType() {
		return plantType;
	}
	
	/**
	 * @return the plantTypeB
	 */
	public int getPlantTypeB() {
		return plantTypeB;
	}
	/**
	 * @param plantTypeB the plantTypeB to set
	 */
	public void setPlantTypeB(int plantTypeB) {
		this.plantTypeB = plantTypeB;
	}
	/**
	 * @return the powerOutToday
	 */
	public double getPowerOutToday() {
		return powerOutToday;
	}
	/**
	 * @param powerOutToday the powerOutToday to set
	 */
	public void setPowerOutToday(double powerOutToday) {
		this.powerOutToday = powerOutToday;
	}
	/**
	 * @return the powerInToday
	 */
	public double getPowerInToday() {
		return powerInToday;
	}
	/**
	 * @param powerInToday the powerInToday to set
	 */
	public void setPowerInToday(double powerInToday) {
		this.powerInToday = powerInToday;
	}
	/**
	 * @return the soc
	 */
	public String getSoc() {
		return soc;
	}
	/**
	 * @param soc the soc to set
	 */
	public void setSoc(String soc) {
		this.soc = soc;
	}
	/**
	 * @return the systemEfficace
	 */
	public String getSystemEfficace() {
		return systemEfficace;
	}
	/**
	 * @param systemEfficace the systemEfficace to set
	 */
	public void setSystemEfficace(String systemEfficace) {
		this.systemEfficace = systemEfficace;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWeather() {
		return weather;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public String getDistribution() {
		if (distribution==-0.99999999) {
			return "未定位";
		}else{
			BigDecimal  d1=new BigDecimal(Double.toString(distribution));
			BigDecimal  d2=new BigDecimal(Double.toString(1000.0));
			BigDecimal d=d1.divide(d2, BigDecimal.ROUND_HALF_UP);
			return d.doubleValue()+"公里";
		}
	}
	public void setDistribution(double distribution) {
		this.distribution = distribution;
	}
	public double getGenToday() {
		return genToday;
	}
	public void setGenToday(double genToday) {
		this.genToday = genToday;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getPlantId() {
		return plantId;
	}
	public void setPlantId(int plantId) {
		this.plantId = plantId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getContectsTel() {
		return contectsTel;
	}
	public void setContectsTel(String contectsTel) {
		this.contectsTel = contectsTel;
	}
	public double getGenDay() {
		return genDay;
	}
	public void setGenDay(double genDay) {
		this.genDay = genDay;
	}
	public double getGenTotal() {
		return genTotal;
	}
	public void setGenTotal(double genTotal) {
		this.genTotal = genTotal;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getPpr() {
		return ppr;
	}
	public void setPpr(double ppr) {
		this.ppr = ppr;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
