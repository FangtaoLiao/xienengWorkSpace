package com.synpower.msg.session;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

import java.util.Collection;

/*****************************************************************************
 * @Package: com.synpower.msg.session ClassName: User
 * @Description: session 中user序列化
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2018年1月5日上午10:42:27 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@DefaultSerializer(BeanSerializer.class)
public class User {
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String loginId;// 用户账号
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String userName;// 用户姓名
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String iconUrl;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String email;// 电子邮箱
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String roleId;// 角色ID
//    @CollectionSerializer.BindCollection(
//            elementSerializer = DefaultSerializers.ClassSerializer.class,
//            elementClass = Object.class)
	@FieldSerializer.Bind(DefaultSerializers.ClassSerializer.class)
	private Object rights;// 权限模块
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String id;// 用户唯一标识
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String tokenId;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String gender;
//    @CollectionSerializer.BindCollection(
//            elementSerializer = DefaultSerializers.StringSerializer.class,
//            elementClass = String.class)
	@FieldSerializer.Bind(DefaultSerializers.ClassSerializer.class)
	private Object userOrg;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String userTel;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String userType;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String plantNum;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String plantId;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String plantType;
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String plantTypeList;
	/**
	 * @author ybj
	 * @Description 这个变量主要用于判断是小电能控还是小电监控登录 根据这个变量调不同的appid和secret
	 */
	@FieldSerializer.Bind(DefaultSerializers.StringSerializer.class)
	private String requestType;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public void setPlantTypeList(String plantTypeList) {
		this.plantTypeList = plantTypeList;
	}

	public String getPlantTypeList() {
		return plantTypeList;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public String getPlantType() {
		return plantType;
	}

	public void setPlantNum(String plantNum) {
		this.plantNum = plantNum;
	}

	public String getPlantNum() {
		return plantNum;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return userType;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Object getRights() {
		return rights;
	}

	public void setRights(Object rights) {
		this.rights = rights;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Object getUserOrg() {
		return userOrg;
	}

	public void setUserOrg(Object userOrg) {
		this.userOrg = userOrg;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getPlantId() {
		return plantId;
	}

}
