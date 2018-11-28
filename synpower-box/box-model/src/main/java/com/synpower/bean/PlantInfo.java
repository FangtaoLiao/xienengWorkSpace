package com.synpower.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/*****************************************************************************
 * @Package: com.synpower.bean ClassName: PlantInfo
 * @Description: 电站信息
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年10月20日上午10:18:36 SP0009 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/

public class PlantInfo implements Serializable {
	/**
	 * 电站唯一标识
	 */
	private Integer id;

	/**
	 * 电站名称
	 */
	private String plantName;

	/**
	 * 装机容量
	 */
	private Double capacity;
	/**
	 * 装机容量基准值单位W
	 */
	private Double capacityBase;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 电站a类型
	 */
	private Integer plantTypeA;

	/**
	 * 电站b类型
	 */
	private Integer plantTypeB;
	/**
	 * 电价
	 */
	private Integer priceId;

	/**
	 * 地址
	 */
	private String plantAddr;

	/**
	 * 并网时间
	 */
	private Long gridConnectedDate;

	/**
	 * 并网类型
	 */
	private String gridConnectedCategory;

	/**
	 * 经纬度
	 */
	private String loaction;

	/**
	 * 电站照片
	 */
	private String plantPhoto;

	/**
	 * 联系人
	 */
	private String contacts;

	/**
	 * 安全运行开始日期
	 */
	private Long safelyRunStartDate;

	/**
	 * 所属公司
	 */
	private Integer orgId;

	/**
	 * 拼音搜索
	 */
	private String pinyinSearch;

	/**
	 * 最后更改时间
	 */
	private Long lastModifyTime;

	/**
	 * 最后更改人
	 */
	private Integer lastModifyUser;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 删除状态 1 删除 0未删除
	 */
	private String plantValid;

	/**
	 * 启停用状态 1启用 0停用
	 */
	private String plantStatus;

	/**
	 * 电站描述
	 */
	private String plantIntroduction;

	/**
	 * 用户
	 */
	private List<SysUser> user;

	/**
	 * 电站面积
	 */
	private Integer plantArea;
	/**
	 * 
	 * 电站的详细地址
	 */
	private String plantFullAddress;

	/**
	 * 单块光伏板容量
	 */
	private Float plantSingleCapacity;
	/**
	 * 起始年度用电量
	 */
	private Float startYearElecValue;
	/**
	 * 起始月度用电量
	 */
	private Float startMonthElecValue;
	/**
	 * 变压器容量
	 */
	private Float voltageChangerCapacity;

	/**
	 * 电站电站用户功率因数类型 基本电费需要 a b c
	 */
	private String plantPowerFactorType;

	/**
	 * 本月最大功率因数
	 */
	private Float maxFactor;

	/**
	 * 本月最小功率因数
	 */
	private Float minFactor;

	/**
	 * 日发电量
	 */
	private Double genDay;
	/**
	 * 总发电量
	 */
	private Double genTotal;

	/**
	 * 等效利用小时
	 */
	private double ppr;

	/**
	 * 电站状态
	 */
	private String status;

	/**
	 * 联系人电话
	 */
	/**
	 * id拼接
	 */
	private String pids;
	private String contectsTel;
	private SysUser contactUser;

	private String showGridDate;

	private PlantTypeA plantTypeAInfo;

	private PlantTypeB plantTypeBInfo;

	private List<SysPowerPriceDetail> powerPriceDetails;

	private static final long serialVersionUID = 1L;

	public String getPlantPowerFactorType() {
		return plantPowerFactorType;
	}

	public void setPlantPowerFactorType(String plantPowerFactorType) {
		this.plantPowerFactorType = plantPowerFactorType;
	}

	public Float getMaxFactor() {
		return maxFactor;
	}

	public void setMaxFactor(Float maxFactor) {
		this.maxFactor = maxFactor;
	}

	public Float getMinFactor() {
		return minFactor;
	}

	public void setMinFactor(Float minFactor) {
		this.minFactor = minFactor;
	}

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

	public void setPowerPriceDetails(List<SysPowerPriceDetail> powerPriceDetails) {
		this.powerPriceDetails = powerPriceDetails;
	}

	public List<SysPowerPriceDetail> getPowerPriceDetails() {
		return powerPriceDetails;
	}

	public PlantTypeA getPlantTypeAInfo() {
		return plantTypeAInfo;
	}

	public void setPlantTypeAInfo(PlantTypeA plantTypeAInfo) {
		this.plantTypeAInfo = plantTypeAInfo;
	}

	public PlantTypeB getPlantTypeBInfo() {
		return plantTypeBInfo;
	}

	public void setPlantTypeBInfo(PlantTypeB plantTypeBInfo) {
		this.plantTypeBInfo = plantTypeBInfo;
	}

	public Integer getPlantTypeA() {
		return plantTypeA;
	}

	public void setPlantTypeA(Integer plantTypeA) {
		this.plantTypeA = plantTypeA;
	}

	public Integer getPlantTypeB() {
		return plantTypeB;
	}

	public void setPlantTypeB(Integer plantTypeB) {
		this.plantTypeB = plantTypeB;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public String getShowGridDate() {
		return showGridDate;
	}

	public void setShowGridDate(String showGridDate) {
		this.showGridDate = showGridDate;
	}

	public String getContectsTel() {
		return contectsTel;
	}

	public void setContectsTel(String contectsTel) {
		this.contectsTel = contectsTel;
	}

	public void setContactUser(SysUser contactUser) {
		this.contactUser = contactUser;
	}

	public SysUser getContactUser() {
		return contactUser;
	}

	public Double getGenDay() {
		return genDay;
	}

	public void setGenDay(Double genDay) {
		this.genDay = genDay;
	}

	public Double getGenTotal() {
		return genTotal;
	}

	public void setGenTotal(Double genTotal) {
		this.genTotal = genTotal;
	}

	public double getPpr(double genDay) {
		BigDecimal d1 = new BigDecimal(Double.toString(genDay));
		if (capacityBase != 0) {
			BigDecimal d2 = new BigDecimal(Double.toString(capacityBase/1000));
			BigDecimal d = d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP);
			return d.doubleValue();
		} else {
			return 0d;
		}
	}

	public void setPpr(double ppr) {
		this.ppr = ppr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPlantArea() {
		return plantArea;
	}

	public void setPlantArea(Integer plantArea) {
		this.plantArea = plantArea;
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
		this.plantName = plantName;
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
		this.unit = unit;
	}

	public String getPlantAddr() {
		return plantAddr;
	}

	public void setPlantAddr(String plantAddr) {
		this.plantAddr = plantAddr;
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
		this.gridConnectedCategory = gridConnectedCategory;
	}

	public String getLoaction() {
		return loaction;
	}

	public void setLoaction(String loaction) {
		this.loaction = loaction;
	}

	public String getPlantPhoto() {
		return plantPhoto;
	}

	public void setPlantPhoto(String plantPhoto) {
		this.plantPhoto = plantPhoto;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
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
		this.pinyinSearch = pinyinSearch;
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
		this.plantValid = plantValid;
	}

	public String getPlantStatus() {
		return plantStatus;
	}

	public void setPlantStatus(String plantStatus) {
		this.plantStatus = plantStatus;
	}

	public String getPlantIntroduction() {
		return plantIntroduction;
	}

	public void setPlantIntroduction(String plantIntroduction) {
		this.plantIntroduction = plantIntroduction;
	}

	public String getPlantFullAddress() {
		return plantFullAddress;
	}

	public void setPlantFullAddress(String plantFullAddress) {
		this.plantFullAddress = plantFullAddress;
	}

	public Float getPlantSingleCapacity() {
		return plantSingleCapacity;
	}

	public void setPlantSingleCapacity(Float plantSingleCapacity) {
		this.plantSingleCapacity = plantSingleCapacity;
	}

	public List<SysUser> getUser() {
		return user;
	}

	public void setUser(List<SysUser> user) {
		this.user = user;
	}

	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}

	public Integer getPriceId() {
		return priceId;
	}

	public Double calcCapacityBase() {
		if ("KW".equals(unit.toUpperCase())) {
			capacityBase = 1000 * capacity;
		}
		if ("MW".equals(unit.toUpperCase())) {
			capacityBase = 1000000 * capacity;
		}
		if ("GW".equals(unit.toUpperCase())) {
			capacityBase = 1000000000 * capacity;
		}
		return capacityBase;
	}

	public Double getCapacityBase() {
		return capacityBase;
	}

	public void setCapacityBase(Double capacityBase) {
		this.capacityBase = capacityBase;
	}
}