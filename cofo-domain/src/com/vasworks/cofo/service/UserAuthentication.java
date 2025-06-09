/**
 * 
 */
package com.vasworks.cofo.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vasworks.cofo.model.ServiceAgent;
import com.vasworks.cofo.model.ServiceAgent.UserStatus;

/**
 * VasWorks Authentication module authenticates users against ActiveDirectory or
 * other LDAP. After a successful "bind" to the directory, user's data is loaded
 * from table ServiceAgent. If there's no record of that user in the database,
 * but user authenticated successfully, a record is created for that user.
 * 
 * @author developer
 * 
 */
public class UserAuthentication implements AuthenticationProvider {
	private static final Log log = LogFactory.getLog(UserAuthentication.class);
	private UserService userService;

	public UserAuthentication(UserService userService) {
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.providers.AuthenticationProvider#authenticate
	 * (org.springframework.security.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Object principal = authentication.getPrincipal();
		Object credentials = authentication.getCredentials();
		if (principal instanceof String && credentials instanceof String) {
			String username = (String) principal;
			String sAMAccountName = username != null ? username.toLowerCase() : null;
			String domainName = null;
			if (username != null && username.split("\\\\").length == 2) {
				String[] loginSplit = username.split("\\\\");
				domainName = loginSplit[0];
				sAMAccountName = loginSplit[1];
				log.info("Authenticating domain: " + domainName + " Username: " + sAMAccountName);
			}
			String password = (String) credentials;

			log.info("Authenticating username '" + username + "' with password length " + password.length());
			if (username.trim().length() == 0 || password == null || password.length() == 0) {
				log.info("Not allowing blank username");
				throw new BadCredentialsException("Provide username and password!");
			}

			// load user by username
			ServiceAgent userdetails = userService.loadUserByUsername(sAMAccountName);

			if (userdetails == null) {
				throw new UsernameNotFoundException("ServiceAgent not registered with application.");
			}

			if (userdetails != null) {
				// user registered with local applicationIdentifier,
				// authenticate
				log.info("ServiceAgent " + sAMAccountName + " registered with applicationIdentifier, authenticating.");

				if (userdetails.getStatus() != UserStatus.ENABLED) {
					log.error("The account has been disabled. Status is: " + userdetails.getStatus() + ", needs to be " + UserStatus.ENABLED);
					throw new BadCredentialsException("The account has been disabled.");
				}

				log.info("ServiceAgent " + sAMAccountName + " set to authenticate with PASSWORD.");
				if (!userService.isPasswordValid(userdetails, password)) {
					log.warn("Invalid password for user " + sAMAccountName);
					userdetails.setLastLoginFailed(new Date());
					userService.updateLoginData(userdetails);
					userdetails = null;
					throw new BadCredentialsException("Login failed!");
				} else {
					log.info("ServiceAgent " + username + " authenticated with PASSWORD method.");
				}
			}

			if (userdetails != null) {
				if (log.isInfoEnabled())
					log.info("ServiceAgent " + username + " fully authenticated.");
				userdetails.setLastLogin(new Date());
				userService.updateLoginData(userdetails);

				// force load roles
				userdetails.setRoles(userService.getUserRoles(userdetails));

				log.info("ServiceAgent authorities being initialized...");
				log.info(userdetails.getRoles());

				if (log.isInfoEnabled())
					log.info("ServiceAgent " + username + " successfully logged in.");
				Authentication usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(userdetails, userdetails, userdetails.getAuthorities());
				return usernamePasswordAuthToken;
			}
		}
		return authentication;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.providers.AuthenticationProvider#supports
	 * (java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(arg0));
	}
}
