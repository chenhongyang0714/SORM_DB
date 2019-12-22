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
 * 根据数据库的配置信息，维持连接对象的管理(增加连接池功能)
 * @author chenhongyang
 *
 */
public class DBManager {

	// 将配置文件中的信息与该对象装配起来
	private static Configuration configuration;
	
	// 连接池对象
	private static DBConnectionPool pool;
	// 静态代码块(加载指定的配置文件)
	static { 
		// 读取和处理db.properties文件的信息
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 根据properties读取的配置信息 初始化configuration对象
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
			
		
		 //加载TableContext类
		System.out.println(TableContext.class);
	}
	
	/**
	 * 返回Configuration对象，用于操作配置信息
	 * @return  
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	// 从连接池中获得Connection对象
	public static Connection getConnection() {
		
		// ** 连接池技术
		if(pool==null) {
			pool = new DBConnectionPool();
		}
		return pool.getConnection();
		
		// 非连接池技术
//		try {
//			// 注册驱动(使用什么驱动连接数据库)
//			System.out.println("chen ********************************");
//			Class.forName(configuration.getDriver());
//			// 与数据库建立连接(有可能抛出 SQLException 异常)
//			return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPwd());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
		
	}
	
	// 创建新的连接,后期增加连接池处理(提高效率)
	public static Connection createConnection() {
		try {
			// 注册驱动(使用什么驱动连接数据库)
			Class.forName(configuration.getDriver());
			// 与数据库建立连接(有可能抛出 SQLException 异常)
			return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// 关闭传入的 Connection 对象
	private static void closeConnection(Connection con) {
		try {
			// 非连接池时使用
//			if(con!=null)  // 关闭 (先声明的后关闭) （任何一个资源的异常不会影响其他资源的关闭）
//				con.close();
			
			// ** 使用连接池技术时的调用
			pool.close(con);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 关闭 ResultSet 
	private static void closeResultSet(ResultSet rs) {
		try {
			if(rs!=null)  // 关闭 (先声明的后关闭) （任何一个资源的异常不会影响其他资源的关闭）
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 关闭 Statement
	private static void closeStatement(Statement stmt) {
		try {
			if(stmt!=null)
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 关闭 PreparedStatement
	static void closePreparedStatement(PreparedStatement pstmt) {
		try {
			if(pstmt!=null)
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 关闭资源  Statement(rs, stmt, con)
	public static void close(ResultSet rs, Statement stmt, Connection con) {
		
		closeResultSet(rs);
		
		closeStatement(stmt);
		
		closeConnection(con);
		
	}
    
	// 关闭资源  PreparedStatement(rs, pstmt, con)
	public static void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
		
		closeResultSet(rs);
		
		closePreparedStatement(pstmt);
		 
		closeConnection(con);
		
	}
}
