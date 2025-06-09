package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ProcessFlowItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2842264125741300828L;

	private Long id;
	
	private Personnel signatory;
	
	private byte[] signature;
	
	private Date scheduledDate;
	
	private Date signatureDate;
	
	private boolean docReqCompleted;
	
	private ProcessFlowItem nextItem;
	
	private List<ProcessFlowQuery> queryMessages;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public Personnel getSignatory() {
		return signatory;
	}

	public void setSignatory(Personnel signatory) {
		this.signatory = signatory;
	}

	@Lob
	@Column(length = 15000)
	@Basic(fetch = FetchType.LAZY)
	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	@Temporal(TemporalType.DATE)
	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getSignatureDate() {
		return signatureDate;
	}

	public void setSignatureDate(Date signatureDate) {
		this.signatureDate = signatureDate;
	}

	public boolean isDocReqCompleted() {
		return docReqCompleted;
	}

	public void setDocReqCompleted(boolean validated) {
		this.docReqCompleted = validated;
	}

	@OneToOne
	public ProcessFlowItem getNextItem() {
		return nextItem;
	}

	public void setNextItem(ProcessFlowItem nextItem) {
		this.nextItem = nextItem;
	}

	@OneToMany(mappedBy = "processFlowItem")
	public List<ProcessFlowQuery> getQueryMessages() {
		return queryMessages;
	}

	public void setQueryMessages(List<ProcessFlowQuery> queryMessages) {
		this.queryMessages = queryMessages;
	}
	
}
