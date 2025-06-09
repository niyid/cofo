package com.vasworks.cofo.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import com.vasworks.cofo.model.CertificateDoc;
import com.vasworks.cofo.model.CooApplication;
import com.vasworks.cofo.model.Country;
import com.vasworks.cofo.model.Ethnicity;
import com.vasworks.cofo.model.LocalGovArea;
import com.vasworks.cofo.model.GeoPolygon;
import com.vasworks.cofo.model.CountryState;
import com.vasworks.cofo.model.DocumentFile;
import com.vasworks.cofo.model.ProcessFlowItem;
import com.vasworks.cofo.model.ProcessFlowQuery;
import com.vasworks.cofo.model.CooApplication.CooStatus;
import com.vasworks.cofo.model.CooApplication.CooType;

public interface AgentService {
	
    String JAI_STREAM_ACTION = "stream";
 
    String JAI_SUBSAMPLE_AVERAGE_ACTION = "SubsampleAverage";
    
    String JAI_ENCODE_ACTION = "encode";
    
    double EARTH_RADIUS = 6371;
    
    static final String LIST_COUNTRY_STATES = "select o from CountryState o where o.country.countryCode = :countryCode";

	static final String FILTER_COUNTRY_STATE = "select o.id, o.stateName from CountryState o where o.country.countryCode = :countryCode and o.stateName like :param";
	
	static final String FILTER_OCCUPATION = "select o.id, o.description from Occupation o where o.description like :param";

    
	/**
	 * 
	 * @param entityId
	 * @param entityClass
	 * @return
	 * @throws NoResultException
	 */
	Object find(Object entityId, Class<?> entityClass) throws NoResultException;
	
	/**
	 * 
	 * @param entityClass
	 * @return
	 * @throws NoResultException
	 */
	List<?> listEntity(Class<?> entityClass) throws NoResultException;
    
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	String authenticate(String emailAddress, String password);
	
	/**
	 * 
	 * @param emailAddress
	 * @param fullName
	 * @param password
	 * @return
	 */
	String register(String emailAddress, String fullName, String password);
	
	/**
	 * 
	 * @param emailAddress
	 * @return
	 */
	byte[] findProfilePhoto(String emailAddress);

	/**
	 * 
	 * @param certificateId
	 * @param certFileId
	 * @param certificateTypeId
	 * @param authorityId
	 * @param certificateDescription
	 * @param certificateFile
	 * @param certificateFileName
	 * @param issueDate
	 * @param expirationDate
	 * @param userId
	 * @return
	 */
	List<String> saveCertificateDoc(Long certificateId, Long certFileId, Long certificateTypeId, Long authorityId, String certificateDescription, File certificateFile, String certificateFileName, Date issueDate, Date expirationDate, String userId);

	/**
	 * 
	 * @param processFlowItemId
	 * @param signature
	 * @param username
	 * @return
	 */
	List<ProcessFlowItem> acceptProcessFlowItem(Long processFlowItemId, String signature, String username);

	/**
	 * 
	 * @param processFlowItemId
	 * @param message
	 * @param username
	 * @return
	 */
	List<ProcessFlowItem> rejectProcessFlowItem(Long processFlowItemId, String message, String username);
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	List<ProcessFlowItem> listProcessFlows(String username);
	
	/**
	 * 
	 * @param paramDate
	 * @param type
	 * @param status
	 * @param username
	 * @return
	 */
	List<CooApplication> listApplications(Date paramDate, CooType type, CooStatus status, String username);

	/**
	 * 
	 * @param applicationId
	 * @return
	 */
	List<CertificateDoc> listCertificates(Long applicationId);
	
	/**
	 * 
	 * @param certificateId
	 * @return
	 */
	List<DocumentFile> listCertDocFiles(Long certificateId);

	/**
	 * 
	 * @param application
	 * @param countryCode
	 * @param streetId
	 * @param occupationId
	 * @param username
	 * @return
	 */
	List<CooApplication> saveCooApplication(CooApplication application, String countryCode, Long streetId, Long occupationId, String username);

	/**
	 * 
	 * @param applicationId
	 * @param reason
	 * @param username
	 * @return
	 */
	List<CooApplication> rejectCooApplication(Long applicationId, String reason, String username);
	
	/**
	 * 
	 * @param applicationId
	 * @param reason
	 * @param username
	 * @return
	 */
	List<CooApplication> cancelCooApplication(Long applicationId, String reason, String username);

	/**
	 * 
	 * @return
	 */
	Country fetchDefaultCountry();

	/**
	 * 
	 * @return
	 */
	List<CountryState> listDefaultCountryStates();

	/**
	 * 
	 * @param countryCode
	 * @return
	 */
	List<CountryState> listCountryStates(String countryCode);
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	String autocompleteCountryStates(String param);
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	String autocompleteOccupations(String param);
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	String autocompleteNationalities(String param);

	/**
	 * 
	 * @param param
	 * @param stateId 
	 * @return
	 */
	String autocompleteLgas(String param, Long stateId);
	/**
	 * 
	 * @param param
	 * @return
	 */
	String autocompleteEthnicities(String param);

	/**
	 * 
	 * @param param
	 * @param stateId
	 * @return
	 */
	List<LocalGovArea> searchLgas(String param, Long stateId);

	/**
	 * 
	 * @param param
	 * @return
	 */
	List<Ethnicity> searchEthnicities(String param);

	/**
	 * 
	 * @param streetId
	 * @return
	 */
	List<GeoPolygon> listStreetPolygons(Long streetId);

	/**
	 * 
	 * @param polygonId
	 * @return
	 */
	Date fetchPolygonSurveyDate(Long polygonId);

	/**
	 * 
	 * @param username
	 * @return
	 */
	Long fetchNextPolygonItemId(String username);

	/**
	 * 
	 * @param gisItemId
	 * @param streetId
	 * @param description
	 * @param uploads
	 * @param capturedPolygons
	 * @param username
	 * @return
	 */
	Long savePolygonSurvey(Long gisItemId, Long streetId, String description, File[] uploads, String capturedPolygons, String username);
	
	/**
	 * 
	 * @param applicationId
	 * @param chargeId
	 * @param username
	 */
	void payCharge(Long applicationId, Long chargeId, String username);
	
	/**
	 * 
	 * @param applicationId
	 * @return
	 */
	List<String> validateApplication(Long applicationId);
	
	/**
	 * 
	 * @param processFlowItemId
	 * @return
	 */
	List<ProcessFlowQuery> listQueryMessages(Long processFlowItemId);
	
	/**
	 * 
	 * @param processFlowItemId
	 * @return
	 */
	Long fetchApplicationIdByFlowItem(Long processFlowItemId);
}
