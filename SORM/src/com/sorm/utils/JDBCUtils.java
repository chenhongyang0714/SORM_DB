package com.sorm.utils;


import java.sql.PreparedStatement;
/**
 * ��װJDBC��ѯ���õĲ���
 * @author chenhongyang
 *
 */
public class JDBCUtils {

	/**
	 * ��sql���
	 * @param preparedStatement  Ԥ����sql������
	 * @param params  ����
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














