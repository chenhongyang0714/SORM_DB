package com.sorm.utils;

/**
 * 封装字符串常用的操作
 * @author chenhongyang
 *
 */
public class StringUtils {
	
	/**
	 * 将目标字符串首字母变为大写
	 * @param string  目标字符串
	 * @return  首字母变为大写的字符串
	 */
	public static String firstChar2UpperCase(String string) {
		// abcd --> ABCD --> Abcd
		return string.toUpperCase().substring(0, 1) + string.substring(1);
	}
	
}















