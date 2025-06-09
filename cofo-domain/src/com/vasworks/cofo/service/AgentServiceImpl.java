package com.vasworks.cofo.service;

import java.awt.RenderingHints;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.RenderedOp;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.sun.media.jai.codec.SeekableStream;
import com.vasworks.cofo.model.AccountTransaction;
import com.vasworks.cofo.model.AccountTransaction.TransactionType;
import com.vasworks.cofo.model.CertificateDoc;
import com.vasworks.cofo.model.CertificateType;
import com.vasworks.cofo.model.CooApplication;
import com.vasworks.cofo.model.CooApplication.CooStatus;
import com.vasworks.cofo.model.CooApplication.CooType;
import com.vasworks.cofo.model.CooCharge;
import com.vasworks.cofo.model.Country;
import com.vasworks.cofo.model.DocumentFile;
import com.vasworks.cofo.model.Ethnicity;
import com.vasworks.cofo.model.LocalGovArea;
import com.vasworks.cofo.model.Occupation;
import com.vasworks.cofo.model.GeoPolygon;
import com.vasworks.cofo.model.GeoStreet;
import com.vasworks.cofo.model.CountryState;
import com.vasworks.cofo.model.LicensingAuthority;
import com.vasworks.cofo.model.Personnel;
import com.vasworks.cofo.model.ProcessFlow;
import com.vasworks.cofo.model.ProcessFlowGisItem;
import com.vasworks.cofo.model.ProcessFlowItem;
import com.vasworks.cofo.model.ProcessFlowQuery;
import com.vasworks.cofo.model.ProcessFlowTemplate;
import com.vasworks.cofo.model.ProcessFlowTemplateItem;
import com.vasworks.cofo.model.ServiceAgent;
import com.vasworks.cofo.model.ServiceAgent.UserStatus;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

@Transactional
public class AgentServiceImpl implements AgentService {

	private static HashMap<String, String> IMAGE_ENCODING_TYPE_MAP = new HashMap<String, String>();

	private static List<String> PROP_NAMES_ETHNICITY = new ArrayList<String>();
	private static List<String> PROP_NAMES_EDUCATIONAL_LEVEL = new ArrayList<String>();
	private static List<String> PROP_NAMES_LGA = new ArrayList<String>();
	private static List<String> PROP_NAMES_COUNTRY = new ArrayList<String>();

	static {
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");

		IMAGE_ENCODING_TYPE_MAP.put("JPEG", "image/jpeg:jpg");
		IMAGE_ENCODING_TYPE_MAP.put("GIF", "image/gif:gif");
		IMAGE_ENCODING_TYPE_MAP.put("TIFF", "image/tiff:tif");
		IMAGE_ENCODING_TYPE_MAP.put("PNG", "image/png:png");
		IMAGE_ENCODING_TYPE_MAP.put("BMP", "image/bmp:bmp");

		PROP_NAMES_ETHNICITY.add("description");

		PROP_NAMES_EDUCATIONAL_LEVEL.add("description");

		PROP_NAMES_LGA.add("lgaName");

		PROP_NAMES_COUNTRY.add("countryName");
		PROP_NAMES_COUNTRY.add("countryCode");
	}

	public static final Log LOG = LogFactory.getLog(AgentServiceImpl.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	private String smsSenderUrl;

	private String smsSenderUser;

	private String smsSenderPass;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setSmsSenderUrl(String smsSenderUrl) {
		this.smsSenderUrl = smsSenderUrl;
	}

	public void setSmsSenderUser(String smsSenderUser) {
		this.smsSenderUser = smsSenderUser;
	}

	public void setSmsSenderPass(String smsSenderPass) {
		this.smsSenderPass = smsSenderPass;
	}

	public String getSmsSenderUrl() {
		return smsSenderUrl;
	}
	
	@Override
	public Object find(Object entityId, Class<?> entityClass) throws NoResultException {
		return entityManager.find(entityClass, entityId);
	}

	@Override
	public List<?> listEntity(Class<?> entityClass) throws NoResultException {
		Query q = entityManager.createQuery("select o from " + entityClass.getName() + " o");
		
		return q.getResultList();
	}

	@Override
	public String authenticate(String emailAddress, String password) {
		LOG.debug("authenticate - " + emailAddress + " " + password);

		String msg = "";
		ServiceAgent userProfile = entityManager.find(ServiceAgent.class, emailAddress);

		if (userProfile != null) {
			if (!UserStatus.DISABLED.equals(userProfile.getStatus())) {
				if (userProfile.getPasswordHash() != null) {
					if (!userProfile.getPasswordHash().equals(hashPassword(password, userProfile.getPasswordSalt()))) {
						msg = "error_incorrect_password";
					} else {
						msg = String.valueOf(userProfile.getUsername());
					}
				}
			} else {
				msg = "error_user_disabled";
			}
		} else {
			msg = "error_no_such_user";
		}

		return msg;
	}

	@Override
	public String register(String emailAddress, String fullName, String password) {
		LOG.info("register - " + emailAddress + " " + fullName + " " + password);

		String msg = "";
		ServiceAgent profile = entityManager.find(ServiceAgent.class, emailAddress);
		if (profile == null) {
			try {
				ServiceAgent userProfile = new ServiceAgent();
				userProfile.setUsername(emailAddress);
				userProfile.setFullName(fullName);
				userProfile.setStatus(UserStatus.ENABLED);

				userProfile.setPasswordSalt(getSalt());
				userProfile.setPasswordHash(hashPassword(password, userProfile.getPasswordSalt()));

				entityManager.persist(userProfile);
				entityManager.flush();
			} catch (Throwable e) {
				msg = "error_reg_failed";
				e.printStackTrace();
			}
		} else {
			msg = "error_user_exists";
		}
		LOG.info("register - " + msg);

		return msg;
	}

	@Override
	public byte[] findProfilePhoto(String emailAddress) {
		LOG.debug("findProfilePhoto - " + emailAddress);

		ServiceAgent userProfile = entityManager.find(ServiceAgent.class, emailAddress);
		return userProfile.getPhoto();
	}


	@Override
	public List<DocumentFile> listCertDocFiles(Long certificateId) {
		LOG.debug("listCertDocFiles - " + certificateId);
		
		CertificateDoc certDoc = entityManager.find(CertificateDoc.class, certificateId);
		
		return certDoc.getCertificateFiles();
	}
	
	@Override
	public List<String> saveCertificateDoc(Long certificateId, Long certFileId, Long certificateTypeId, Long authorityId, String certificateDescription,
			File certificateFile, String certificateFileName, Date issueDate, Date expirationDate, String userId) {
		LOG.debug("saveCertificateDoc - " + certificateId + " " + userId);

		CertificateDoc certificateDoc = entityManager.find(CertificateDoc.class, certificateId);
		
		if (certificateId == null) {
			entityManager.persist(certificateDoc);
		}
		if (certFileId == null) {
			entityManager.persist(createDocumentFile(certificateDoc, certificateFile, certificateFileName, certificateDescription,
					authorityId, issueDate, expirationDate));

		} else {
			DocumentFile docFile = entityManager.find(DocumentFile.class, certFileId);
			entityManager.merge(updateDocumentFile(docFile, certificateFile, certificateFileName, certificateDescription,
					authorityId, issueDate, expirationDate));
		}
		certificateDoc.setCertificateType(entityManager.find(CertificateType.class, certificateTypeId));
		entityManager.merge(certificateDoc);

		entityManager.flush();
		
		List<String> errors = validateApplication(certificateDoc.getCooApplication().getId());
		
		if(errors.isEmpty()) {
			CooApplication application = certificateDoc.getCooApplication();
			ProcessFlow processFlow = application.getProcessFlow();
			ProcessFlowItem flowItem = processFlow.getProcessStart();
			while(flowItem != null) {
				flowItem.setDocReqCompleted(true);
				entityManager.merge(flowItem);
				flowItem = flowItem.getNextItem();
			}
		}
		
		return errors;
	}

	@Override
	public Country fetchDefaultCountry() {
		LOG.debug("fetchDefaultCountry()");
		return (Country) find("NG", Country.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CountryState> listCountryStates(String countryCode) {
		LOG.debug("listCountryStates - " + countryCode);
		Query q = entityManager.createQuery(LIST_COUNTRY_STATES);

		q.setParameter("countryCode", countryCode);

		return q.getResultList();
	}

	@Override
	public List<CountryState> listDefaultCountryStates() {
		LOG.debug("listDefaultCountryStates()");
		Country c = fetchDefaultCountry();
		return listCountryStates(c.getCountryCode());
	}

	@SuppressWarnings("unchecked")
	@Override
	public String autocompleteCountryStates(String param) {
		LOG.debug("autocompleteCountryStates - " + param);

		Country c = fetchDefaultCountry();

		Query q = entityManager.createQuery(FILTER_COUNTRY_STATE);

		q.setParameter("param", "%" + param + "%");
		q.setParameter("countryCode", c.getCountryCode());

		List<Object[]> results = q.getResultList();

		return convert2Json(results);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String autocompleteOccupations(String param) {
		LOG.debug("autocompleteOccupations - " + param);

		Query q = entityManager.createQuery(FILTER_OCCUPATION);

		q.setParameter("param", "%" + param + "%");

		List<Object[]> results = q.getResultList();

		return convert2Json(results);
	}

	@Override
	public String autocompleteEthnicities(String param) {
		LOG.debug("autocompleteEthnicities - " + param);

		List<Ethnicity> results = searchEthnicities(param);

		return convertEthnicity2Json(results);
	}

	@Override
	public String autocompleteLgas(String param, Long stateId) {
		LOG.debug("autocompleteLgas - " + param + " " + stateId);

		List<LocalGovArea> results = searchLgas(param, stateId);

		return convertLga2Json(results);
	}

	@Override
	public String autocompleteNationalities(String param) {
		LOG.debug("autocompleteNationalities - " + param);

		List<Country> results = searchCountries(param);

		return convertCountry2Json(results);
	}

	private String convert2Json(List<Object[]> results) {
		StringBuilder b = new StringBuilder("[");
		if (results != null) {
			boolean first = true;
			for (Object[] row : results) {
				if (!first) {
					b.append(",");
				}
				{
					first = false;
				}
				b.append("{\"value\":");
				b.append(row[0]);
				b.append(",\"label\":\"");
				b.append(row[1]);
				b.append("\"}");
			}
		}
		b.append("]");

		return b.toString();
	}

	private String convertEthnicity2Json(List<Ethnicity> results) {
		StringBuilder b = new StringBuilder("[");
		if (results != null) {
			boolean first = true;
			for (Ethnicity e : results) {
				if (!first) {
					b.append(",");
				}
				{
					first = false;
				}
				b.append("{\"value\":");
				b.append(e.getId());
				b.append(",\"label\":\"");
				b.append(e.getDescription());
				b.append("\"}");
			}
		}
		b.append("]");

		return b.toString();
	}

	private String convertLga2Json(List<LocalGovArea> results) {
		StringBuilder b = new StringBuilder("[");
		if (results != null) {
			boolean first = true;
			for (LocalGovArea e : results) {
				if (!first) {
					b.append(",");
				}
				{
					first = false;
				}
				b.append("{\"value\":");
				b.append(e.getId());
				b.append(",\"label\":\"");
				b.append(e.getLgaName());
				b.append("\"}");
			}
		}
		b.append("]");

		return b.toString();
	}

	private String convertCountry2Json(List<Country> results) {
		StringBuilder b = new StringBuilder("[");
		if (results != null) {
			boolean first = true;
			for (Country e : results) {
				if (!first) {
					b.append(",");
				}
				{
					first = false;
				}
				b.append("{\"value\":");
				b.append(e.getCountryCode());
				b.append(",\"label\":\"");
				b.append(e.getCountryName());
				b.append("\"}");
			}
		}
		b.append("]");

		return b.toString();
	}

	private DocumentFile createDocumentFile(CertificateDoc certificateDoc, File file, String fileName, String description, Long authorityId, Date issueDate, Date expirationDate) {
		DocumentFile docFile = new DocumentFile();
		docFile.setDescription(description);
		docFile.setFileName(fileName);
		docFile.setIssueDate(issueDate);
		docFile.setExpirationDate(expirationDate);
		docFile.setCertificateDoc(certificateDoc);
		docFile.setLicensingAuthority(entityManager.find(LicensingAuthority.class, authorityId));
		try {
			Path path = file.toPath();
			docFile.setMimeType(Files.probeContentType(path));
			docFile.setRawContent(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return docFile;
	}

	private DocumentFile updateDocumentFile(DocumentFile docFile, File file, String fileName, String description, Long authorityId, Date issueDate, Date expirationDate) {
		docFile.setDescription(description);
		docFile.setFileName(fileName);
		docFile.setIssueDate(issueDate);
		docFile.setExpirationDate(expirationDate);
		docFile.setLicensingAuthority(entityManager.find(LicensingAuthority.class, authorityId));
		try {
			Path path = file.toPath();
			docFile.setMimeType(Files.probeContentType(path));
			docFile.setRawContent(Files.readAllBytes(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return docFile;
	}
	
	private String hashPassword(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// Add password bytes to digest
			md.update(salt);
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
		}

		LOG.debug("Hashed: " + generatedPassword);

		return generatedPassword;
	}

	// Add salt
	private byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		// Create array for salt
		byte[] salt = new byte[16];
		// Get a random salt
		sr.nextBytes(salt);
		// return salt
		return salt;
	}

	protected String[] mimeTypeAndFileExtension(String encodingType) {
		LOG.debug("mimeTypeAndFileExtension - " + encodingType);

		String val = IMAGE_ENCODING_TYPE_MAP.get(encodingType);

		String[] pair = null;
		if (val != null) {
			pair = val.split(":");
		}

		LOG.debug("mimeTypeAndFileExtension - " + (pair != null ? Arrays.asList(pair) : pair));

		return pair;
	}

	protected byte[] resizeImageUsingEncoding(byte[] pImageData, int pMaxWidth, String encodingFormat) throws IOException {
		InputStream imageInputStream = new ByteArrayInputStream(pImageData);
		// read in the original image from an input stream
		SeekableStream seekableImageStream = SeekableStream.wrapInputStream(imageInputStream, true);
		RenderedOp originalImage = JAI.create(JAI_STREAM_ACTION, seekableImageStream);
		((OpImage) originalImage.getRendering()).setTileCache(null);
		int origImageWidth = originalImage.getWidth();
		// now resize the image
		double scale = 1.0;
		if (pMaxWidth > 0 && origImageWidth > pMaxWidth) {
			scale = (double) pMaxWidth / originalImage.getWidth();
		}
		ParameterBlock paramBlock = new ParameterBlock();
		paramBlock.addSource(originalImage); // The source image
		paramBlock.add(scale); // The xScale
		paramBlock.add(scale); // The yScale
		paramBlock.add(0.0); // The x translation
		paramBlock.add(0.0); // The y translation

		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		RenderedOp resizedImage = JAI.create(JAI_SUBSAMPLE_AVERAGE_ACTION, paramBlock, qualityHints);

		// lastly, write the newly-resized image to an output stream, in a
		// specific encoding
		ByteArrayOutputStream encoderOutputStream = new ByteArrayOutputStream();
		JAI.create(JAI_ENCODE_ACTION, resizedImage, encoderOutputStream, encodingFormat, null);
		// Export to Byte Array
		byte[] resizedImageByteArray = encoderOutputStream.toByteArray();
		return resizedImageByteArray;
	}

	protected byte[] resizeImage(byte[] originalImageData, int maxImageWidth, String encodingFormat) {
		byte[] finalImageData = originalImageData;
		if (originalImageData.length > 10240) {
			try {
				finalImageData = resizeImageUsingEncoding(originalImageData, maxImageWidth, encodingFormat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return finalImageData;
	}
	
	private ProcessFlowItem fetchPrevFlowItem(Long processFlowItemId) {
		LOG.debug("fetchPrevFlowItem - " + processFlowItemId);
		
		Query q = entityManager.createQuery("select o from ProcessFlowItem o where o.nextItem.id = :processFlowItemId");
		q.setParameter("processFlowItemId", processFlowItemId);
		
		ProcessFlowItem flowItem = null;
		try {
			flowItem = (ProcessFlowItem) q.getSingleResult();
		} catch(NoResultException e) {
			LOG.info("This is a root item: " + processFlowItemId);
		}
		
		return flowItem;
	}
	
	private Long fetchRootFlowItemId(Long processFlowItemId) {
		LOG.debug("fetchRootFlowItemId - " + processFlowItemId);
		
		ProcessFlowItem nextFlowItem = entityManager.find(ProcessFlowItem.class, processFlowItemId);
		
		ProcessFlowItem rootFlowItem = null;
		while(nextFlowItem != null) {
			rootFlowItem = nextFlowItem;
			nextFlowItem = fetchPrevFlowItem(nextFlowItem.getId());
		}
		
		return rootFlowItem.getId();
	}
	
	private Long fetchProcessFlowId(Long rootFlowItemId) {
		LOG.debug("fetchProcessFlowId - " + rootFlowItemId);
		
		Query q = entityManager.createQuery("select o.id from ProcessFlow o where o.processStart.id = :rootFlowItemId");
		q.setParameter("rootFlowItemId", rootFlowItemId);
		
		return (Long) q.getSingleResult();
	}
	
	private CooApplication fetchApplication(Long processFlowId) {
		LOG.debug("fetchApplication - " + processFlowId);
		
		Query q = entityManager.createQuery("select o from CooApplication o where o.processFlow.id = :processFlowId");
		q.setParameter("processFlowId", processFlowId);
		
		return (CooApplication) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessFlowItem> listProcessFlows(String username) {
		LOG.debug("listProcessFlows - " + username);

//FIXME For testing only; switch back to below
//		Query q = entityManager.createQuery("select o from ProcessFlowItem o where o.signatory.username = :username and o.signatureDate is null and o.docReqCompleted = true");
		Query q = entityManager.createQuery("select o from ProcessFlowItem o where o.signatory.username = :username and o.signatureDate is null");
		q.setParameter("username", username);
		
		return q.getResultList();
	}

	@Override
	public List<ProcessFlowItem> acceptProcessFlowItem(Long processFlowItemId, String signature, String username) {
		LOG.debug("acceptProcessFlowItem - " + processFlowItemId + " " + signature + " " + username);
		
		ProcessFlowItem processFlowItem = entityManager.find(ProcessFlowItem.class, processFlowItemId);

		String procSign = signature.replace("data:image/png;base64,", "");

		processFlowItem.setSignature(Base64.decodeBase64(procSign));
		processFlowItem.setSignatureDate(new Date());
		
		entityManager.merge(processFlowItem);
		
		if(processFlowItem.getNextItem() == null) {
			CooApplication application = fetchApplication(fetchProcessFlowId(fetchRootFlowItemId(processFlowItemId)));
			application.setCompletionDate(new Date());
			application.setAppStatus(CooStatus.COMPLETED);
			//TODO Initiate SMS notification
			entityManager.merge(application);
		}
		
		return listProcessFlows(username);
	}
	
	@Override
	public List<ProcessFlowItem> rejectProcessFlowItem(Long processFlowItemId, String message, String username) {
		LOG.debug("rejectProcessFlowItem - " + processFlowItemId + " " + message + " " + username);
		
		ProcessFlowItem prevProcessFlowItem = fetchPrevFlowItem(processFlowItemId);
		ServiceAgent serviceAgent = entityManager.find(ServiceAgent.class, username);

		ProcessFlowQuery flowQuery = new ProcessFlowQuery();
		flowQuery.setProcessFlowItem(prevProcessFlowItem);
		flowQuery.setServiceAgent(serviceAgent);
		flowQuery.setQueryDate(new Date());
		flowQuery.setTextMessage(message);
		entityManager.persist(flowQuery);
		
		prevProcessFlowItem.setSignature(null);
		prevProcessFlowItem.setSignatureDate(null);
		entityManager.merge(prevProcessFlowItem);
		//TODO Initiate SMS notification
		
		return listProcessFlows(username);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CooApplication> listApplications(Date paramDate, CooType type, CooStatus status, String username) {
		LOG.debug("listApplications - " + paramDate + " " + type + " " + status + " " + username);
		
		StringBuilder b = new StringBuilder("select o from CooApplication o where o.filingOfficer.username = :username");
		
		if(type != null) {
			b.append(" and o.cooType = :cooType");
		}	
		if(status != null) {
			b.append(" and o.appStatus = :cooStatus");
		}
		if(paramDate != null) {
			b.append(" and o.acceptanceDate <= :paramDate");
		}
		
		Query q = entityManager.createQuery(b.toString());
		q.setParameter("username", username);
		
		if(type != null) {
			q.setParameter("cooType", type);
		}	
		if(status != null) {
			q.setParameter("cooStatus", status);
		}
		if(paramDate != null) {
			q.setParameter("paramDate", paramDate);
		}
		
		return q.getResultList();
	}

	@Override
	public List<CertificateDoc> listCertificates(Long applicationId) {
		LOG.debug("listCertificates - " + applicationId);
		
		CooApplication application = entityManager.find(CooApplication.class, applicationId);
		
		return application.getCertificateDocs();
	}
	
	@SuppressWarnings("unchecked")
	private String fetchSignatoryByRole(String role) {
		LOG.debug("fetchSignatoryByRole - " + role);
		
		Query q = entityManager.createQuery("select o.user.username from UserRole o where o.role = :role");
		q.setParameter("role", role);
		
		List<String> agents = q.getResultList();
		if(agents != null && !agents.isEmpty()) {
			Collections.shuffle(agents);
		}
		return agents != null && !agents.isEmpty() ? agents.get(0) : null;
	}

	@Override
	public List<CooApplication> saveCooApplication(CooApplication application, String countryCode, Long streetId, Long occupationId, String username) {
		LOG.debug("saveCooApplication - " + application + " " + countryCode + " " + streetId + " " + occupationId + " " + username);
		
		application.setFilingOfficer(entityManager.find(Personnel.class, username));
		application.setAppStatus(CooStatus.INITIATED);
		if(application.getAcceptanceDate() == null) {
			application.setAcceptanceDate(new Date());
		}
		
		if(countryCode != null) {
			application.getVitalInformation().setNationality(entityManager.find(Country.class, countryCode));
		}
		
		if(streetId != null) {
			application.getVitalInformation().getGeoAddress().setGeoStreet(entityManager.find(GeoStreet.class, streetId));
		}
		
		if(occupationId != null) {
			application.getVitalInformation().setOccupation(entityManager.find(Occupation.class, occupationId));
		}
		
		if(application.getProcessFlow() == null) {			
			ProcessFlowTemplate processFlowTemplate = entityManager.find(ProcessFlowTemplate.class, application.getCooType());
			ProcessFlow processFlow = new ProcessFlow();
			application.setProcessFlow(processFlow);
			ProcessFlowItem processFlowItem;
			ProcessFlowItem previousFlowItem = null;
			ProcessFlowTemplateItem processFlowTemplateItem = processFlowTemplate.getProcessStart();
			
			int idx = 0;
			int sumOfDays = 0;
			Calendar cal = Calendar.getInstance();
			String userId = null;
			Personnel signatory;
			
			//TODO Assign to field agent if processFlowItem is GIS type

			while(processFlowTemplateItem != null) {				
				if("ROLE_SURVEYOR_GENERAL".equals(processFlowTemplateItem.getUserRole())) {
					processFlowItem = new ProcessFlowItem();
				} else {
					processFlowItem = new ProcessFlowGisItem();
				}

				cal.add(Calendar.DAY_OF_YEAR, sumOfDays);
				processFlowItem.setScheduledDate(cal.getTime());
				
				userId = fetchSignatoryByRole(processFlowTemplateItem.getUserRole());

				signatory = userId != null ? entityManager.find(Personnel.class, userId) : null;
				processFlowItem.setSignatory(signatory);
				
				entityManager.persist(processFlowItem);
				if(idx == 0) {
					processFlow.setProcessStart(processFlowItem);
				} else {
					previousFlowItem.setNextItem(processFlowItem);
				}
				
				LOG.info(processFlowTemplateItem.getUserRole() + ">>=" + sumOfDays);
				processFlowTemplateItem = processFlowTemplateItem.getNextItem();
				
				if(processFlowTemplateItem != null) {
					sumOfDays += processFlowTemplateItem.getNumberOfDays();
					idx++;
					previousFlowItem = processFlowItem;	
				}
			}
			
			//TODO Associate required certificate type instances
			List<CertificateType> certTypes = processFlowTemplate.getRequiredCerts();
			
			CertificateDoc certDoc;
			for(CertificateType cType : certTypes) {
				if((application.isLandDeveloped() && cType.getId() == 16) || (application.isLandOccupied() && cType.getId() == 17) || (cType.getId() != 16 && cType.getId() != 17)) {
					certDoc = new CertificateDoc();
					certDoc.setCertificateType(cType);
					certDoc.setCooApplication(application);
					entityManager.persist(certDoc);
				}
			}
			
			entityManager.persist(processFlow);
		}
		
		if(application.getId() == null) {
			entityManager.persist(application.getVitalInformation().getContactInfo());
			entityManager.persist(application.getVitalInformation().getGeoAddress());
			entityManager.persist(application.getVitalInformation());
			entityManager.persist(application);
		} else {
			entityManager.merge(application);
		}
		//TODO Initiate SMS notification
		
		return listApplications(new Date(), null, null, username);
	}

	@Override
	public List<CooApplication> rejectCooApplication(Long applicationId, String reason, String username) {
		LOG.debug("rejectCooApplication - " + applicationId + " " + reason + " " + username);
		
		CooApplication application = entityManager.find(CooApplication.class, applicationId);
		application.setAppStatus(CooStatus.REJECTED);
		application.setRejectionDate(new Date());
		application.setRejectionReason(reason);
		entityManager.merge(application);
		//TODO Initiate SMS notification
		
		return listApplications(new Date(), null, null, username);
	}

	@Override
	public List<CooApplication> cancelCooApplication(Long applicationId, String reason, String username) {
		LOG.debug("cancelCooApplication - " + applicationId + " " + reason + " " + username);
		
		CooApplication application = entityManager.find(CooApplication.class, applicationId);
		application.setAppStatus(CooStatus.CANCELED);
		application.setCancellationDate(new Date());
		application.setCancellationReason(reason);
		entityManager.merge(application);
		//TODO Initiate SMS notification
		
		return listApplications(new Date(), null, null, username);
	}
	
	private Query createSearchQuery(String className, List<String> properties, String param) {
		String[] paramTokens = param.split(" ");

		StringBuilder b = new StringBuilder("select o from " + className + " o");

		if (paramTokens != null && paramTokens.length > 0) {
			b.append(" where ");
			boolean start = true;
			for (int idx = 0; idx < paramTokens.length; idx++) {
				for (String prop : properties) {
					if (!start) {
						b.append(" or");
					} else {
						start = false;
					}
					b.append(" o.");
					b.append(prop);
					b.append(" like ");
					b.append("'%");
					b.append(paramTokens[idx]);
					b.append("%'");
				}
			}
		}

		LOG.debug("createSearchQuery - " + b);

		Query q = entityManager.createQuery(b.toString());

		return q;
	}

	private Query createSearchQueryForLga(List<String> properties, String param, Long stateId) {
		String[] paramTokens = param.split(" ");

		StringBuilder b = new StringBuilder("select o from LocalGovArea o");

		if (paramTokens != null && paramTokens.length > 0) {
			b.append(" where (");
			boolean start = true;
			for (int idx = 0; idx < paramTokens.length; idx++) {
				for (String prop : properties) {
					if (!start) {
						b.append(" or");
					} else {
						start = false;
					}
					b.append(" o.");
					b.append(prop);
					b.append(" like ");
					b.append("'%");
					b.append(paramTokens[idx]);
					b.append("%'");
				}
			}
		}

		b.append(") and countryState.id = ");
		b.append(stateId);

		LOG.debug("createSearchQuery - " + b);

		Query q = entityManager.createQuery(b.toString());

		return q;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ethnicity> searchEthnicities(String param) {
		LOG.debug("searchEthnicities - " + param);

		Query q = createSearchQuery("Ethnicity", PROP_NAMES_ETHNICITY, param);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalGovArea> searchLgas(String param, Long stateId) {
		LOG.debug("searchLgas - " + param + " " + stateId);

		Query q = createSearchQueryForLga(PROP_NAMES_LGA, param, stateId);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Country> searchCountries(String param) {
		LOG.debug("searchCountries - " + param);

		Query q = createSearchQuery("Country", PROP_NAMES_COUNTRY, param);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GeoPolygon> listStreetPolygons(Long streetId) {
		LOG.debug("listStreetPolygons - " + streetId);
		
		Query q = entityManager.createQuery("select o from GeoPolygon o where o.geoStreet.id = :streetId");
		q.setParameter("streetId", streetId);
		
		return q.getResultList();
	}

	@Override
	public Long fetchApplicationIdByFlowItem(Long processFlowItemId) {
		LOG.debug("fetchApplicationByFlowItem - " + processFlowItemId);
		
		CooApplication application = fetchApplication(fetchProcessFlowId(fetchRootFlowItemId(processFlowItemId)));
		return application != null ? application.getId() : null;
	}

	@Override
	public Date fetchPolygonSurveyDate(Long polygonId) {
		LOG.debug("fetchPolygonSurveyDate - " + polygonId);
		
		Query q = entityManager.createQuery("select o.surveyDate from ProcessFlowGisItem o where o.sitePolygon.id = :polygonId");
		q.setParameter("polygonId", polygonId);
		
		return (Date) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long fetchNextPolygonItemId(String username) {
		LOG.debug("fetchNextPolygonItemId - " + username);
		
		Query q = entityManager.createQuery("select o.sitePolygon.id from ProcessFlowGisItem o where o.fieldAgent.username = :username and o.surveyDate is null");
		q.setParameter("username", username);
		q.setMaxResults(1);
		List<Long> results = q.getResultList();
		
		return results != null && !results.isEmpty() ? results.get(0) : null;
	}

	@Override
	public Long savePolygonSurvey(Long gisItemId, Long streetId, String description, File[] uploads, String capturedPolygons, String username) {
		LOG.debug("savePolygonSurvey - " + uploads + " " + streetId + " " + description + " " + capturedPolygons + " " + username);

		ProcessFlowGisItem gisItem = entityManager.find(ProcessFlowGisItem.class, gisItemId);
		
		GeoStreet geoStreet = streetId != null ? entityManager.find(GeoStreet.class, streetId) : null;
		
		GeoPolygon sitePolygon = new GeoPolygon();
		sitePolygon.setBuildingPolygon(parsePolygon(capturedPolygons));
		sitePolygon.setGeoStreet(geoStreet);
		sitePolygon.setDescription(description);
		
		entityManager.persist(sitePolygon);
		
		gisItem.setSurveyDate(new Date());		
		gisItem.setSitePolygon(sitePolygon);

		entityManager.merge(gisItem);
		entityManager.flush();
		
		return fetchNextPolygonItemId(username);
	}

	@Override
	public void payCharge(Long applicationId, Long chargeId, String username) {
		LOG.debug("payCharge - " + applicationId + " " + chargeId + " " + username);
		
		CooApplication application = entityManager.find(CooApplication.class, applicationId);
		
		CooCharge charge = entityManager.find(CooCharge.class, chargeId);
		
		ServiceAgent agent = entityManager.find(ServiceAgent.class, username);
		
		AccountTransaction trans = new AccountTransaction();
		trans.setChargeType(charge);
		trans.setAccountOwner(application.getVitalInformation());
		trans.setServiceAgent(agent);
		trans.setTransactionType(TransactionType.DEBIT);
		trans.setTransactionDate(new Date());
		entityManager.persist(trans);
	}
	
	private Polygon parsePolygon(String unformattedPolygon) {
		LOG.debug("parsePolygon - " + unformattedPolygon);
		
		unformattedPolygon = unformattedPolygon.replace(")(", "_");
		unformattedPolygon = unformattedPolygon.replace("(", "");
		unformattedPolygon = unformattedPolygon.replace(")", "");
		unformattedPolygon = unformattedPolygon.replace(" ", "");
		String[] xyPair = unformattedPolygon.split("_");
		Polygon polygon = null;
		if(xyPair != null && xyPair.length > 0) {
			Coordinate[] coordinates = new Coordinate[xyPair.length];
			int i = 0;
			String[] xandy;
			for(String cood : xyPair) {
				xandy = cood.split(",");
				coordinates[i] = new Coordinate(Double.valueOf(xandy[0]), Double.valueOf(xandy[1]));
				i++;
			}
			GeometryFactory geometryFactory = new GeometryFactory();
			LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
			polygon = new Polygon(linearRing, null, geometryFactory);
		}
		
		return polygon;
	}

	@Override
	public List<String> validateApplication(Long applicationId) {
		LOG.debug("validateApplication - " + applicationId);
		
		List<String> errors = new ArrayList<>();
		
		CooApplication application = entityManager.find(CooApplication.class, applicationId);
		
		List<CertificateDoc> certDocs = application.getCertificateDocs();
		
		for(CertificateDoc c : certDocs) {
			if(c.getCertificateFiles().isEmpty()) {
				errors.add("Missing '" + c.getCertificateType().getDescription() + "'.");
			}
		}
		
		return errors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessFlowQuery> listQueryMessages(Long processFlowItemId) {
		LOG.debug("listQueryMessages - " + processFlowItemId);
		
		Query q = entityManager.createQuery("select o from ProcessFlowQuery o where o.processFlowItem.id = :processFlowItemId");
		q.setParameter("processFlowItemId", processFlowItemId);
		
		return q.getResultList();
	}
	
	private class SmsDeliveryThread extends Thread implements Runnable {
		
		private Map<String, String> msgMap;

		private SmsDeliveryThread(Map<String, String> msgMap) {
			super();
			this.msgMap = msgMap;
		}

		@Override
		public void run() {
			LOG.debug("deliverMessages - " + msgMap);

			try {

				for (String destination : msgMap.keySet()) {
					StringBuilder b = new StringBuilder();

					b.append("username=").append(smsSenderUser);
					b.append("&password=").append(smsSenderPass);
					b.append("&to=").append(destination);
					b.append("&text=").append(URLEncoder.encode(msgMap.get(destination), "UTF-8"));

					URL url = new URL(smsSenderUrl + "?" + b);

					LOG.info("SMS URL: " + smsSenderUrl + "?" + b);

					HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
					httpConnection.setRequestMethod("GET");

					int responseCode = httpConnection.getResponseCode();

					LOG.info("SMS Sender response code: " + responseCode);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
