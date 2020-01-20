package org.yez.easyweb.util;

public class StringUtils {

	public static boolean isNotEmpty(String string){
		return string != null ? !string.trim().equals("") : false;
	}
}
