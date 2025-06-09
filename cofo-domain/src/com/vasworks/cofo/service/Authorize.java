/**
 * 
 */
package com.vasworks.cofo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vasworks.cofo.model.ServiceAgent;

/**
 * <p>
 * Utility class to chech for user's granted roles.
 * </p>
 * 
 * @author developer
 */
public class Authorize {

	/**
	 * @param string
	 * @return
	 */
	public static boolean hasAny(String requestedAuthorities) {
		String[] requested = requestedAuthorities.split(",");
		for (String r : requested) {
			if (hasAuthority(r.trim()))
				return true;
		}
		return false;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean hasAll(String requestedAuthorities) {
		String[] requested = requestedAuthorities.split(",");
		boolean hasAll = true;
		for (String r : requested) {
			hasAll &= hasAuthority(r.trim());
			if (!hasAll)
				return false;
		}
		return hasAll;
	}

	/**
	 * @param r
	 * @return
	 */
	public static boolean hasAuthority(String r) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return false;
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (authority.getAuthority().equals(r))
				return true;
		}
		return false;
	}

	/**
	 * @param user
	 * @param string
	 * @return
	 */
	public static boolean hasRole(ServiceAgent user, String string) {
		for (GrantedAuthority role : user.getAuthorities()) {
			if (role.getAuthority().equalsIgnoreCase(string))
				return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public static ServiceAgent getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		if (authentication.getPrincipal() instanceof ServiceAgent) {
			return (ServiceAgent) authentication.getPrincipal();
		}
		return null;
	}

}
