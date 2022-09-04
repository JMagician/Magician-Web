package com.magician.web.commons.util;

/**
 * String utility
 * @author yuye
 *
 */
public class StringUtil {

	/**
	 * Convert the first letter of a string to lowercase
	 * @param str
	 * @return string
	 */
	public static String getFirstLowerCase(String str) {
		String str2 = str.substring(1);
		String str3 = str.substring(0,1);
		
		return str3.toLowerCase()+str2;
	}
	
	/**
	 * Check if a string is empty
	 * @param obj
	 * @return string
	 */
	public static boolean isNull(Object obj) {
		if (obj == null || obj.toString().trim().equals("")) {
			return true;
		}
		return false;
	}
}
