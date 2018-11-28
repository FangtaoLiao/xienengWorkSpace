package com.synpower.util;

import java.io.Serializable;

public class IncomeData  implements Serializable{
	/**
	 * 实时功率
	 * */
	private String currentPower;
	/**
	 * 实时收益
	 * */
	private String currentIncome;
	/**
	 * 实时发电量
	 * */
	private String currentEnergy;
	public String getCurrentPower() {
		return currentPower;
	}
	public void setCurrentPower(String currentPower) {
		this.currentPower = currentPower;
	}
	public String getCurrentIncome() {
		return currentIncome;
	}
	public void setCurrentIncome(String currentIncome) {
		this.currentIncome = currentIncome;
	}
	public String getCurrentEnergy() {
		return currentEnergy;
	}
	public void setCurrentEnergy(String currentEnergy) {
		this.currentEnergy = currentEnergy;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentEnergy == null) ? 0 : currentEnergy.hashCode());
		result = prime * result + ((currentIncome == null) ? 0 : currentIncome.hashCode());
		result = prime * result + ((currentPower == null) ? 0 : currentPower.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IncomeData other = (IncomeData) obj;
		if (currentEnergy == null) {
			if (other.currentEnergy != null)
				return false;
		} else if (!currentEnergy.equals(other.currentEnergy))
			return false;
		if (currentIncome == null) {
			if (other.currentIncome != null)
				return false;
		} else if (!currentIncome.equals(other.currentIncome))
			return false;
		if (currentPower == null) {
			if (other.currentPower != null)
				return false;
		} else if (!currentPower.equals(other.currentPower))
			return false;
		return true;
	}
	
	
}
