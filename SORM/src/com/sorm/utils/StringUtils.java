package com.sorm.utils;

/**
 * ��װ�ַ������õĲ���
 * @author chenhongyang
 *
 */
public class StringUtils {
	
	/**
	 * ��Ŀ���ַ�������ĸ��Ϊ��д
	 * @param string  Ŀ���ַ���
	 * @return  ����ĸ��Ϊ��д���ַ���
	 */
	public static String firstChar2UpperCase(String string) {
		// abcd --> ABCD --> Abcd
		return string.toUpperCase().substring(0, 1) + string.substring(1);
	}
	
}















