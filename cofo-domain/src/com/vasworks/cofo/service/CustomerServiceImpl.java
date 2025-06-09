package com.vasworks.cofo.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.vasworks.cofo.model.AccountTransaction;
import com.vasworks.cofo.model.VitalInformation;

@Transactional
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CustomerServiceImpl implements CustomerService {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#.00");
	
	public static final DecimalFormat VOUCHER_FORMAT = new DecimalFormat("0000000000");
	
	public static final Log LOG = LogFactory.getLog(CustomerServiceImpl.class);

	private String smsSenderUrl;

	private String smsSenderUser;

	private String smsSenderPass;
	
	static {
		CURRENCY_FORMAT.setCurrency(Currency.getInstance("NGN"));
	}

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

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
	public Map<String, String> processCommand(String originator, String reqCommand) {
		LOG.debug("processCommand - " + originator + " " + reqCommand);

		Map<String, String> msgMap = new HashMap<String, String>();
		if (reqCommand != null) {
			String[] tokens = reqCommand.split(" ");

			if (tokens[0] != null) {
				try {
					
					CMDS cmds = CMDS.valueOf(tokens[0].toUpperCase());
					Integer cmd = cmds != null ? cmds.ordinal() : null;
					cmd = cmd != null ? cmd : -1;
					switch (cmd) {
					case 0:
						if (tokens.length == 1) {
							msgMap = register(originator);
						} else {
							msgMap.put(originator, "Invalid command format.");
						}
						break;
					case 1:
						if (tokens.length == 1) {
							msgMap = help(originator);
						} else {
							msgMap.put(originator, "Invalid command format.");
						}
						break;
					case 2:
						if (tokens.length == 2) {
//TODO							msgMap = creditAccount(originator, tokens[1]);
						} else {
							msgMap.put(originator, "Invalid command format.");
						}
						break;
					case 3:
						if (tokens.length == 2) {
//TODO							msgMap = purchaseItem(originator, Long.valueOf(tokens[1]));
						} else {
							msgMap.put(originator, "Invalid command format.");
						}
						break;
					default:
						msgMap = help(originator);
						
						if(msgMap.isEmpty()) {
							msgMap.put(originator, "Invalid command format.");
						}
						break;
					}
				} catch (Exception e) {
					msgMap.put(originator, "Unknown error (most likely wrong format).");
					e.printStackTrace();
				}

			}

			if (!msgMap.isEmpty()) {
				deliverMessages(msgMap);
			}
		}

		return msgMap;
	}

	@Override
	public Map<String, String> register(String mobileNumber) {
		LOG.debug("register - " + mobileNumber);

		Map<String, String> msg = new HashMap<String, String>();
			
		//TODO This doesn't do anything
		msg.put(mobileNumber, "Welcome to C of O Notifier!\n");

		return msg;
	}

	@Override
	public Map<String, String> help(String mobileNumber) {
		LOG.debug("help - " + mobileNumber);

		Map<String, String> msgMap = new HashMap<String, String>();

		msgMap.put(mobileNumber, APPENDED_CMD_CATALOG);

		return msgMap;
	}

	@Override
	public Map<String, String> checkStatus(String originator) {
		LOG.debug("checkStatus - " + originator);
		
		VitalInformation customer = (VitalInformation) entityManager.find(VitalInformation.class, originator);
				
		Map<String, String> msgMap = new HashMap<String, String>();
		
		if(customer != null) {
			msgMap.put(originator, "Current balance: " + CURRENCY_FORMAT.format(customer.getAccountBalance()) + ".");
		} else {
			msgMap.put(originator, "Customer not registered. Deposit an amount to be registered.");
		}
		
		return msgMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountTransaction> listTransactions(String customerId, Date fromDate, Date toDate) {
		LOG.debug("listTransactions - " + customerId + " " + fromDate + " " + toDate);
		
		StringBuilder b = new StringBuilder("select o from AccountTransaction o where");
		
		if(customerId != null) {
			b.append(" o.accountOwner.phoneNumber = :customerId");
		}
		
		if(fromDate == null) {
			if(customerId != null) {
				b.append(" and");
			}
			b.append(" o.transactionDate like '%" + DATE_FORMAT.format(new Date()) + "%'");
		} else {
			if(customerId != null) {
				b.append(" and");
			}
			b.append(" o.transactionDate between :fromDate");
		}
		
		if(toDate == null) {
			toDate = new Date();
		}
		
		if(fromDate != null) {
			b.append(" and :toDate");
		}
		
		Query q = entityManager.createQuery(b.toString());
		
		if(customerId != null) {
			q.setParameter("customerId", customerId);
		}
		
		if(fromDate != null) {
			q.setParameter("fromDate", fromDate);
			q.setParameter("toDate", toDate);
		}
		
		return q.getResultList();
	}
	
	private void deliverMessages(Map<String, String> msgMap) {
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

	@Override
	public void remove(Object entityId, Class<?> entityType) throws PersistenceException {
		entityManager.remove(entityManager.find(entityType, entityId));
	}

	@Override
	public Object find(Object entityId, Class<?> entityClass) throws NoResultException {
		return entityManager.find(entityClass, entityId);
	}
}
