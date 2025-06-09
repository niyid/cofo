package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CertificateDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532681192674517961L;
	
	private Long id;
	
	private CertificateType certificateType;
	
	private CooApplication cooApplication;
	
	private List<DocumentFile> certificateFiles;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	@ManyToOne
	public CooApplication getCooApplication() {
		return cooApplication;
	}

	public void setCooApplication(CooApplication cooApplication) {
		this.cooApplication = cooApplication;
	}

	@OneToMany(mappedBy = "certificateDoc")
	public List<DocumentFile> getCertificateFiles() {
		return certificateFiles;
	}

	public void setCertificateFiles(List<DocumentFile> certificateFiles) {
		this.certificateFiles = certificateFiles;
	}

}
