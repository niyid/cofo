package com.vasworks.cofo.struts.agent;

import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.vasworks.cofo.model.CooApplication;
import com.vasworks.cofo.model.CooApplication.CooStatus;
import com.vasworks.cofo.model.CooApplication.CooType;
import com.vasworks.cofo.model.Country;
import com.vasworks.cofo.model.CountryState;
import com.vasworks.cofo.model.GeoStreet;
import com.vasworks.cofo.model.LocalGovArea;
import com.vasworks.cofo.model.Occupation;
import com.vasworks.cofo.model.UserTitle;
import com.vasworks.cofo.model.VitalInformation.Gender;
import com.vasworks.cofo.model.VitalInformation.MaritalStatus;
import com.vasworks.cofo.model.VitalInformationId.IdType;
import com.vasworks.cofo.struts.AgentAction;

public class CooApplicationAction extends AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3044965041941202832L;
	
	private Long applicationId;
	
	private CooApplication entity;
	
	private String message;
	
	private List<CooApplication> applications;
	
	private CooType cooType;
	
	private CooStatus cooStatus;
	
	private Date filterDate;
	
	private Long countryStateId;
	
	private Long lgaId;
	
	private Long streetId;
	
	private String nationalityId;
	
	private Long occupationId;
	
	private Long titleId;

	@Override
	public void prepare() {
	}

	@Override
	public String execute() {
		session.put("application_id", null);
		
		return super.execute();
	}
	
	public String load() {
		if(applicationId != null) {
			if(entity == null) {
				entity = (CooApplication) agentService.find(applicationId, CooApplication.class);
			}
		}
		return SUCCESS;
	}
	
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "entity.description", message = "'Description' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.id.idValue", message = "'Identity Number' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.lastName", message = "'Surname' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.firstName", message = "'First Name' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.contactInfo.primaryPhone", message = "'Primary Cellphone' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.contactInfo.primaryEmail", message = "'Primary Email' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.geoAddress.areaName1", message = "'Address Area 1' is required."),
			@RequiredStringValidator(fieldName = "entity.vitalInformation.geoAddress.propertyNumber", message = "'Property Number' is required.")
		},
		requiredFields = {
				@RequiredFieldValidator(fieldName = "entity.cooType", message = "'C-of-O Type' is required."),
				@RequiredFieldValidator(fieldName = "entity.vitalInformation.id.idType", message = "'Identity Type' is required."),
				@RequiredFieldValidator(fieldName = "entity.vitalInformation.birthDate", message = "'Birth Date' is required."),
				@RequiredFieldValidator(fieldName = "entity.vitalInformation.gender", message = "'Gender' is required."),
				@RequiredFieldValidator(fieldName = "entity.vitalInformation.maritalStatus", message = "'Marital Status' is required."),
				@RequiredFieldValidator(fieldName = "nationalityId", message = "'Nationality' is required."),
				@RequiredFieldValidator(fieldName = "occupationId", message = "'Occupation' is required."),
				@RequiredFieldValidator(fieldName = "countryStateId", message = "'State of Origin' is required."),
				@RequiredFieldValidator(fieldName = "lgaId", message = "'LGA' is required."),
				@RequiredFieldValidator(fieldName = "streetId", message = "'Street' is required.")
		}
	)
	public String save() {
		applications = agentService.saveCooApplication(entity, nationalityId, streetId, occupationId, getUser().getUsername());
		
		entity = new CooApplication();
		applicationId = null;
		
		session.remove("application_id");
		
		addActionMessage("Application process successfully saved.");
		
		return SUCCESS;
	}
	
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "message", message = "'Message' is required.")
		}
	)
	public String reject() {
		applications = agentService.rejectCooApplication(applicationId, message, getUser().getUsername());
		
		entity = new CooApplication();
		applicationId = null;
		message = null;
		
		session.remove("application_id");
		
		addActionMessage("Application rejected.");
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
	}
	
	public String select() {
		if(applicationId != null) {
			entity = (CooApplication) agentService.find(applicationId, CooApplication.class);
			
			List<String> errors = agentService.validateApplication(applicationId);
			
			setActionErrors(errors);
			
			session.put("application_id", applicationId);
		}
		return SUCCESS;
	}
	
	public String list() {
		applications = (List<CooApplication>) agentService.listApplications(filterDate, cooType, cooStatus, getUser().getUsername());
		
		return SUCCESS;
	}
	
	public Long getCooApplicationId() {
		return applicationId;
	}

	public void setCooApplicationId(Long processFlowItemId) {
		this.applicationId = processFlowItemId;
	}
	
	public CooApplication getEntity() {
		return entity;
	}

	public void setEntity(CooApplication entity) {
		this.entity = entity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public CooType getCooType() {
		return cooType;
	}

	public void setCooType(CooType cooType) {
		this.cooType = cooType;
	}

	public CooStatus getCooStatus() {
		return cooStatus;
	}

	public void setCooStatus(CooStatus cooStatus) {
		this.cooStatus = cooStatus;
	}

	public Date getFilterDate() {
		return filterDate;
	}

	public void setFilterDate(Date filterDate) {
		this.filterDate = filterDate;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Long getCountryStateId() {
		return countryStateId;
	}

	public void setCountryStateId(Long countryStateId) {
		this.countryStateId = countryStateId;
	}

	public Long getLgaId() {
		return lgaId;
	}

	public void setLgaId(Long lgaId) {
		this.lgaId = lgaId;
	}

	public Long getStreetId() {
		return streetId;
	}

	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}

	public String getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(String countryCode) {
		this.nationalityId = countryCode;
	}

	public Long getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Long occupationId) {
		this.occupationId = occupationId;
	}

	public Long getTitleId() {
		return titleId;
	}

	public void setTitleId(Long titleId) {
		this.titleId = titleId;
	}

	public List<CooApplication> getApplications() {
		return applications;
	}
	
	public CooType[] getCooTypeLov() {
		return CooType.values();
	}
	
	public Gender[] getGenderLov() {
		return Gender.values();
	}
	
	public MaritalStatus[] getMaritalStatusLov() {
		return MaritalStatus.values();
	}
	
	public IdType[] getIdTypeLov() {
		return IdType.values();
	}
	
	public List<?> getCountryLov() {
		return agentService.listEntity(Country.class);
	}
	
	public List<?> getCountryStateLov() {
		return agentService.listEntity(CountryState.class);
	}
	
	public List<?> getLgaLov() {
		return agentService.listEntity(LocalGovArea.class);
	}
	
	public List<?> getStreetLov() {
		return agentService.listEntity(GeoStreet.class);
	}

	public List<?> getOccupationLov() {
		return agentService.listEntity(Occupation.class);
	}

	public List<?> getTitleLov() {
		return agentService.listEntity(UserTitle.class);
	}
}
