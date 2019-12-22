package com.sorm.utils;


import java.sql.PreparedStatement;
/**
 * 封装JDBC查询常用的操作
 * @author chenhongyang
 *
 */
public class JDBCUtils {

	/**
	 * 给sql设参
	 * @param preparedStatement  预编译sql语句对象
	 * @param params  参数
	 */
	public static void handleParams(PreparedStatement preparedStatement, Object[] params) {
		
		if(params!=null) {
			for(int i=0;i<params.length;i++) {
				try {
					preparedStatement.setObject(i+1, params[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}














