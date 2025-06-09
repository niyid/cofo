package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.vasworks.cofo.model.CooApplication.CooType;

@Entity
public class ProcessFlowTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CooType id;
	
	private String description;
	
	private ProcessFlowTemplateItem processStart;
	
	private List<CertificateType> requiredCerts;
	
	@Id
	public CooType getId() {
		return id;
	}

	public void setId(CooType id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public ProcessFlowTemplateItem getProcessStart() {
		return processStart;
	}

	public void setProcessStart(ProcessFlowTemplateItem processStart) {
		this.processStart = processStart;
	}

	@ManyToMany
	public List<CertificateType> getRequiredCerts() {
		return requiredCerts;
	}

	public void setRequiredCerts(List<CertificateType> requiredCerts) {
		this.requiredCerts = requiredCerts;
	}
}
