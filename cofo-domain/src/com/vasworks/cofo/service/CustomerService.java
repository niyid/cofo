package com.vasworks.cofo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import com.vasworks.cofo.model.AccountTransaction;

public interface CustomerService {
	
	static final String APPENDED_CMD_CATALOG = "Register: reg [First Name] [Surname]\n";
	
	public static enum CMDS {REG, HELP, STATUS, PAYMENTS};
	/**
	 * 
	 * @param entityId
	 * @param entityClass
	 * @return
	 * @throws NoResultException
	 */
	public Object find(Object entityId, Class<?> entityClass) throws NoResultException;

	/**
	 * 
	 * @param entityId
	 * @param entityType
	 * @throws PersistenceException
	 */
	public void remove(Object entityId, Class<?> entityType) throws PersistenceException;
	
	/**
	 * 
	 * @param originator
	 * @param reqCommand
	 * @return
	 */
	Map<String, String> processCommand(String originator, String reqCommand);
	
	/**
	 * 
	 * @param mobileNumber
	 * @return
	 */
	Map<String, String> register(String mobileNumber);
	
	/**
	 * 
	 * @param mobileNumber
	 * @return
	 */
	Map<String, String> help(String mobileNumber);
	
	/**
	 * 
	 * @param originator
	 * @param itemId
	 * @return
	 */
	Map<String, String> checkStatus(String originator);
	
	/**
	 * 
	 * @param customerId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	List<AccountTransaction> listTransactions(String customerId, Date fromDate, Date toDate);
	
}
