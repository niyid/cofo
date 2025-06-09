package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LocalGovArea implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6802294726622437960L;

	private Long id;

	private String lgaName;
	
	private String lgaCode;

	private CountryState countryState;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLgaName() {
		return lgaName;
	}

	public void setLgaName(String lgaName) {
		this.lgaName = lgaName;
	}

	public String getLgaCode() {
		return lgaCode;
	}

	public void setLgaCode(String lgaCode) {
		this.lgaCode = lgaCode;
	}

	@ManyToOne
	public CountryState getCountryState() {
		return countryState;
	}

	public void setCountryState(CountryState countryState) {
		this.countryState = countryState;
	}
}
