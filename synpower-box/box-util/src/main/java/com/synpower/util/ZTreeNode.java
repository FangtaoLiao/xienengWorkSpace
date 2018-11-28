package com.synpower.util;

import java.util.List;

import com.synpower.bean.PlantInfo;

public class ZTreeNode {
	private String id;
	private String name;
	private String pId;
	private List<PlantInfo> plantList;
	private String icon;
	private String iconSkin;//节点样式
	private boolean checked=false;//是否选中
	private boolean isParent=false;//是不是父节点
	private boolean open=false;//是否展开
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconSkin() {
		return iconSkin;
	}
	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public List<PlantInfo> getPlantList() {
		return plantList;
	}
	public void setPlantList(List<PlantInfo> plantList) {
		this.plantList = plantList;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
