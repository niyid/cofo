package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DocumentFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7115694715254622231L;
	
	private Long id;
	
	private String fileName;
	
	private String mimeType;

	private String description;
	
	private byte[] rawContent;

	private Date issueDate;
	
	private Date expirationDate;
	
	private LicensingAuthority licensingAuthority;
	
	private CertificateDoc certificateDoc;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Lob
	@Column(length = 15000)
	@Basic(fetch = FetchType.LAZY)
	public byte[] getRawContent() {
		return rawContent;
	}

	public void setRawContent(byte[] rawContent) {
		this.rawContent = rawContent;
	}

	@Temporal(TemporalType.DATE)
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date approvalDate) {
		this.issueDate = approvalDate;
	}

	@ManyToOne
	public LicensingAuthority getLicensingAuthority() {
		return licensingAuthority;
	}

	public void setLicensingAuthority(LicensingAuthority licensingAuthority) {
		this.licensingAuthority = licensingAuthority;
	}

	@ManyToOne
	public CertificateDoc getCertificateDoc() {
		return certificateDoc;
	}

	public void setCertificateDoc(CertificateDoc certificateDoc) {
		this.certificateDoc = certificateDoc;
	}
}
