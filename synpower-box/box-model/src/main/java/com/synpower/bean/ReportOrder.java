package com.synpower.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReportOrder {
	private double co2;
	private double coal;
	private double gen;
	private double income;
	private double plantRank;
	private double ppr;
	private long time;
	private double tree;
	private String formatStr;
	private double capacity;
	private int id;
	private String name;
	private int inventerNum;
	private double electriNumUsed;
	private double electriMoneyUsed;
	private double electriNumOn;
	private double electriMoneyOn;
	private double electriNumOff;
	private double electriMoneyOff;
	
	
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
	public void setInventerNum(int inventerNum) {
		this.inventerNum = inventerNum;
	}
	public int getInventerNum() {
		return inventerNum;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setFormatStr(String formatStr) {
		this.formatStr = formatStr;
	}
	public String getCo2() {
		if (co2==-999999.99999) {
			return "--";
		}
		return co2+"";
	}
	public void setCo2(double co2) {
		this.co2 = co2;
	}
	public String getCoal() {
		if (coal==-999999.99999) {
			return "--";
		}
		return coal+"";
	}
	public void setCoal(double coal) {
		this.coal = coal;
	}
	public String getGen() {
		if (gen==-999999.99999) {
			return "--";
		}
		return gen+"";
	}
	public void setGen(double gen) {
		this.gen = gen;
	}
	public String getIncome() {
		if (income==-999999.99999) {
			return "--";
		}
		return income+"";
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public String getPlantRank() {
		if (plantRank==-999999.99999) {
			return "--";
		}
		return plantRank+"";
	}
	public void setPlantRank(double plantRank) {
		this.plantRank = plantRank;
	}
	public String getPpr() {
		if (ppr==-999999.99999) {
			return "--";
		}
		return ppr+"";
	}
	public void setPpr(double ppr) {
		this.ppr = ppr;
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
	public String getTree() {
		if (tree==-999999.99999) {
			return "--";
		}
		return tree+"";
	}
	public void setTree(double tree) {
		this.tree = tree;
	}
	public String getFormatStr() {
		return formatStr;
	}
	
}
