package com.vasworks.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time pattern.
 *         Minutes should be mm not MM (MM is month).
 */
public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);
	private static final String TIME_PATTERN = "HH:mm";

	/**
	 * Checkstyle rule: utility classes should not have public constructor
	 */
	private DateUtil() {
	}

	/**
	 * Return default datePattern (MM/dd/yyyy)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		String defaultDatePattern;
		try {
			defaultDatePattern = ResourceBundle.getBundle("ApplicationResources", locale).getString("date.format");
		} catch (MissingResourceException mse) {
			defaultDatePattern = "dd/MM/yyyy";
		}

		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		log.debug("Using LOCALE: " + locale);
		String defaultDateTimePattern;
		try {
			defaultDateTimePattern = ResourceBundle.getBundle("ApplicationResources", locale).getString("datetime.format");
		} catch (MissingResourceException mse) {
			defaultDateTimePattern = "dd/MM/yyyy HH:mm";
		}

		return defaultDateTimePattern;
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate date from database as a string
	 * @return formatted string for the ui
	 */
	public static String getDate(Date aDate) {
		SimpleDateFormat df;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the format you specify on input
	 * 
	 * @param aMask the date pattern the string is in
	 * @param strDate a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException when String doesn't match the expected format
	 */
	public static Date convertStringToDate(String aMask, String strDate, TimeZone timezone) throws ParseException {
		SimpleDateFormat df;
		Date date;
		df = new SimpleDateFormat(aMask);
		df.setTimeZone(timezone);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "' in timezone " + timezone.getDisplayName());
		}

		try {
			date = df.parse(strDate);
			log.debug("result=" + df.format(date));
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return date;
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM a
	 * 
	 * @param theTime the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(TIME_PATTERN, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException when String doesn't match the expected format
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString, TimeZone.getDefault()));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time in the format you specify on input
	 * 
	 * @param aMask the date pattern the string is in
	 * @param aDate a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date based on the System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate A date to convert
	 * @return a string representation of the date
	 */
	public static String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * This method converts a String to a date using the datePattern. The method will first try to parse date time format, then the provided date format. If
	 * both fail, a ParseException is thrown.
	 * 
	 * @param strDate the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * @throws ParseException when String doesn't match the expected format
	 */
	public static Date convertStringToDate(String strDate, TimeZone tz) throws ParseException {
		Date aDate = null;

		try {
			log.info("converting date with pattern: " + getDateTimePattern());

			aDate = convertStringToDate(getDateTimePattern(), strDate, tz);
			return aDate;
		} catch (ParseException pe) {
			log.info("Could not convert '" + strDate + "' to a date with pattern: " + getDateTimePattern());
		}

		try {
			log.info("converting date with pattern: " + getDatePattern());

			aDate = convertStringToDate(getDatePattern(), strDate, tz);
			return aDate;
		} catch (ParseException pe) {
			log.info("Could not convert '" + strDate + "' to a date with pattern: " + getDatePattern());
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
	}

	public static Date sum(Date date1, Date date2) {
		Date sum = null;
		if (date1 == null && date2 != null) {
			sum = date2;
		} else if (date2 == null && date1 != null) {
			sum = date1;
		} else if (date1 == null && date2 == null) {
			sum = new Date();
		} else {
			sum = new Date(date1.getTime() + date2.getTime());
		}

		log.debug("date1: " + date1 + " date2: " + date2 + " sum: " + sum);

		return sum;
	}

	/**
	 * @param string
	 * @param timezone
	 * @return
	 * @throws ParseException
	 */
	public static Calendar convertStringToCalendar(String string, TimeZone timezone) throws ParseException {
		Date date = convertStringToDate(string, timezone);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}
}