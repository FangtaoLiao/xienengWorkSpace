package com.synpower.lang;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigParam {
	@Value("#{propertyConfigurer4Config['CO2']}")
	private String co2;
	@Value("#{propertyConfigurer4Config['TREE']}")
	private String tree;
	@Value("#{propertyConfigurer4Config['COAL']}")
	private String coal;
	@Value("#{propertyConfigurer4Config['DEVICE_CONN_TIME_OUT']}")
	private String deviceConnTimeOut;
	@Value("#{propertyConfigurer4Config['RABBIT_RECEIVE_TIME_OUT']}")
	private String rabbitReveiveTimeOut;
	@Value("#{propertyConfigurer4Config['POWERPRICE']}")
	private String powerPrice;
	@Value("#{propertyConfigurer4Config['LOCATION']}")
	private String location;
	@Value("#{propertyConfigurer4Config['appid']}")
	private String appid;
	@Value("#{propertyConfigurer4Config['secret']}")
	private String secret;
	@Value("#{propertyConfigurer4Config['appid2']}")
	private String appid2;
	@Value("#{propertyConfigurer4Config['secret2']}")
	private String secret2;
	@Value("#{propertyConfigurer4Config['appid3']}")
	private String appid3;
	@Value("#{propertyConfigurer4Config['secret3']}")
	private String secret3;
	@Value("#{propertyConfigurer4Config['weixinUrl']}")
	private String weixinUrl;
	@Value("#{propertyConfigurer4Config['BAIDU_APP_KEY']}")
	private String baiduAppAk;// 百度地图开发者AK
	@Value("#{propertyConfigurer4Config['defaultRole']}")
	private String defaultRole;
	@Value("#{propertyConfigurer4Config['defaultOrg']}")
	private String defaultOrg;
	@Value("#{propertyConfigurer4Config['WEBSOCKET_CONN_TIME_OUT']}")
	private String websocketTimeOut;
	@Value("#{propertyConfigurer4Config['TX_API_KEY']}")
	private String txApiKey;
	@Value("#{propertyConfigurer4Config['PLANT_PHOTO_URL']}")
	private String plantPhotoURL;
	@Value("#{propertyConfigurer4Config['PLANT_PHOTO_REQUEST_URL']}")
	private String plantPhotoRequestURL;
	@Value("#{propertyConfigurer4Config['ICON_URL']}")
	private String iconURL;
	@Value("#{propertyConfigurer4Config['ICON_REQUEST_URL']}")
	private String iconRequestURL;
	@Value("#{propertyConfigurer4Config['SYSTEM_PHOTO_REQUEST_URL']}")
	private String systemPhotoRequestUrl;
	@Value("#{propertyConfigurer4Config['SYSTEM_PHOTO_URL']}")
	private String systemPhotoUrl;
	@Value("#{propertyConfigurer4Config['EQUIPMENT_PHOTO_REQUEST_URL']}")
	private String equipmentPhotoRequestUrl;
	@Value("#{propertyConfigurer4Config['EQUIPMENT_PHOTO_URL']}")
	private String equipmentPhotoUrl;
	@Value("#{propertyConfigurer4Config['LOG_MAX_SURVIVAL_TIME']}")
	private String logMaxSurvivalTime;
	@Value("#{propertyConfigurer4Config['BOOT_TIP_TITLE']}")
	private String bootTipTitle;
	@Value("#{propertyConfigurer4Config['BOOT_TIP_CONTENT']}")
	private String bootTipContent;
	@Value("#{propertyConfigurer4Config['SHUTDOWN_TIP_TITLE']}")
	private String shutdownTipTitle;
	@Value("#{propertyConfigurer4Config['SHUTDOWN_TIP_CONTENT']}")
	private String shutdownTipContent;
	@Value("#{propertyConfigurer4Config['TEMPLATE_ID1']}")
	private String templateId1;
	@Value("#{propertyConfigurer4Config['TEMPLATE_ID2']}")
	private String templateId2;

	public String getAppid3() {
		return appid3;
	}

	public void setAppid3(String appid3) {
		this.appid3 = appid3;
	}

	public String getSecret3() {
		return secret3;
	}

	public void setSecret3(String secret3) {
		this.secret3 = secret3;
	}

	public String getTemplateId1() {
		return templateId1;
	}

	public void setTemplateId1(String templateId1) {
		this.templateId1 = templateId1;
	}

	public String getTemplateId2() {
		return templateId2;
	}

	public void setTemplateId2(String templateId2) {
		this.templateId2 = templateId2;
	}

	public void setAppid2(String appid2) {
		this.appid2 = appid2;
	}

	public void setSecret2(String secret2) {
		this.secret2 = secret2;
	}

	public String getAppid2() {
		return appid2;
	}

	public String getSecret2() {
		return secret2;
	}

	public void setLogMaxSurvivalTime(String logMaxSurvivalTime) {
		this.logMaxSurvivalTime = logMaxSurvivalTime;
	}

	public String getLogMaxSurvivalTime() {
		return logMaxSurvivalTime;
	}

	public void setPlantPhotoURL(String plantPhotoURL) {
		this.plantPhotoURL = plantPhotoURL;
	}

	public void setPlantPhotoRequestURL(String plantPhotoRequestURL) {
		this.plantPhotoRequestURL = plantPhotoRequestURL;
	}

	public String getPlantPhotoRequestURL() {
		return plantPhotoRequestURL;
	}

	public String getPlantPhotoURL() {
		return plantPhotoURL;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getIconRequestURL() {
		return iconRequestURL;
	}

	public void setIconRequestURL(String iconRequestURL) {
		this.iconRequestURL = iconRequestURL;
	}

	public String getSystemPhotoRequestUrl() {
		return systemPhotoRequestUrl;
	}

	public void setSystemPhotoRequestUrl(String systemPhotoRequestUrl) {
		this.systemPhotoRequestUrl = systemPhotoRequestUrl;
	}

	public String getSystemPhotoUrl() {
		return systemPhotoUrl;
	}

	public void setSystemPhotoUrl(String systemPhotoUrl) {
		this.systemPhotoUrl = systemPhotoUrl;
	}

	public String getEquipmentPhotoRequestUrl() {
		return equipmentPhotoRequestUrl;
	}

	public void setEquipmentPhotoRequestUrl(String equipmentPhotoRequestUrl) {
		this.equipmentPhotoRequestUrl = equipmentPhotoRequestUrl;
	}

	public String getEquipmentPhotoUrl() {
		return equipmentPhotoUrl;
	}

	public void setEquipmentPhotoUrl(String equipmentPhotoUrl) {
		this.equipmentPhotoUrl = equipmentPhotoUrl;
	}

	public void setTxApiKey(String txApiKey) {
		this.txApiKey = txApiKey;
	}

	public String getTxApiKey() {
		return txApiKey;
	}

	public void setWebsocketTimeOut(String websocketTimeOut) {
		this.websocketTimeOut = websocketTimeOut;
	}

	public String getWebsocketTimeOut() {
		return websocketTimeOut;
	}

	public String getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(String defaultRole) {
		this.defaultRole = defaultRole;
	}

	public String getDefaultOrg() {
		return defaultOrg;
	}

	public void setDefaultOrg(String defaultOrg) {
		this.defaultOrg = defaultOrg;
	}

	public void setBaiduAppAk(String baiduAppAk) {
		this.baiduAppAk = baiduAppAk;
	}

	public String getBaiduAppAk() {
		return baiduAppAk;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getWeixinUrl() {
		return weixinUrl;
	}

	public void setWeixinUrl(String weixinUrl) {
		this.weixinUrl = weixinUrl;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public String getCo2() {
		return co2;
	}

	public void setCo2(String co2) {
		this.co2 = co2;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	public String getCoal() {
		return coal;
	}

	public void setCoal(String coal) {
		this.coal = coal;
	}

	public String getDeviceConnTimeOut() {
		return deviceConnTimeOut;
	}

	public void setDeviceConnTimeOut(String deviceConnTimeOut) {
		this.deviceConnTimeOut = deviceConnTimeOut;
	}

	public String getRabbitReveiveTimeOut() {
		return rabbitReveiveTimeOut;
	}

	public void setRabbitReveiveTimeOut(String rabbitReveiveTimeOut) {
		this.rabbitReveiveTimeOut = rabbitReveiveTimeOut;
	}

	public void setPowerPrice(String powerPrice) {
		this.powerPrice = powerPrice;
	}

	public String getPowerPrice() {
		return powerPrice;
	}

	public String getBootTipTitle() {
		return bootTipTitle;
	}

	public void setBootTipTitle(String bootTipTitle) {
		this.bootTipTitle = bootTipTitle;
	}

	public String getBootTipContent() {
		return bootTipContent;
	}

	public void setBootTipContent(String bootTipContent) {
		this.bootTipContent = bootTipContent;
	}

	public String getShutdownTipTitle() {
		return shutdownTipTitle;
	}

	public void setShutdownTipTitle(String shutdownTipTitle) {
		this.shutdownTipTitle = shutdownTipTitle;
	}

	public String getShutdownTipContent() {
		return shutdownTipContent;
	}

	public void setShutdownTipContent(String shutdownTipContent) {
		this.shutdownTipContent = shutdownTipContent;
	}

}
