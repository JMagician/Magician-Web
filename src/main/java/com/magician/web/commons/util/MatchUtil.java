package com.magician.web.commons.util;

/**
 * Check if a string matches a rule
 * @author yuye
 *
 */
public class MatchUtil {

	/**
	 * Check if a string with wildcards matches another string
	 * @param rule
	 * @param str
	 * @return boolean
	 */
	public static Boolean isMatch(String rule,String str){
		if(rule==null || str == null){
			return false;
		}
		int ind = rule.indexOf("*");
		if(ind>-1){
			if(rule.length()==1){
				return true;
			}else{
				String ru = rule.replaceAll("\\*", "([a-zA-Z1-9/]+)");
				return str.matches(ru);
			}
		}else{
			return rule.equals(str);
		}
	}
}
