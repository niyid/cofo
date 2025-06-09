package com.vasworks.cofo.struts.agent;

import java.util.Date;
import java.util.List;

import com.vasworks.cofo.model.CooApplication;
import com.vasworks.cofo.model.CooApplication.CooStatus;
import com.vasworks.cofo.model.CooApplication.CooType;
import com.vasworks.cofo.struts.AgentAction;

public class SearchApplicationAction extends AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3044965041941202832L;

	private String param;

	private Date searchStartDate;
	
	private boolean searchExport2Excel;
	
	private List<CooApplication> results;

	private Date filterDate;

	private CooType cooType;

	private CooStatus cooStatus;
	
	@Override
	public void prepare() {
	}

	@Override
	public String execute() {
		
		return SUCCESS;
	}
	
	public String search() {
		if(searchStartDate == null) {
			searchStartDate = new Date();
		}
		
		results = agentService.listApplications(filterDate != null ? filterDate : new Date(), cooType, cooStatus, getUser().getUsername());
		
		return SUCCESS;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String captureType) {
		this.param = captureType;
	}

	public Date getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(Date startDate) {
		this.searchStartDate = startDate;
	}
	
	public boolean isSearchExport2Excel() {
		return searchExport2Excel;
	}

	public void setSearchExport2Excel(boolean export2Excel) {
		this.searchExport2Excel = export2Excel;
	}

	public Date getFilterDate() {
		return filterDate;
	}

	public void setFilterDate(Date filterDate) {
		this.filterDate = filterDate;
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

	public void setResults(List<CooApplication> results) {
		this.results = results;
	}

	public List<CooApplication> getResults() {
		return results;
	}
}
