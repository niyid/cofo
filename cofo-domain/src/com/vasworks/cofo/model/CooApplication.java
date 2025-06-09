package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CooApplication implements Serializable {
	
	public static enum CooType {STATE_LAND, NON_STATE_LAND};
	
	public static enum CooStatus {INITIATED, REJECTED, CANCELED, COMPLETED};

	/**
	 * 
	 */
	private static final long serialVersionUID = 7268674076701616233L;
	
	private Long id;
	
	private CooType cooType;

	private boolean landDeveloped;

	private boolean landOccupied;
	
	private Date acceptanceDate;
	
	private Date rejectionDate;
	
	private Date cancellationDate;
	
	private Date completionDate;
	
	private CooStatus appStatus;
	
	private String description;
	
	private String rejectionReason;
	
	private String cancellationReason;
	
	private VitalInformation vitalInformation;
	
	private ProcessFlow processFlow;
	
	private Personnel filingOfficer;

	private List<CertificateDoc> certificateDocs;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CooType getCooType() {
		return cooType;
	}

	public void setCooType(CooType cooType) {
		this.cooType = cooType;
	}

	public boolean isLandDeveloped() {
		return landDeveloped;
	}

	public void setLandDeveloped(boolean landDeveloped) {
		this.landDeveloped = landDeveloped;
	}

	public boolean isLandOccupied() {
		return landOccupied;
	}

	public void setLandOccupied(boolean landOccupied) {
		this.landOccupied = landOccupied;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getAcceptanceDate() {
		return acceptanceDate;
	}

	public void setAcceptanceDate(Date acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getRejectionDate() {
		return rejectionDate;
	}

	public void setRejectionDate(Date rejectionDate) {
		this.rejectionDate = rejectionDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public CooStatus getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(CooStatus appStatus) {
		this.appStatus = appStatus;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Lob
	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	@Lob
	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	@OneToOne
	public VitalInformation getVitalInformation() {
		return vitalInformation;
	}

	public void setVitalInformation(VitalInformation vitalInformation) {
		this.vitalInformation = vitalInformation;
	}

	@OneToOne
	public ProcessFlow getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(ProcessFlow processFlow) {
		this.processFlow = processFlow;
	}

	@ManyToOne
	public Personnel getFilingOfficer() {
		return filingOfficer;
	}

	public void setFilingOfficer(Personnel filingOfficer) {
		this.filingOfficer = filingOfficer;
	}
	
	@OneToMany(mappedBy = "cooApplication")
	public List<CertificateDoc> getCertificateDocs() {
		return certificateDocs;
	}

	public void setCertificateDocs(List<CertificateDoc> certificateDocs) {
		this.certificateDocs = certificateDocs;
	}
}
