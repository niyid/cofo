package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Organization extends MySqlBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3706846969688000654L;
	
	private String organizationName;
	
	private ContactInfo contactInfo;
	
	private Address address;
	
	private Currency currency;
	
	private byte[] logo;

	@Column(unique = true)
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String companyName) {
		this.organizationName = companyName;
	}

	@ManyToOne
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	@ManyToOne
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@ManyToOne
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	@Lob
	@Column(length = 15000)
	@Basic(fetch = FetchType.LAZY)
	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return organizationName;
	}
}
