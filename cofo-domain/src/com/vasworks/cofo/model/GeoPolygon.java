package com.vasworks.cofo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Polygon;

@Entity
public class GeoPolygon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4515344049418019627L;

	private Long id;
	
	private String description;
	
	private Polygon buildingPolygon;
	
	private GeoStreet geoStreet;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Type(type="org.hibernate.spatial.GeometryType")
	@Column(nullable = false)
	public Polygon getBuildingPolygon() {
		return buildingPolygon;
	}

	public void setBuildingPolygon(Polygon buildingGeometry) {
		this.buildingPolygon = buildingGeometry;
	}

	@ManyToOne
	public GeoStreet getGeoStreet() {
		return geoStreet;
	}

	public void setGeoStreet(GeoStreet geoStreet) {
		this.geoStreet = geoStreet;
	}
	
}
