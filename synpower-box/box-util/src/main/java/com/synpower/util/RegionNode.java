package com.synpower.util;


public class RegionNode {
	private String id;
	private String fatherId;
	private String[] location;
	private boolean hasChild;
	private int type;
	private String name;
	private int count=0;
	private String plantType;
	private int level;
	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}
	public String getPlantType() {
		return plantType;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	public String[] getLocation() {
		return location;
	}
	public void setLocation(String[] location) {
		this.location = location;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
