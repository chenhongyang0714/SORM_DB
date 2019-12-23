package com.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sorm.bean.ColumnInfo;
import com.sorm.bean.TableInfo;
import com.sorm.utils.JDBCUtils;
import com.sorm.utils.ReflectUtils;

/**
 * �����ѯ(�����ṩ����ĺ�����)
 * @author chenhongyang
 *
 */

@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	
	/**
	 * ����ģ�巽��ģʽ��JDBC������װ��ģ�壬��������
	 * ���ж��в�ѯģ��(�ѹ̶���-->�����޸�)
	 * queryRows����ģ��(����,��ƻص�����)
	 * @param sql  sql���
	 * @param params  sql����
	 * @param clazz  ��¼Ҫ��װ����java��
	 * @param back  CallBack��ʵ���࣬ʵ�ֻص�
	 * @return 
	 */
	public Object executeQueryTemplate(String sql, Object[] params, Class clazz, CallBack back) {
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
			
		try {
			preparedStatement = connection.prepareStatement(sql);
			// ��sql���
			JDBCUtils.handleParams(preparedStatement, params);
			System.out.println(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			
			return back.doExecute(connection, preparedStatement, resultSet);  // �൱��ռλ��
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(resultSet, preparedStatement, connection);
		}
		
	}
	
	/**
	 * ֱ��ִ��һ��DML���
	 * @param sql  sql���
	 * @param params  ���� 
	 * @return  ִ��sql����Ӱ���¼������
	 */
	public int executeDML(String sql, Object[] params) {
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		int count = 0;  // ��¼���ݿ�����Ӱ�������
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			
			//��sql���
			JDBCUtils.handleParams(preparedStatement, params);
			System.out.println(preparedStatement);
			count = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.closePreparedStatement(preparedStatement);
		}
		
		return count;
	}
	
	/**�Ӷ��󵽿�
	 * ��һ������洢�����ݿ���
	 * �Ѷ����в�Ϊnull�����������ݿ��д洢 �������Ϊnull���0
	 * ͨ���޸����е����ԣ������޸����Ӧ�����ݿ��еļ�ֵ
	 * @param obj  Ҫ�洢�Ķ���
	 */
	public void insert(Object obj) {
		// obj --> ����    insert into ���� (id, name) values(?,?);
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		
		List<Object> params = new ArrayList<Object>();  // �洢sql�����Ե�ֵ
		
		StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + " (");
		
		int countNotNullField = 0;  // ���ڼ�¼���иö���Ϊ�յ����Ը���(�Ա����ɣ��ĸ���)
		
		Field [] fs = c.getDeclaredFields();  // ��ȡ���Ӧ���� �����е�����
		
		for(Field field : fs) {
			String fieldName = field.getName();  // �����������
			Object fieldValue = ReflectUtils.invokeGet(obj, fieldName);  // ��ȡobj����fieldName���Ե�ֵ
			
			System.out.println(fieldName + "  " + fieldValue);  // ������
			
			if(fieldValue != null) {
				countNotNullField ++;
				sql.append(fieldName + ",");  // ����sql��� id, name,
				params.add(fieldValue);  // ���value
			}
		}
		
		sql.setCharAt(sql.length()-1, ')');  // ����ʱ��sql�������һ���ַ���Ϊ ')'
		sql.append(" values (");
		for(int i=0;i<countNotNullField;i++) {
			sql.append("?,");
		}
		
		sql.setCharAt(sql.length()-1, ')');  // ����ʱ��sql�������һ���ַ���Ϊ ')'
		sql.append(";");
		
		executeDML(sql.toString(), params.toArray());
	}
	
	/**�Ӷ��󵽿�
	 * ɾ��clazz��ʾ���Ӧ�ı��еļ�¼(ָ������ֵid�ļ�¼)
	 * @param clazz  �����Ӧ�����Class����
	 * @param id  ������ֵ
	 */
	public void delete(Class clazz, Object id) {  // delete from User where id=2;
		// Qqq.class, 2 --> delete from qqq where id = 2;
		
		// ͨ��Class��������֮��Ӧ��TableInfo
		TableInfo tableInfo = TableContext.poClassMap.get(clazz);
		// �������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";
		
		executeDML(sql, new Object[] {id});
	}
	
	/**�Ӷ��󵽿�
	 * ɾ�����������ݿ��ж�Ӧ�ļ�¼(�������ڵ����Ӧ���������������ֵ��Ӧ����¼)
	 * @param obj
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();  // ���ش� Object ������ʱ��
		// ͨ��Class��������֮��Ӧ��TableInfo
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		// �������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();  
		
		// onlyPriKey.getName() Ϊ ��ȡ��������
		Object priKeyValue = ReflectUtils.invokeGet(obj, onlyPriKey.getName());
//		System.out.println(priKeyValue);  // ������ֵ
		delete(c, priKeyValue);
	}
	
	/**�Ӷ��󵽿�
	 * ���¶����Ӧ�ļ�¼������ֻ����ָ�����ֶε�ֵ
	 * @param object  ��Ҫ���µĶ���
	 * @param fieldNames  ���µ������б�
	 * @return  ִ��sql����Ӱ���¼������
	 */
	public int update(Object object, String[] fieldNames) {  // update users set uname=?,pwd=?
		// obj{"uname", "pwd"}  --> update ���� set uname=?,pwd=? where id=?;
		
		Class c = object.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		
		List<Object> params = new ArrayList<Object>();  // �洢sql�����Ե�ֵ
		
		ColumnInfo columnInfo = tableInfo.getOnlyPriKey();  // ��ȡΨһ����
		
		StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set ");
		 
		for(String filedName : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(object, filedName);  // ��ȡpo������Ӧ���Ե�ֵ��Ȼ���ټ��ص����ݿ���
			params.add(fvalue);  // ������Ե�ֵ
			sql.append(filedName + "=?,");
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append("where " + columnInfo.getName() + "=?;");
		
		params.add(ReflectUtils.invokeGet(object, columnInfo.getName()));  // ���qqq ������ֵ
		
		return executeDML(sql.toString(), params.toArray());
	}
	
	
	/**�ӿ⵽����(��ѯ�ķ���)
	 * ��ѯ���ض��м�¼������ÿ�м�¼��װ��clazzָ������Ķ�����
	 * ִ��˳��queryRows --> executeQueryTemplate --> doExecute -->executeQueryTemplate-->queryRows--> return list���õ������
	 * @param sql  ��ѯ���
	 * @param clazz  ��װ���ݵ�javabean���Class����
	 * @param params  sql�Ĳ���
	 * @return  ��ѯ���Ľ��
	 */
	public List queryRows(final String sql, final Class clazz, final Object[] params) {
		
		final List list = new ArrayList();  // list�д��clazz�Ķ���(��������Դ�ű��е�����)
		
		return (List)executeQueryTemplate(sql, params, clazz, new CallBack() {
			
			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
				
				try {
					// ��ѯ�ķ��� ����Ҫ��ѯʱд��
					ResultSetMetaData metaData = resultSet.getMetaData();  // ��ȡԪ���������Ϣ
					// ����
					while(resultSet.next()) {
						// ÿһ������һ������
						Object rowObj = clazz.newInstance();  // ����Javabean���޲ι�����(����һ������)
						
						// ���� select username, pwd, age from user where id>? and age>18;
						for(int i=0; i<metaData.getColumnCount();i++) {
							// id name
							//  1  chy
							String columeName = metaData.getColumnLabel(i+1);  // ��ȡ����  username
							Object columeValue = resultSet.getObject(i+1);  // ��Ӧ��ֵ
							// ����rowObj�����setUsername(String username)��������columnValue��ֵ���ý�ȥ
//						System.out.println(columeName + " " + columeValue);  // ������
							ReflectUtils.invokeSet(rowObj, columeName, columeValue);
						}
						list.add(rowObj);  // list�д��clazz�Ķ���(��������Դ�ű��е�����)
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return list;
			}
		});
		
	}


	/**�ӿ⵽����
	 * ��ѯ����һ�м�¼�������ü�¼��װ��clazzָ������Ķ�����
	 * @param sql  ��ѯ���
	 * @param clazz  ��װ���ݵ�javabean���Class����
	 * @param params  sql�Ĳ���
	 * @return  ��ѯ���Ľ��
	 */
	public List queryUniqyeRowMyself(String sql, Class clazz, Object[] params) {
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List list = null;
		
		try {
			if(list == null) {
				list = new ArrayList();
			}
			preparedStatement = connection.prepareStatement(sql);
			JDBCUtils.handleParams(preparedStatement, params);
			resultSet = preparedStatement.executeQuery();
			
			ResultSetMetaData metaData = resultSet.getMetaData();  // ��ȡԪ���������Ϣ
			
			Object rowObj = clazz.newInstance();  // ����Javabean���޲ι�����(����һ������)
			
			for(int i=0;i<metaData.getColumnCount();i++) {
				String columeName = metaData.getColumnLabel(i+1);  // ��ȡ����
				System.out.println(columeName);
				Object columeValue = resultSet.getObject(i+1);  // ��Ӧ��ֵ
//				System.out.println(columeName + " " + columeValue);
				ReflectUtils.invokeSet(rowObj, columeName, columeValue);
				
				list.add(rowObj);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(resultSet, preparedStatement, connection);
		}
		return list;  // ���δ��ѯ���κ���Ϣ����listΪ��
	}

	
	public List queryUniqyeRowMyTeacher(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list==null && list.size()==0)?null:list;
	}
	
	
	/** �ӿ⵽����
	 * ��ѯ����һ��ֵ(һ��һ��)��������ֵ����
	 * @param sql  ��ѯ���
	 * @param params  sql�Ĳ���
	 * @return  ��ѯ���Ľ��
	 */
	public Object queryValue(final String sql, final Object[] params) {
		
		return (Object)executeQueryTemplate(sql, params, null, new CallBack() {
			
			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
				Object value = null;  // �洢��ѯ����Ķ���
				try {
					while(resultSet.next()) {
						value = resultSet.getObject(1);  // ��ȡ��ֵ
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return value;
			}
		});
	}
	
	
	/**�ӿ⵽����
	 * ��ѯ����һ������(һ��һ��)��������ֵ����
	 * @param sql  ��ѯ���
	 * @param params  sql�Ĳ���
	 * @return  ��ѯ��������
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number)new MySqlQuery().queryValue(sql, params);
	}
	
	
	/**
	 * 	��ҳ��ѯ
	 * @param pageNum  �ڼ�ҳ����
	 * @param size  ÿҳ��ʾ���ټ�¼
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);
	
	/**
	 * ʵ�ֿ�¡
	 */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * ����������ֱֵ�Ӳ��Ҷ�Ӧ�Ķ���
	 * @param clazz  po��
	 * @param id  id(����)
	 * @return  ��ѯ�Ľ��  	
	 */
	public Object queryById(Class clazz, Object id) {	
		// ͨ��Class��������֮��Ӧ��TableInfo
		TableInfo tableInfo = TableContext.poClassMap.get(clazz);
		// �������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		String sql = "select *  from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";
		
		return queryUniqyeRowMyTeacher(sql, clazz, new Object[] {id});
	}
	
}























