/**
 * 
 */
package com.vasworks.cofo.struts;

import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.vasworks.cofo.model.ServiceAgent;

/**
 * @author developer
 * 
 */
@SuppressWarnings("serial")
public abstract class BaseAction extends ActionSupport implements Preparable, ServletContextAware {

	/**
	 * 
	 */
	private String path;
	private ServiceAgent user = null;

	/**
	 * 
	 */
	public BaseAction() {
		super();
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.path = arg0.getRealPath("/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() {

	}

	@Override
	public String execute() {
		return Action.SUCCESS;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Get the user for which we are accessing data.
	 * 
	 * @see getPrincipal()
	 * @return the user
	 */
	public ServiceAgent getUser() {
		LOG.debug("getUser()");
		
		LOG.debug("getUser(): " + this.user);
		LOG.debug("getUser(): " + SecurityContextHolder.getContext());
		LOG.debug("getUser(): " + SecurityContextHolder.getContext().getAuthentication());
		
		if (this.user == null && SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			LOG.debug("SecurityContextHolder: " + SecurityContextHolder.getContext() + " " + SecurityContextHolder.getContext().getAuthentication());
			Object u = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LOG.debug("ServiceAgent: " + u + " Type: " + u.getClass());
			if (u instanceof ServiceAgent)
				this.user = (ServiceAgent) u;
		}
		return this.user;
	}

	/**
	 * Get currently logged in user.
	 * 
	 * @return the user
	 */
	public ServiceAgent getPrincipal() {
		ServiceAgent user = getUser();
		if (user == null) {
			return null;
		}
		else {
			return user;
		}
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getVirtualDirectory() {
		ActionContext ac = ActionContext.getContext().getActionInvocation().getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		String contextPath = request.getContextPath();
		return request == null ? null : (contextPath.equals("/") ? null : contextPath);
	}

	/**
	 * General "cancel" button action that returns "goback" as action result.
	 * 
	 * @return
	 */
	public String goback() {
		LOG.info("Go back!");
		return "goback";
	}

	/**
	 * Get user's timezone
	 * 
	 * @return
	 */
	public TimeZone getTimezone() {
		TimeZone timezone = (TimeZone) ActionContext.getContext().get("WW_TRANS_I18N_TIMEZONE");
		if (timezone == null)
			timezone = TimeZone.getDefault();
		return timezone;
	}
}