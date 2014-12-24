package mdn.vtvsport.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;

/**
 * String Utility class
 * 
 */
public class StringUtil {

	public static final String SERVER_RACE_MEETING_DATE_FORMAT = "yyyy-MM-dd";

	public static final String RACE_MEETING_DATE_FORMAT = "MMM yyyy";

	public static final String PREVIOUS_PLAY_DATE_FORMAT = "dd-MM-yyyy";

	public static final String TIMESTAMP_DATE_FORMAT = "yyyy-MM-dd HH:mm";

	public static final String TIMESTAMP_DATE_FORMAT_CONVERT = "HH:mm dd MMM yyyy";

	public static final String TIMESTAMP_DATE_FORMAT_12H = "dd MMM yyyy hh:mm:ss a";

	public static final String TIMESTAMP_DATE_FORMAT_STANDARD = "yyyy-MM-dd";

	public static final String TIMESTAMP_DATE_12H = "dd MMM yyyy";

	public static final String JANUARY = "January";

	public static final String FEBRUARY = "February";

	public static final String MARCH = "March";

	public static final String APRIL = "April";

	public static final String MAY = "May";

	public static final String JUNE = "June";

	public static final String JULY = "July";

	public static final String AUGUST = "August";

	public static final String SEPTEMBER = "September";

	public static final String OCTOBER = "October";

	public static final String NOVEMBER = "November";

	public static final String DECEMBER = "December";

	/**
	 * Check input string
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}

	/**
	 * Convert a date string to a date
	 * 
	 * @param time
	 * @return
	 */
	public static Date convertStringToDate(String time) {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				SERVER_RACE_MEETING_DATE_FORMAT);
		Date date = null;
		try {
			date = dateformat.parse(time);
			return date;
		} catch (ParseException e) {
			return new Date(System.currentTimeMillis());
		}
	}

	public static Date convertStringToDate(String format, String time) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateformat.parse(time);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Convert string to calendar
	 * 
	 * @param time
	 * @return
	 */
	public static Calendar convertStringToCalendar(String time, String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateformat.parse(time);
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			return calendar;
		}
	}

	/**
	 * Convert date to string
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDateToString(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String strDate;
		try {
			strDate = dateFormat.format(date);
			return strDate;
		} catch (IllegalArgumentException e) {
			return dateFormat.format(new Date(System.currentTimeMillis()));
		}
	}

	public static String convertDateToString(Calendar calendar, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String strDate;
		try {
			strDate = dateFormat.format(calendar.getTime());
			return strDate;
		} catch (IllegalArgumentException e) {
			return dateFormat.format(new Date(System.currentTimeMillis()));
		}
	}

	/**
	 * 
	 * @param editText
	 * @return
	 */
	// public static String removeEmpty(String editText) {
	// return StringUtils.isBlank(editText) ? "" : editText.replaceAll("null",
	// "");
	// }

	/**
	 * Check input string
	 * 
	 * @param editText
	 * @return
	 */
	// public static boolean isEmpty(String editText) {
	// return StringUtils.isBlank(editText);
	// }

	/**
	 * Merge all elements of a string array into a string
	 * 
	 * @param strings
	 * @param separator
	 * @return
	 */
	// public static String join(String[] strings, String separator) {
	// return StringUtils.join(strings, separator);
	// }

	/**
	 * @param str
	 * @return
	 */
	public static String sqlQuote(String string) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\"");
		stringBuffer.append(string);
		stringBuffer.append("\"");
		return stringBuffer.toString();
	}

	/**
	 * @param long value
	 * @return
	 */
	public static String sqlQuote(long value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\"");
		stringBuffer.append(value);
		stringBuffer.append("\"");
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param int value
	 * @return
	 */
	public static String sqlQuote(int value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\"");
		stringBuffer.append(value);
		stringBuffer.append("\"");
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param int value
	 * @return
	 */
	public static String sqlQuote(double value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\"");
		stringBuffer.append(value);
		stringBuffer.append("\"");
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		try {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static boolean validNumber(String number) {
		return PhoneNumberUtils.isGlobalPhoneNumber(number);
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	public static String removeAllTagHtml(String html) {
		Pattern tags = Pattern.compile("</?[^>]+>");
		Matcher match = tags.matcher(html); // here you specify the string you
											// want to modify (HTML)
		String result = match.replaceAll("");
		return result;
	}

	/**
	 * @param pPhoneNumber
	 * @return true if the phone number is correct
	 */
	public static boolean isPhoneNumberCorrect(String pPhoneNumber) {
		Pattern pattern = Pattern
				.compile("((\\+[1-9]{3,4}|0[1-9]{4}|00[1-9]{3})\\-?)?\\d{8,20}");
		Matcher matcher = pattern.matcher(pPhoneNumber);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static String getDeviceId(Context context) {
		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	public static Date getLastModifiedDate(File file) {
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(file.lastModified());
		date = calendar.getTime();
		return date;
	}

	public static Date addDay(Date date, int numberDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, numberDay);
		return calendar.getTime();
	}

	public static Date getCurrentDate() {
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		date = calendar.getTime();
		return date;
	}

	public static String convertTimeStampToDate(long timeStamp) {
		SimpleDateFormat objFormatter = new SimpleDateFormat(
				TIMESTAMP_DATE_FORMAT_12H);
		objFormatter.setTimeZone(TimeZone.getDefault());

		Calendar objCalendar = Calendar.getInstance(TimeZone.getDefault());
		objCalendar.setTimeInMillis(timeStamp);
		String result = objFormatter.format(objCalendar.getTime());
		objCalendar.clear();
		return result;
	}

	public static String convertDateFormat(String myTime, String dateIn,
			String dateOut) {
		SimpleDateFormat dateFormatIn = new SimpleDateFormat(dateIn);
		SimpleDateFormat dateFormatOut = new SimpleDateFormat(dateOut);
		Date myDate = null;
		try {
			myDate = dateFormatIn.parse(myTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String finalDate = dateFormatOut.format(myDate);
		return finalDate;
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DATE_FORMAT_12H);
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}

	public static String getMonthFromNumber(String month) {
		if (month.equals("1") || month.equals("01")) {
			return JANUARY;
		} else if (month.equals("2") || month.equals("02")) {
			return FEBRUARY;
		} else if (month.equals("3") || month.equals("03")) {
			return MARCH;
		} else if (month.equals("4") || month.equals("04")) {
			return APRIL;
		} else if (month.equals("5") || month.equals("05")) {
			return MAY;
		} else if (month.equals("6") || month.equals("06")) {
			return JUNE;
		} else if (month.equals("7") || month.equals("07")) {
			return JULY;
		} else if (month.equals("8") || month.equals("08")) {
			return AUGUST;
		} else if (month.equals("9") || month.equals("09")) {
			return SEPTEMBER;
		} else if (month.equals("10") || month.equals("10")) {
			return OCTOBER;
		} else if (month.equals("11") || month.equals("11")) {
			return NOVEMBER;
		} else if (month.equals("12") || month.equals("12")) {
			return DECEMBER;
		} else {
			return "";
		}
	}

	public static String convertSecondToString(int totalSecond) {
		String timeString = "";
		int seconds = totalSecond % 60;
		int minutes = (totalSecond / 60) % 60;
		int hours = totalSecond / 60 / 60;
		if (hours > 0) {
			timeString += hours + ":";
		}
		if (minutes > 9) {
			timeString += minutes + ":";
		} else {
			timeString += "0" + minutes + ":";
		}
		if (seconds > 9) {
			timeString += seconds;
		} else {
			timeString += "0" + seconds;
		}

		return timeString;
	}
	
	public static String getCaptcha() {
		Random rd = new Random(System.currentTimeMillis());
		StringBuilder strBuilder = new StringBuilder("");
		String tmp = new String("123456789abcdefghijklmnopqrstuvwxyz");
		for (int i = 0; i < 8; i++) {
			strBuilder.append(tmp.charAt(rd.nextInt(35)));
		}
		return strBuilder.toString();
	}
}
