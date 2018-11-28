package com.synpower.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StorageReportOrder {
	//时间
	private long time;
	//充电量
	private double charge;
	//放电量
	private double discharge;
	//收益
	private double income;
	//转换效率
	public double conversion;
	private String formatStr;
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
	public String getCharge() {
		if (charge==-999999.99999) {
			return "--";
		}
		return charge+"";
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public String getDischarge() {
		if(discharge==-999999.99999){
			return "--";
		}
		return discharge+"";
	}
	public void setDischarge(double discharge) {
		this.discharge = discharge;
	}
	public String getIncome() {
		if(income==-999999.99999){
			return "--";
		}
		return income+"";
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public String getConversion() {
		if(conversion==-999999.99999){
			return "--";
		}
		return conversion+"%";
	}
	public void setConversion(double conversion) {
		this.conversion = conversion;
	}
	public String getFormatStr() {
		return formatStr;
	}
	public void setFormatStr(String formatStr) {
		this.formatStr = formatStr;
	}
}
