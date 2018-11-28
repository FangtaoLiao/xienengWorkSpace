package com.synpower.util;


/**
 * 属性文件读取
 * 
 * @author PC043
 *
 */
public class PropertyUtil {
	private String workId;// 机器号，负载均衡后需要手动调整，以满足ID生成
	private String datacenterId;//数据中心号，目前就等于机器号

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public String getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(String datacenterId) {
		this.datacenterId = datacenterId;
	}
}
