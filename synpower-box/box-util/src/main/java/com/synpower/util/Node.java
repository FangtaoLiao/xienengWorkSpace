package com.synpower.util;

import java.util.List;

public class Node {
    private String id;
    private String rightsName;
    private List<Node>child;// 子节点集合
    private int sn;
    public void setSn(int sn) {
		this.sn = sn;
	}
    public int getSn() {
		return sn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRightsName() {
		return rightsName;
	}
	public void setRightsName(String rightsName) {
		this.rightsName = rightsName;
	}
	public List<Node> getChild() {
		return child;
	}
	public void setChild(List<Node> child) {
		this.child = child;
	}
	@Override
	public String toString() {
		return "Node [id=" + id + ", rightsName=" + rightsName + ", child=" + child + ", sn=" + sn + "]";
	}

      
}