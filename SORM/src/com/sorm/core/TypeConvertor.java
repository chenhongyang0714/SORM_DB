package com.sorm.core;

/**
 * 负责java数据类型和数据库数据类型的相互转化
 * @author chenhongyang
 *
 */
public interface TypeConvertor {
	
	/**
	 * 将数据库数据类型转换为java的数据类型
	 * @param columeType  数据库字段的数据类型
	 * @return  java的数据类型
	 */
	public String databaseType2JavaType(String columeType);
	
	
	/**
	 * 将java数据类型转化成数据库数据类型
	 * @param javaDataType  java数据类型
	 * @return  数据库的数据类型
	 */
	public String javaType2DatabaseType(String javaDataType);
	
	
}
