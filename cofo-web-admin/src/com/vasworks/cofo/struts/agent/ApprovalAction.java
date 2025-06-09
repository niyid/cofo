package com.vasworks.cofo.struts.agent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.vasworks.cofo.model.CertificateDoc;
import com.vasworks.cofo.model.GeoPolygon;
import com.vasworks.cofo.model.ProcessFlowGisItem;
import com.vasworks.cofo.model.ProcessFlowItem;
import com.vasworks.cofo.struts.AgentAction;
import com.vividsolutions.jts.geom.Coordinate;

public class ApprovalAction extends AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3044965041941202832L;
	
	private Long processFlowItemId;
	
	private String signatureData;
	
	private boolean signed;
	
	private String message;
	
	private List<ProcessFlowItem> processFlowItems;
	
	private ProcessFlowItem entity;
	
	private List<CertificateDoc> certDocs;
	
	@Override
	public void prepare() {
		if(processFlowItemId != null) {
			entity = (ProcessFlowItem) agentService.find(processFlowItemId, ProcessFlowItem.class);
		}
	}

	@Override
	public String execute() {
		session.put("process_flow_item_id", null);
		
		return super.execute();
	}
	
	public String load() {
	
		return SUCCESS;
	}
	
	public String save() {
		if(!signed) {
			addActionError("Approver must append his signatureData.");
		} else {
			processFlowItems = agentService.acceptProcessFlowItem(processFlowItemId, signatureData, getUser().getUsername());
			
			processFlowItemId = null;
			
			session.remove("process_flow_item_id");
			
			addActionMessage("Approval successfully completed.");
		}
		
		return SUCCESS;
	}
	
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "message", message = "'Message' is required.")
		}
	)
	public String reject() {
		processFlowItems = agentService.rejectProcessFlowItem(processFlowItemId, message, getUser().getUsername());
		
		processFlowItemId = null;
		message = null;
		session.remove("process_flow_item_id");
		
		addActionMessage("Application process successfully saved.");
		
		return SUCCESS;
	}
	
	public String select() {
		if(processFlowItemId != null) {
			session.put("process_flow_item_id", processFlowItemId);
			Long applicationId = agentService.fetchApplicationIdByFlowItem(processFlowItemId);
			
			if(applicationId != null) {
				certDocs = agentService.listCertificates(applicationId);
			}
		}
		return SUCCESS;
	}
	
	public String list() {
		processFlowItems = (List<ProcessFlowItem>) agentService.listProcessFlows(getUser().getUsername());
		
		return SUCCESS;
	}
	
	public Long getProcessFlowItemId() {
		return processFlowItemId;
	}

	public void setProcessFlowItemId(Long processFlowItemId) {
		this.processFlowItemId = processFlowItemId;
	}

	public String getSignatureData() {
		return signatureData;
	}

	public void setSignatureData(String signature) {
		this.signatureData = signature;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public List<ProcessFlowItem> getProcessFlowItems() {
		return processFlowItems;
	}

	public ProcessFlowItem getEntity() {
		return entity;
	}

	public List<CertificateDoc> getCertDocs() {
		return certDocs;
	}

	public String getPolygonScript() {
		StringBuilder b = new StringBuilder();
		if(entity instanceof ProcessFlowGisItem) {
			ProcessFlowGisItem gisItem = (ProcessFlowGisItem) entity;
			List<GeoPolygon> streetPolygons = null;
			if(gisItem.getSitePolygon().getGeoStreet() != null) {
				streetPolygons = agentService.listStreetPolygons(gisItem.getSitePolygon().getGeoStreet().getId());
			} else {
				streetPolygons = new ArrayList<GeoPolygon>();
				streetPolygons.add(gisItem.getSitePolygon());
			}

			b.append("\n\n");

			if (streetPolygons != null && !streetPolygons.isEmpty()) {
				b.append("function drawAllPolygons(clientPosition) {\n");
				b.append("var mapObject = document.getElementById('mapCanvas');\n");
				b.append("var mapOptions = {\n");
				b.append("center: clientPosition\n");
				b.append("};\n");
				b.append("var buildingCoords = [];\n");
				b.append("var buildingPolygon = [];\n");
				b.append("var map = new google.maps.Map(mapObject, mapOptions);\n");
				b.append("var latlngBounds = new google.maps.LatLngBounds();");

				String color = null;
				Date surveyDate;
				for (GeoPolygon polygonItem : streetPolygons) {
					boolean first = true;
					b.append("buildingCoords.push([\n");
					for (Coordinate coord : polygonItem.getBuildingPolygon().getCoordinates()) {
						if (first) {
							first = false;
						} else {
							b.append(",\n");
						}
						b.append("new google.maps.LatLng(" + coord.x + "," + coord.y + ")\n");
					}
					b.append("]);\n");
					for (Coordinate coord : polygonItem.getBuildingPolygon().getCoordinates()) {
						b.append("latlngBounds.extend(new google.maps.LatLng(" + coord.x + "," + coord.y + "));\n");
					}
					
					surveyDate = agentService.fetchPolygonSurveyDate(polygonItem.getId());

					if (surveyDate != null) {
						// Green
						color = "'#00FF00'";
					} else if (surveyDate == null) {
						// Red
						color = "'#FF0000'";
					}
					if (polygonItem.getId().equals(session.get("item_id"))) {
						color = "'#0000FF'";
					}

					b.append("buildingPolygon.push(new google.maps.Polygon({\n");
					b.append("paths: buildingCoords[buildingCoords.length - 1],\n");
					b.append("strokeColor: ");
					b.append(color);
					b.append(",\n");
					b.append("strokeOpacity: 0.8,\n");
					b.append("strokeWeight: 2,\n");
					b.append("fillColor: ");
					b.append(color);
					b.append(",\n");
					b.append("fillOpacity: 0.35\n");
					b.append("}));\n");

					b.append("buildingPolygon[buildingPolygon.length - 1].setMap(map);\n");
				}
				b.append("map.fitBounds(latlngBounds);\n");

				b.append("}\n");
			}		
		}


		return b.toString();
	}
}
