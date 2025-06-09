package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class VitalInformationId implements Serializable {

	public static enum IdType {DRIVERS_LICENSE, PASSPORT, BVN, NIN, TIN, RC_NUMBER};
	/**
	 * 
	 */
	private static final long serialVersionUID = -7928813798647309328L;

	private IdType idType;
	
	private String idValue;

	public IdType getIdType() {
		return idType;
	}

	public void setIdType(IdType idType) {
		this.idType = idType;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idType == null) ? 0 : idType.hashCode());
		result = prime * result + ((idValue == null) ? 0 : idValue.hashCode());
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
		VitalInformationId other = (VitalInformationId) obj;
		if (idType != other.idType)
			return false;
		if (idValue == null) {
			if (other.idValue != null)
				return false;
		} else if (!idValue.equals(other.idValue))
			return false;
		return true;
	}
}
