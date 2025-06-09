package com.vasworks.cofo.struts.agent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.vasworks.cofo.struts.AgentAction;

// TODO: Auto-generated Javadoc
/**
 * The Class DialogHomeAction.
 */
public class DialogHomeAction extends AgentAction {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -45564564536L;
	
	/** The Constant log. */
	private static final Log LOG = LogFactory.getLog(DialogHomeAction.class);
	
	/* (non-Javadoc)
	 * @see com.vasworks.struts.BaseAction#prepare()
	 */
	@Override
	@SkipValidation
	public void prepare() {
		LOG.debug("prepare()");
		
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@SkipValidation
	public String execute() {
	
		return SUCCESS;
	}

}
