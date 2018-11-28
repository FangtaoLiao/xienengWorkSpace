package com.synpower.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReportOrderStoredOnly {
	
	private long time;
	private double electriNumUsed;
	private double electriMoneyUsed;
	private double electriNumOn;
	private double electriMoneyOn;
	private double electriNumOff;
	private double electriMoneyOff;
	private String formatStr;
	
	
	public String getElectriMoneyOff() {
		if (electriMoneyOff==-999999.99999) {
			return "--";
		}
		return electriMoneyOff+"";
	}
	public void setElectriMoneyOff(double electriMoneyOff) {
		this.electriMoneyOff = electriMoneyOff;
	}
	public String getElectriNumUsed() {
		if (electriNumUsed==-999999.99999) {
			return "--";
		}
		return electriNumUsed+"";
	}
	public void setElectriNumUsed(double electriNumUsed) {
		this.electriNumUsed = electriNumUsed;
	}
	public String getElectriMoneyUsed() {
		if (electriMoneyUsed==-999999.99999) {
			return "--";
		}
		return electriMoneyUsed+"";
	}
	public void setElectriMoneyUsed(double electriMoneyUsed) {
		this.electriMoneyUsed = electriMoneyUsed;
	}
	public String getElectriNumOn() {
		if (electriNumOn==-999999.99999) {
			return "--";
		}
		return electriNumOn+"";
		
	}
	public void setElectriNumOn(double electriNumOn) {
		this.electriNumOn = electriNumOn;
	}
	public String getElectriMoneyOn() {
		if (electriMoneyOn==-999999.99999) {
			return "--";
		}
		return electriMoneyOn+"";
	}
	public void setElectriMoneyOn(double electriMoneyOn) {
		this.electriMoneyOn = electriMoneyOn;
	}
	public String getElectriNumOff() {
		if (electriNumOff==-999999.99999) {
			return "--";
		}
		return electriNumOff+"";
	}
	public void setElectriNumOff(double electriNumOff) {
		this.electriNumOff = electriNumOff;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getFormatStr() {
		return formatStr;
	}
	public void setFormatStr(String formatStr) {
		this.formatStr = formatStr;
	}
	public String getTime() {
		if (time==-999999L) {
			return "最大值";
		}else if (time==-999998L) {
			return "最小值";
		}else if (time==-999997L) {
			return "平均值";
		}else{
			SimpleDateFormat sdf=sdf=new SimpleDateFormat(this.formatStr);
			return sdf.format(time);
		}
	}
	public void setTime(String time) {
		SimpleDateFormat sdf=sdf=new SimpleDateFormat(this.formatStr);
		if ("max".equals(time)) {
			this.time=-999999L;
		}else if ("min".equals(time)) {
			this.time=-999998L;
		}else if ("avg".equals(time)) {
			this.time=-999997L;
		}else{
			try {
				this.time = sdf.parse(time).getTime();
			} catch (ParseException e) {
			
				}
		}
	}
	
}
