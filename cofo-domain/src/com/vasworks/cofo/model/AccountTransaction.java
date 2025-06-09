package com.vasworks.cofo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AccountTransaction implements Serializable {
	
	public static enum TransactionType {DEBIT, CREDIT};

	/**
	 * 
	 */
	private static final long serialVersionUID = -7626742288029921134L;

	private Long id;
	
	private VitalInformation accountOwner;
	
	private BigDecimal transactionAmount;
	
	private TransactionType transactionType;
	
	private Date transactionDate;
	
	private CooCharge chargeType;
	
	private ServiceAgent serviceAgent;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public VitalInformation getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(VitalInformation accountOwner) {
		this.accountOwner = accountOwner;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@ManyToOne
	public CooCharge getChargeType() {
		return chargeType;
	}

	public void setChargeType(CooCharge chargeType) {
		this.chargeType = chargeType;
	}

	@ManyToOne
	public ServiceAgent getServiceAgent() {
		return serviceAgent;
	}

	public void setServiceAgent(ServiceAgent serviceAgent) {
		this.serviceAgent = serviceAgent;
	}
}
