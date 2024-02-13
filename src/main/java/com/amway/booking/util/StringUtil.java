package com.amway.booking.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


	public static boolean isEmpty(String arg) {
		return arg == null || arg.trim().length() == 0;
	}

	public static boolean isNotEmpty(String arg) {
		return !isEmpty(arg);
	}

	public static boolean isNumeric(String orginal) {
		return isMatch("[0-9]*", orginal);
	}
	
	private static boolean isMatch(String regex, String orginal) {
		if (orginal != null && !orginal.trim().equals("")) {
			Pattern pattern = Pattern.compile(regex);
			Matcher isNum = pattern.matcher(orginal);
			return isNum.matches();
		} else {
			return false;
		}
	}

	public static String upperCaseInitial(String str) {
		str=str.toLowerCase();
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
}
