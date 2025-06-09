package com.vasworks.cofo.struts.agent;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.vasworks.cofo.model.CertificateDoc;
import com.vasworks.cofo.model.DocumentFile;
import com.vasworks.cofo.model.LicensingAuthority;
import com.vasworks.cofo.struts.AgentAction;


public class CertificateDocAction extends AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -978288662080145465L;

	private Long applicationId;
	
	private Long certificateId;
	
	private Long certFileId;

	private Long authorityId;
	
	private Long certificateTypeId;
	
	private CertificateDoc entity;
	
	private List<CertificateDoc> certificates;
	
	private List<DocumentFile> docFiles;
	
	private File uploadedFile;
	
	private String uploadedFileContentType;
	
	private String uploadedFileFileName;

	private String certificateDesc;
	
	private Date issueDate;
	
	private Date expirationDate;
	
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return super.execute();
	}
	
	@Validations(
		requiredFields = {
			@RequiredFieldValidator(fieldName = "expirationDate", message = "'Expiration Date' is required."),
			@RequiredFieldValidator(fieldName = "issueDate", message = "'Issue Date' is required."),
			@RequiredFieldValidator(fieldName = "uploadedFile", message = "'Document File' is required."),
			@RequiredFieldValidator(fieldName = "certificateTypeId", message = "'Type' is required."),
			@RequiredFieldValidator(fieldName = "authorityId", message = "'Licensing Authority' is required.")
		},
		requiredStrings = {
			@RequiredStringValidator(fieldName = "certificateDesc", message = "'Description' is required.")	
		}
	)
	public String save() {
		agentService.saveCertificateDoc(certificateId, certFileId, certificateTypeId, authorityId, certificateDesc, uploadedFile, uploadedFileFileName, issueDate, expirationDate, getUser().getUsername());			
		addActionMessage("Certificate successfully saved.");
		
		docFiles = agentService.listCertDocFiles(certificateId);
		
		return SUCCESS;
	}
	
	public String list() {
		certificates = agentService.listCertificates(applicationId);
		
		return SUCCESS;
	}
	
	public String listDocs() {
		docFiles = agentService.listCertDocFiles(certificateId);
		
		return SUCCESS;
	}
	
	public String select() {
		if(certificateId != null) {
			entity = (CertificateDoc) agentService.find(certificateId, CertificateDoc.class);
			
			session.put("certificate_id", certificateId);
		}
		return SUCCESS;
	}
	
	public String initNew() {
		entity = new CertificateDoc();
		
		session.put("certificate_id", null);
		
		return SUCCESS;
	}
	
	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Long getCertificateId() {
		return certificateId;
	}
	
	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}
	
	public Long getCertFileId() {
		return certFileId;
	}

	public void setCertFileId(Long certFileId) {
		this.certFileId = certFileId;
	}

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public Long getCertificateTypeId() {
		return certificateTypeId;
	}

	public void setCertificateTypeId(Long certificateTypeId) {
		this.certificateTypeId = certificateTypeId;
	}

	public CertificateDoc getEntity() {
		return entity;
	}
	
	public void setEntity(CertificateDoc entity) {
		this.entity = entity;
	}
	
	public List<CertificateDoc> getCertificates() {
		return certificates;
	}

	public List<DocumentFile> getDocFiles() {
		return docFiles;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getUploadedFileContentType() {
		return uploadedFileContentType;
	}

	public void setUploadedFileContentType(String uploadedFileContentType) {
		this.uploadedFileContentType = uploadedFileContentType;
	}

	public String getUploadedFileFileName() {
		return uploadedFileFileName;
	}

	public void setUploadedFileFileName(String uploadedFileFileName) {
		this.uploadedFileFileName = uploadedFileFileName;
	}

	public String getCertificateDesc() {
		return certificateDesc;
	}

	public void setCertificateDesc(String certificateDesc) {
		this.certificateDesc = certificateDesc;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public List<?> getLicensingAuthorityLov() {
		return agentService.listEntity(LicensingAuthority.class);
	}
}
