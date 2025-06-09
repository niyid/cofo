package com.vasworks.cofo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CooCharge implements Serializable {

	public static enum ChargeRate {FLAT_RATE, PERCENTAGE};
	/**
	 * 
	 */
	private static final long serialVersionUID = 4708148809959174450L;

	private Long id;
	
	private BigDecimal chargeValue;
	
	private String description;
	
	private ChargeRate chargeRate;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getChargeValue() {
		return chargeValue;
	}

	public void setChargeValue(BigDecimal chargeAmount) {
		this.chargeValue = chargeAmount;
	}

	public ChargeRate getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(ChargeRate chargeRate) {
		this.chargeRate = chargeRate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
