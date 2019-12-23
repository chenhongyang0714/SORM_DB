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
 * 负责查询(对外提供服务的核心类)
 * @author chenhongyang
 *
 */

@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	
	/**
	 * 采用模板方法模式将JDBC操作封装成模板，便于重用
	 * 多行多列查询模板(已固定死-->不可修改)
	 * queryRows方法模板(晋级,设计回调机制)
	 * @param sql  sql语句
	 * @param params  sql参数
	 * @param clazz  记录要封装到的java类
	 * @param back  CallBack的实现类，实现回调
	 * @return 
	 */
	public Object executeQueryTemplate(String sql, Object[] params, Class clazz, CallBack back) {
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
			
		try {
			preparedStatement = connection.prepareStatement(sql);
			// 给sql设参
			JDBCUtils.handleParams(preparedStatement, params);
			System.out.println(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			
			return back.doExecute(connection, preparedStatement, resultSet);  // 相当于占位符
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(resultSet, preparedStatement, connection);
		}
		
	}
	
	/**
	 * 直接执行一个DML语句
	 * @param sql  sql语句
	 * @param params  参数 
	 * @return  执行sql语句后影响记录的行数
	 */
	public int executeDML(String sql, Object[] params) {
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		int count = 0;  // 记录数据库中受影响的行数
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			
			//给sql设参
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
	
	/**从对象到库
	 * 将一个对象存储到数据库中
	 * 把对象中不为null的属性往数据库中存储 如果数字为null则放0
	 * 通过修改类中的属性，进而修改相对应的数据库中的键值
	 * @param obj  要存储的对象
	 */
	public void insert(Object obj) {
		// obj --> 表中    insert into 表名 (id, name) values(?,?);
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		
		List<Object> params = new ArrayList<Object>();  // 存储sql的属性的值
		
		StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + " (");
		
		int countNotNullField = 0;  // 用于记录表中该对象不为空的属性个数(以便生成？的个数)
		
		Field [] fs = c.getDeclaredFields();  // 获取表对应的类 中所有的属性
		
		for(Field field : fs) {
			String fieldName = field.getName();  // 获得属性名称
			Object fieldValue = ReflectUtils.invokeGet(obj, fieldName);  // 获取obj对象fieldName属性的值
			
			System.out.println(fieldName + "  " + fieldValue);  // 测试用
			
			if(fieldValue != null) {
				countNotNullField ++;
				sql.append(fieldName + ",");  // 生成sql语句 id, name,
				params.add(fieldValue);  // 添加value
			}
		}
		
		sql.setCharAt(sql.length()-1, ')');  // 将此时的sql语句的最后一个字符改为 ')'
		sql.append(" values (");
		for(int i=0;i<countNotNullField;i++) {
			sql.append("?,");
		}
		
		sql.setCharAt(sql.length()-1, ')');  // 将此时的sql语句的最后一个字符改为 ')'
		sql.append(";");
		
		executeDML(sql.toString(), params.toArray());
	}
	
	/**从对象到库
	 * 删除clazz表示类对应的表中的记录(指定主键值id的记录)
	 * @param clazz  跟表对应的类的Class对象
	 * @param id  主键的值
	 */
	public void delete(Class clazz, Object id) {  // delete from User where id=2;
		// Qqq.class, 2 --> delete from qqq where id = 2;
		
		// 通过Class对象找与之对应的TableInfo
		TableInfo tableInfo = TableContext.poClassMap.get(clazz);
		// 获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";
		
		executeDML(sql, new Object[] {id});
	}
	
	/**从对象到库
	 * 删除对象在数据库中对应的记录(对象所在的类对应到表，对象的主键的值对应到记录)
	 * @param obj
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();  // 返回此 Object 的运行时类
		// 通过Class对象找与之对应的TableInfo
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		// 获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();  
		
		// onlyPriKey.getName() 为 获取主键名称
		Object priKeyValue = ReflectUtils.invokeGet(obj, onlyPriKey.getName());
//		System.out.println(priKeyValue);  // 主键的值
		delete(c, priKeyValue);
	}
	
	/**从对象到库
	 * 更新对象对应的记录，并且只更新指定的字段的值
	 * @param object  所要更新的对象
	 * @param fieldNames  更新的属性列表
	 * @return  执行sql语句后影响记录的行数
	 */
	public int update(Object object, String[] fieldNames) {  // update users set uname=?,pwd=?
		// obj{"uname", "pwd"}  --> update 表名 set uname=?,pwd=? where id=?;
		
		Class c = object.getClass();
		TableInfo tableInfo = TableContext.poClassMap.get(c);
		
		List<Object> params = new ArrayList<Object>();  // 存储sql的属性的值
		
		ColumnInfo columnInfo = tableInfo.getOnlyPriKey();  // 获取唯一主键
		
		StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set ");
		 
		for(String filedName : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(object, filedName);  // 获取po类中相应属性的值，然后再加载到数据库中
			params.add(fvalue);  // 添加属性的值
			sql.append(filedName + "=?,");
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append("where " + columnInfo.getName() + "=?;");
		
		params.add(ReflectUtils.invokeGet(object, columnInfo.getName()));  // 添加qqq 主键的值
		
		return executeDML(sql.toString(), params.toArray());
	}
	
	
	/**从库到对象(查询的方法)
	 * 查询返回多行记录，并将每行记录封装到clazz指定的类的对象中
	 * 执行顺序：queryRows --> executeQueryTemplate --> doExecute -->executeQueryTemplate-->queryRows--> return list（得到结果）
	 * @param sql  查询语句
	 * @param clazz  封装数据的javabean类的Class对象
	 * @param params  sql的参数
	 * @return  查询到的结果
	 */
	public List queryRows(final String sql, final Class clazz, final Object[] params) {
		
		final List list = new ArrayList();  // list中存放clazz的对象(对象的属性存放表中的数据)
		
		return (List)executeQueryTemplate(sql, params, clazz, new CallBack() {
			
			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
				
				try {
					// 查询的方法 “需要查询时写”
					ResultSetMetaData metaData = resultSet.getMetaData();  // 获取元数据相关信息
					// 多行
					while(resultSet.next()) {
						// 每一行声明一个对象
						Object rowObj = clazz.newInstance();  // 调用Javabean的无参构造器(生成一个对象)
						
						// 多列 select username, pwd, age from user where id>? and age>18;
						for(int i=0; i<metaData.getColumnCount();i++) {
							// id name
							//  1  chy
							String columeName = metaData.getColumnLabel(i+1);  // 获取列名  username
							Object columeValue = resultSet.getObject(i+1);  // 对应的值
							// 调用rowObj对象的setUsername(String username)方法，将columnValue的值设置进去
//						System.out.println(columeName + " " + columeValue);  // 测试用
							ReflectUtils.invokeSet(rowObj, columeName, columeValue);
						}
						list.add(rowObj);  // list中存放clazz的对象(对象的属性存放表中的数据)
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return list;
			}
		});
		
	}


	/**从库到对象
	 * 查询返回一行记录，并将该记录封装到clazz指定的类的对象中
	 * @param sql  查询语句
	 * @param clazz  封装数据的javabean类的Class对象
	 * @param params  sql的参数
	 * @return  查询到的结果
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
			
			ResultSetMetaData metaData = resultSet.getMetaData();  // 获取元数据相关信息
			
			Object rowObj = clazz.newInstance();  // 调用Javabean的无参构造器(生成一个对象)
			
			for(int i=0;i<metaData.getColumnCount();i++) {
				String columeName = metaData.getColumnLabel(i+1);  // 获取列名
				System.out.println(columeName);
				Object columeValue = resultSet.getObject(i+1);  // 对应的值
//				System.out.println(columeName + " " + columeValue);
				ReflectUtils.invokeSet(rowObj, columeName, columeValue);
				
				list.add(rowObj);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(resultSet, preparedStatement, connection);
		}
		return list;  // 如果未查询到任何信息，则list为空
	}

	
	public List queryUniqyeRowMyTeacher(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list==null && list.size()==0)?null:list;
	}
	
	
	/** 从库到对象
	 * 查询返回一个值(一行一列)，并将该值返回
	 * @param sql  查询语句
	 * @param params  sql的参数
	 * @return  查询到的结果
	 */
	public Object queryValue(final String sql, final Object[] params) {
		
		return (Object)executeQueryTemplate(sql, params, null, new CallBack() {
			
			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
				Object value = null;  // 存储查询结果的对象
				try {
					while(resultSet.next()) {
						value = resultSet.getObject(1);  // 获取该值
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return value;
			}
		});
	}
	
	
	/**从库到对象
	 * 查询返回一个数字(一行一列)，并将该值返回
	 * @param sql  查询语句
	 * @param params  sql的参数
	 * @return  查询到的数字
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number)new MySqlQuery().queryValue(sql, params);
	}
	
	
	/**
	 * 	分页查询
	 * @param pageNum  第几页数据
	 * @param size  每页显示多少记录
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);
	
	/**
	 * 实现克隆
	 */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * 根据主键的值直接查找对应的对象
	 * @param clazz  po类
	 * @param id  id(主键)
	 * @return  查询的结果  	
	 */
	public Object queryById(Class clazz, Object id) {	
		// 通过Class对象找与之对应的TableInfo
		TableInfo tableInfo = TableContext.poClassMap.get(clazz);
		// 获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		String sql = "select *  from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";
		
		return queryUniqyeRowMyTeacher(sql, clazz, new Object[] {id});
	}
	
}























