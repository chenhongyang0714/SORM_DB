package com.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.sorm.bean.Configuration;
import com.sorm.pool.DBConnectionPool;

/**		
 * �������ݿ��������Ϣ��ά�����Ӷ���Ĺ���(�������ӳع���)
 * @author chenhongyang
 *
 */
public class DBManager {

	// �������ļ��е���Ϣ��ö���װ������
	private static Configuration configuration;
	
	// ���ӳض���
	private static DBConnectionPool pool;
	// ��̬�����(����ָ���������ļ�)
	static { 
		// ��ȡ�ʹ���db.properties�ļ�����Ϣ
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ����properties��ȡ��������Ϣ ��ʼ��configuration����
		configuration = new Configuration();
		configuration.setDriver(properties.getProperty("driver"));
		configuration.setUrl(properties.getProperty("url"));
		configuration.setUser(properties.getProperty("user"));
		configuration.setPwd(properties.getProperty("pwd"));
		configuration.setUsingDB(properties.getProperty("usingDB"));
		configuration.setSrcPath(properties.getProperty("srcPath"));
		configuration.setPoPackage(properties.getProperty("poPackage"));
		configuration.setQueryClass(properties.getProperty("queryClass"));
		configuration.setPoolMaxSize(Integer.parseInt(properties.getProperty("poolMaxSize")));
		configuration.setPoolMinSize(Integer.parseInt(properties.getProperty("poolMinSize")));
			
		
		 //����TableContext��
		System.out.println(TableContext.class);
	}
	
	/**
	 * ����Configuration�������ڲ���������Ϣ
	 * @return  
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	// �����ӳ��л��Connection����
	public static Connection getConnection() {
		
		// ** ���ӳؼ���
		if(pool==null) {
			pool = new DBConnectionPool();
		}
		return pool.getConnection();
		
		// �����ӳؼ���
//		try {
//			// ע������(ʹ��ʲô�����������ݿ�)
//			System.out.println("chen ********************************");
//			Class.forName(configuration.getDriver());
//			// �����ݿ⽨������(�п����׳� SQLException �쳣)
//			return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPwd());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
		
	}
	
	// �����µ�����,�����������ӳش���(���Ч��)
	public static Connection createConnection() {
		try {
			// ע������(ʹ��ʲô�����������ݿ�)
			Class.forName(configuration.getDriver());
			// �����ݿ⽨������(�п����׳� SQLException �쳣)
			return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// �رմ���� Connection ����
	private static void closeConnection(Connection con) {
		try {
			// �����ӳ�ʱʹ��
//			if(con!=null)  // �ر� (�������ĺ�ر�) ���κ�һ����Դ���쳣����Ӱ��������Դ�Ĺرգ�
//				con.close();
			
			// ** ʹ�����ӳؼ���ʱ�ĵ���
			pool.close(con);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// �ر� ResultSet 
	private static void closeResultSet(ResultSet rs) {
		try {
			if(rs!=null)  // �ر� (�������ĺ�ر�) ���κ�һ����Դ���쳣����Ӱ��������Դ�Ĺرգ�
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// �ر� Statement
	private static void closeStatement(Statement stmt) {
		try {
			if(stmt!=null)
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// �ر� PreparedStatement
	static void closePreparedStatement(PreparedStatement pstmt) {
		try {
			if(pstmt!=null)
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �ر���Դ  Statement(rs, stmt, con)
	public static void close(ResultSet rs, Statement stmt, Connection con) {
		
		closeResultSet(rs);
		
		closeStatement(stmt);
		
		closeConnection(con);
		
	}
    
	// �ر���Դ  PreparedStatement(rs, pstmt, con)
	public static void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
		
		closeResultSet(rs);
		
		closePreparedStatement(pstmt);
		 
		closeConnection(con);
		
	}
}
