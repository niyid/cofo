package com.vasworks.cofo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ProcessFlowGisItem extends ProcessFlowItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6150604235704756765L;
	
	private GeoPolygon sitePolygon;
	
	private ServiceAgent fieldAgent;
	
	private Date surveyDate;

	@Temporal(TemporalType.DATE)
	public Date getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}

	@ManyToOne
	public ServiceAgent getFieldAgent() {
		return fieldAgent;
	}

	public void setFieldAgent(ServiceAgent fieldAgent) {
		this.fieldAgent = fieldAgent;
	}

	@OneToOne
	public GeoPolygon getSitePolygon() {
		return sitePolygon;
	}

	public void setSitePolygon(GeoPolygon sitePolygon) {
		this.sitePolygon = sitePolygon;
	}
}
