package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GeoStreet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 558108060353982021L;
	
	private Long id;
	
	private String description;
	
	private LocalGovArea localGovArea;

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public LocalGovArea getLocalGovArea() {
		return localGovArea;
	}

	public void setLocalGovArea(LocalGovArea localGovArea) {
		this.localGovArea = localGovArea;
	}

}
