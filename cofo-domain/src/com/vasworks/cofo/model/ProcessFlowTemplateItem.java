package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProcessFlowTemplateItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2842264125741300828L;

	private Long id;
	
	private String userRole;
	
	private int numberOfDays;
	
	private ProcessFlowTemplateItem nextItem;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@ManyToOne
	public ProcessFlowTemplateItem getNextItem() {
		return nextItem;
	}

	public void setNextItem(ProcessFlowTemplateItem nextItem) {
		this.nextItem = nextItem;
	}
	
}
