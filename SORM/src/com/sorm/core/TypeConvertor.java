package com.sorm.core;

/**
 * ����java�������ͺ����ݿ��������͵��໥ת��
 * @author chenhongyang
 *
 */
public interface TypeConvertor {
	
	/**
	 * �����ݿ���������ת��Ϊjava����������
	 * @param columeType  ���ݿ��ֶε���������
	 * @return  java����������
	 */
	public String databaseType2JavaType(String columeType);
	
	
	/**
	 * ��java��������ת�������ݿ���������
	 * @param javaDataType  java��������
	 * @return  ���ݿ����������
	 */
	public String javaType2DatabaseType(String javaDataType);
	
	
}
