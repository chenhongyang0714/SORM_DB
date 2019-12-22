package com.sorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sorm.core.DBManager;
	
public class DBConnectionPool {

	/**
	 * ���ӳض���
	 */
	private static List<Connection> pool;
	
	/**
	 * ���������
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPoolMaxSize();
	
	/**
	 * ��С������
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPoolMinSize();
	
	public DBConnectionPool() {
		initPool();
	}
	
	/**
	 * ��ʼ�����ӳأ�ʹ���е��������ﵽ��Сֵ
	 */
	public void initPool() {
		if(pool==null) {
			pool = new ArrayList<Connection>();
		}
		
		while(pool.size() < DBConnectionPool.POOL_MIN_SIZE) {                         
			pool.add(DBManager.createConnection());
			System.out.println("��ʼ�����ӳأ�����������Ϊ�� " + pool.size());
		}
	}
	
	/**
	 * �����ӳ���ȡ�����һ������
	 * @return
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size()-1;  // ��ȡpool�����һ�����ӵ�����
		Connection connection = pool.get(last_index);
		pool.remove(last_index);  // �Ѽ���Ҫȡ����������pool��ɾ��
		return connection;
	}
	
	/**
	 * ֻ�ǽ����ӷŻ�pool������pool�Ѵ����ޣ�������close
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



























