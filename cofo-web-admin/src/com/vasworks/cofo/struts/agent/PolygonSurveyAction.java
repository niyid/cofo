package com.vasworks.cofo.struts.agent;

import java.io.File;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.vasworks.cofo.struts.AgentAction;

public class PolygonSurveyAction extends AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3044965041941202832L;

	private File[] uploads;

	private String[] uploadFileNames;

	private String[] uploadContentTypes;

	private String msg;
	
	private Long streetId;
	
	private String description;

	private String capturedPolygons;

	@Override
	public void prepare() {
	}

	@Override
	public String execute() {

		Long itemId = agentService.fetchNextPolygonItemId(getUser().getUsername());

		String page = SUCCESS;
		if (itemId != null) {
			session.put("item_id", itemId);
		} else {
			addActionMessage("You do not have a running schedule in the selected district for today.");
			page = ERROR;
		}

		return page;
	}

	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "upload", message = "'Elevation View' and 'Axonometric View' photos are required.") }, regexFields = {
			@RegexFieldValidator(fieldName = "floors", expression = "^(\\d+[,\\s])*\\d+$", message = "'Floors' is in the wrong format."),
			@RegexFieldValidator(fieldName = "wings", expression = "^(\\d+[,\\s])*\\d+$", message = "'Wings' is in the wrong format."),
			@RegexFieldValidator(fieldName = "stairs", expression = "^(\\d+[,\\s])*\\d+$", message = "'Stairs' is in the wrong format.") })
	public String save() {
		Long itemId = null;
		String page = SUCCESS;

		try {
			
			itemId = agentService.savePolygonSurvey((Long) session.get("item_id"), streetId, description, uploads, capturedPolygons, getUser().getUsername());
			if (itemId != null) {
				session.put("item_id", itemId);
				addActionMessage("Polygon successfully surveyed.");
			} else {
				page = "finished";
			}
		} catch (Exception e) {
			addActionError(e.getMessage());
		}

		return page;
	}

	public String list() {

		return SUCCESS;
	}

	public File[] getUpload() {
		return this.uploads;
	}

	public void setUpload(File[] upload) {
		this.uploads = upload;
	}

	public String[] getUploadFileName() {
		return this.uploadFileNames;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileNames = uploadFileName;
	}

	public String[] getUploadContentType() {
		return this.uploadContentTypes;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentTypes = uploadContentType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getStreetId() {
		return streetId;
	}

	public void setStreetId(Long applicationId) {
		this.streetId = applicationId;
	}

	public String getCapturedPolygons() {
		return capturedPolygons;
	}

	public void setCapturedPolygons(String capturedPolygons) {
		this.capturedPolygons = capturedPolygons;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
