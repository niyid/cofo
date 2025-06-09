/**
 * 
 */
package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

/**
 * AppUser roles.
 * 
 * @author developer
 */
@Entity
public class UserRole implements Serializable, GrantedAuthority {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4529017104077822590L;
	private Long id;
	private String application;
	private ServiceAgent user;
	private String role;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the application
	 */
	@Column(length = 50, nullable = false)
	public String getApplication() {
		return this.application;
	}

	/**
	 * @return the user
	 */
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, optional = false)
	public ServiceAgent getUser() {
		return this.user;
	}

	/**
	 * @return the role
	 */
	@Column(length = 50, nullable = false)
	public String getRole() {
		return this.role;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(ServiceAgent user) {
		this.user = user;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.application == null) ? 0 : this.application.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.role == null) ? 0 : this.role.hashCode());
		result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserRole other = (UserRole) obj;
		if (this.application == null) {
			if (other.application != null) {
				return false;
			}
		} else if (!this.application.equals(other.application)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.role == null) {
			if (other.role != null) {
				return false;
			}
		} else if (!this.role.equals(other.role)) {
			return false;
		}
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return role;
	}

	@Override
	@Transient
	public String getAuthority() {
		// TODO Auto-generated method stub
		return role;
	}
}
