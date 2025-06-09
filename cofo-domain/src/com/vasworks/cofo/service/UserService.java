/**
 * 
 */
package com.vasworks.cofo.service;

import java.util.List;

import com.vasworks.cofo.model.ServiceAgent;
import com.vasworks.cofo.model.UserRole;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserService.
 * 
 * @author developer
 */
public interface UserService extends org.springframework.security.core.userdetails.UserDetailsService {

	/**
	 * Load user by username.
	 * 
	 * @param username the username
	 * 
	 * @return the user
	 * 
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	ServiceAgent loadUserByUsername(String username);

	/**
	 * Find.
	 * 
	 * @param id the id
	 * 
	 * @return the user
	 */
	public ServiceAgent find(long id);

	/**
	 * Save.
	 * 
	 * @param user the user
	 * 
	 * @return the string
	 */
	public void save(ServiceAgent user);

	/**
	 * Removes the.
	 * 
	 * @param id the id
	 * 
	 * @return the string
	 */
	public String remove(long id);

	/**
	 * Find all.
	 * 
	 * @return the list< user>
	 */
	public List<ServiceAgent> findAll();


	/**
	 * Find user by lookup value. Will automatically try to import user data.
	 * 
	 * @param identifier the identifier
	 * 
	 * @return the user
	 */
	public ServiceAgent lookup(String identifier);
	
	/**
	 * Sets the password.
	 * 
	 * @param passwd1 the passwd1
	 * @param user the user
	 */
	void setPassword(ServiceAgent user, String passwd1);

	/**
	 * Checks if is password valid.
	 * 
	 * @param password the password
	 * @param user the user
	 * 
	 * @return true, if checks if is password valid
	 */
	boolean isPasswordValid(ServiceAgent user, String password);

	/**
	 * 
	 * @param user
	 * @return
	 */
	List<UserRole> getUserRoles(ServiceAgent user);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<ServiceAgent> findByRole(String role);
	
	/**
	 * 
	 * @param application
	 * @param role
	 * @return
	 */
	List<ServiceAgent> findByRole(String application, String role);

	/**
	 * @param lookup
	 * @return
	 */
	List<ServiceAgent> findByName(String lookup, int maxResults);
	
	/**
	 * 
	 * @param user
	 * @param application
	 * @return
	 */
	List<UserRole> getUserRoles(ServiceAgent user, String application);
	
	/**
	 * @param staffID
	 * @return
	 */
	ServiceAgent findByStaffID(String staffID);

	/**
	 * Get all user roles defined in this application
	 */
	List<String> getUserRoles();

	/**
	 * Find all users with a particular role
	 * 
	 * @param role
	 * @param startAt
	 * @param maxResults
	 * @return
	 */

	void updateLoginData(ServiceAgent user);

}
