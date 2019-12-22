package com.sorm.core;

/**
 * mysql数据类型和java数据类型的转换
 * @author chenhongyang
 *
 */
public class MySqlTypeConvertor implements TypeConvertor{

	public String databaseType2JavaType(String columeType) {
		
		if("varchar".equalsIgnoreCase(columeType) || "char".equalsIgnoreCase(columeType)) {
				return "String";
		} else if("int".equalsIgnoreCase(columeType)
			    || "tinyint".equalsIgnoreCase(columeType)
				|| "smallint".equalsIgnoreCase(columeType)
				|| "integer".equalsIgnoreCase(columeType)
				|| "int".equalsIgnoreCase(columeType)) {
					return "Integer";
		} else if("bigint".equalsIgnoreCase(columeType)) {
			return "Long";
		} else if("double".equalsIgnoreCase(columeType) || "float".equalsIgnoreCase(columeType)) {
			return "Double";
		} else if("clob".equalsIgnoreCase(columeType)) {
			return "java.sql.Clob";
		} else if("blob".equalsIgnoreCase(columeType)) {
			return "java.sql.Blob";
		} else if("date".equalsIgnoreCase(columeType)) {
			return "java.util.Date";
		} else if("time".equalsIgnoreCase(columeType)) {
			return "java.util.Time";
		} else if("timestamp".equalsIgnoreCase(columeType)) {
			return "java.util.Timestamp";
		}
		return null;
	}

	public String javaType2DatabaseType(String javaDataType) {
		// TODO Auto-generated method stub
		return null;
	}

}
