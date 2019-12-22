package com.sorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sorm.core.DBManager;
	
public class DBConnectionPool {

	/**
	 * 连接池对象
	 */
	private static List<Connection> pool;
	
	/**
	 * 最大连接数
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPoolMaxSize();
	
	/**
	 * 最小连接数
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPoolMinSize();
	
	public DBConnectionPool() {
		initPool();
	}
	
	/**
	 * 初始化连接池，使池中的连接数达到最小值
	 */
	public void initPool() {
		if(pool==null) {
			pool = new ArrayList<Connection>();
		}
		
		while(pool.size() < DBConnectionPool.POOL_MIN_SIZE) {                         
			pool.add(DBManager.createConnection());
			System.out.println("初始化连接池，池中连接数为： " + pool.size());
		}
	}
	
	/**
	 * 从连接池中取出最后一个连接
	 * @return
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size()-1;  // 获取pool中最后一个连接的索引
		Connection connection = pool.get(last_index);
		pool.remove(last_index);  // 把即将要取出的连接在pool中删掉
		return connection;
	}
	
	/**
	 * 只是将连接放回pool，但是pool已达上限，则将连接close
	 * @param connection
	 */
	public synchronized void close(Connection connection) {
		if(pool.size() > DBConnectionPool.POOL_MAX_SIZE) {
			if(connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			pool.add(connection);
		}
	}
	
}



























