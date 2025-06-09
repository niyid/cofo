package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class ContactInfo extends MySqlBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7190785692142202983L;
	
	private String primaryPhone;
	
	private String secondaryPhone;
	
	private String primaryEmail;
	
	private String secondaryEmail;

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	public String getSecondaryPhone() {
		return secondaryPhone;
	}

	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

}
