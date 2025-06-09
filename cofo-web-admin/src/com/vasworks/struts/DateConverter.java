package com.vasworks.struts;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.TypeConversionException;
import com.vasworks.util.DateUtil;

public class DateConverter extends StrutsTypeConverter {
	private static final Log LOG = LogFactory.getLog(DateConverter.class);

	@SuppressWarnings("rawtypes")
	public Object convertFromString(Map ctx, String[] value, Class arg2) {
		if (value[0] == null || value[0].trim().equals("")) {
			return null;
		}

		TimeZone timezone=(TimeZone) ActionContext.getContext().get("WW_TRANS_I18N_TIMEZONE");
		if (timezone==null) timezone=TimeZone.getDefault();
		
		try {
			return DateUtil.convertStringToDate(value[0], timezone);
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new TypeConversionException(pe.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public String convertToString(Map ctx, Object data) {
		return DateUtil.convertDateToString((Date) data);
	}
}