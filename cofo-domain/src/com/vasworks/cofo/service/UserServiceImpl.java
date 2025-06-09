/**
 * 
 */
package com.vasworks.cofo.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LazyInitializationException;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.vasworks.cofo.model.ServiceAgent;
import com.vasworks.cofo.model.ServiceAgent.UserStatus;
import com.vasworks.cofo.model.UserRole;


/**
 * @author developer
 * 
 */
public class UserServiceImpl implements UserService {
	private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
	private String applicationName = null;

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplication(String applicationId) {
		LOG.warn("Application ID configured to: " + applicationId);
		this.applicationName = applicationId;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devworks.par.service.UserService#loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public ServiceAgent loadUserByUsername(String username) {
		LOG.debug("loadUserByUsername - " + username);
		LOG.debug("loadUserByUsername - " + this.entityManager);

		
		if (username == null)
			return null;

		try {			
			ServiceAgent u = (ServiceAgent) this.entityManager.createQuery("from ServiceAgent u where u.username=:username").setParameter("username", username).setMaxResults(1)
					.getSingleResult();
			return u;
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ServiceAgent find(long id) {
		return entityManager.find(ServiceAgent.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ServiceAgent> findAll() {
		Query query = this.entityManager.createQuery("from ServiceAgent u where u.status!=? order by u.lastName, u.firstName").setParameter(1, UserStatus.DELETED);
		return query.getResultList();
	}


	@Override
	@Transactional(readOnly = true)
	public String remove(long id) {
		ServiceAgent user = find(id);
		if (user != null) {
			user.setStatus(UserStatus.DELETED);
			entityManager.merge(user);
			return "success";
		} else {
			return "input";
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void save(ServiceAgent user) {
		ServiceAgent userPreSave = this.entityManager.find(ServiceAgent.class, user.getUsername());
		try {
			try {
				List<UserRole> roles = userPreSave.getRoles();
				if (roles != null)
					for (int i = roles.size() - 1; i >= 0; i--)
						if (roles.get(i).getRole() == null || roles.get(i).getRole().trim().length() == 0) {
							UserRole role = roles.remove(i);
							this.entityManager.remove(role);
						} else
							roles.get(i).setUser(user);
			} catch (LazyInitializationException e) {

			}
			
			LOG.trace("Merging user data for id=" + user.getUsername());
			entityManager.merge(user);

		} catch (RuntimeException e) {
			LOG.error(e);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updateLoginData(ServiceAgent user) {
		try {
			LOG.trace("Merging user data for id=" + user.getUsername());
			entityManager.merge(user);
		} catch (RuntimeException e) {
			LOG.error(e);
			throw e;
		}
	}

	/*
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public ServiceAgent lookup(String identifier) {
		LOG.info("Looking up user with identifier: " + identifier);
		try {
			return (ServiceAgent) entityManager.createQuery("select ul.user from UserLookup ul where ul.identifier like :identifier").setParameter("identifier",
					identifier).getSingleResult();
		} catch (NoResultException e) {
			LOG.info("Lookup service: '" + identifier + "' did not find any matches. Error: " + e.getMessage());
		} catch (Exception e) {
			LOG.info("Lookup service: '" + identifier + "' did not find any matches. Error: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Sets user's password and configures the account to use PASSWORD login instead of LDAP.
	 * 
	 * @see com.devworks.par.service.UserService#setPassword(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public void setPassword(ServiceAgent user, String passwd1) {
		LOG.info("Setting password for " + user.getUsername() + ".");
		user.setPassword(hashPassword(passwd1, user.getPasswordSalt()));
		updateLoginData(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devworks.par.service.UserService#isPasswordValid(com.devworks.par.model.User, java.lang.String)
	 */
	@Override
	public boolean isPasswordValid(ServiceAgent user, String password) {
		LOG.info("Checking password for " + user);
		if (password == null) {
			LOG.debug("Provided null password for " + user.getUsername());
			return false;
		}
		String hash = hashPassword(password, user.getPasswordSalt());
		LOG.debug("Pass-hash: " + hash);
		LOG.info("Password check for " + user.getUsername() + ": " + user.getPasswordHash().equals(hashPassword(password, user.getPasswordSalt())));
		
		return !UserStatus.DISABLED.equals(user.getStatus()) && user.getPasswordHash() != null && user.getPasswordHash().equals(hashPassword(password, user.getPasswordSalt())); 
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
	
	@Override
	@Transactional(readOnly = true)
	public List<UserRole> getUserRoles(ServiceAgent user) {
		return this.getUserRoles(user, this.applicationName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devworks.par.service.UserService#getUserRoles(com.devworks.par.model.User, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<UserRole> getUserRoles(ServiceAgent user, String application) {
		LOG.info("Loading roles " + application + ".* for " + user);
		return this.entityManager.createQuery("from UserRole ur where ur.application=:application and ur.user=:user").setParameter("application", application)
				.setParameter("user", user).getResultList();
	}

	@Override
	public List<ServiceAgent> findByRole(String role) {
		return this.findByRole(this.applicationName, role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.devworks.par.service.UserService#findByRole(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ServiceAgent> findByRole(String application, String role) {
		return this.entityManager.createQuery(
				"select ur.user from UserRole ur where ur.application=:application and ur.role=:role order by ur.user.lastName, ur.user.firstName")
				.setParameter("application", application).setParameter("role", role).getResultList();
	}

	/**
	 * @see com.vasworks.security.service.UserService#findByName(java.lang.String, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ServiceAgent> findByName(String lookup, int maxResults) {
		lookup = lookup + "%";
		return this.entityManager.createQuery("from ServiceAgent u where u.lastName like :lookup or u.firstName like :lookup order by u.lastName, u.firstName")
				.setParameter("lookup", lookup).setMaxResults(maxResults).getResultList();
	}

	/**
	 * @see com.vasworks.security.service.UserService#getUserRoles()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<String> getUserRoles() {
		return this.entityManager.createQuery("select distinct ur.role from UserRole ur order by ur.role").getResultList();
	}


	@Override
	public ServiceAgent findByStaffID(String staffID) {
		try {
			return (ServiceAgent) this.entityManager.createQuery("from ServiceAgent u where u.staffId=:staffID").setParameter("staffID", staffID).getSingleResult();
		} catch (NoResultException e) {
			LOG.error("No staff found for ID: " + staffID, e);
			return null;
		} catch (NonUniqueResultException e) {
			LOG.error("Multiple staff found for ID: " + staffID, e);
			return null;
		}
	}

}
