package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ProcessFlowQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3566157293473083331L;

	private Long id;
	
	private String textMessage;
	
	private Date queryDate;
	
	private ProcessFlowItem processFlowItem;
	
	private ServiceAgent serviceAgent;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}

	@ManyToOne
	public ProcessFlowItem getProcessFlowItem() {
		return processFlowItem;
	}

	public void setProcessFlowItem(ProcessFlowItem processFlowItem) {
		this.processFlowItem = processFlowItem;
	}

	@ManyToOne
	public ServiceAgent getServiceAgent() {
		return serviceAgent;
	}

	public void setServiceAgent(ServiceAgent serviceAgent) {
		this.serviceAgent = serviceAgent;
	}
}
