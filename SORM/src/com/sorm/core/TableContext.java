package com.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sorm.bean.ColumnInfo;
import com.sorm.bean.TableInfo;
import com.sorm.utils.JavaFileUtils;
import com.sorm.utils.StringUtils;

/**  难度系数较大，可供以后学习(现阶段直接用)
 * 负责获取管理  数据库所有表结构和类结构的关系，并可以根据 表结构生成类结构
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class TableContext {

	/**
	 * 生成的源码类名与相应的表对应
	 * 表名为Key, 表信息对象为Value(键值对)
	 */
	public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
	
	/**
	 * 将po的class对象和表信息关联起来，便于重用（一个类对象存储有一张表的所有源码信息）
	 */
	public static Map<Class, TableInfo> poClassMap = new HashMap<Class, TableInfo>();
	
	private TableContext() {}  // 将属性私有化(private的无参构造器-->别的地方无权new该类的对象，相当于将该类的属性私有化)
	
	// 将表信息加载进来
	static {
		try {
			// 初始化获得表的信息
			Connection connection = DBManager.getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();  // 获得数据库的源信息(包含表中的所有信息)
			
			ResultSet tableRet = dbmd.getTables(null, "%","%",new String[]{"TABLE"});  // 获取所有表的数据
			
			while(tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");  // 获取表名
				
				// 初始化类TableInfo的对象ti的表名属性
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());  
				
				tables.put(tableName, ti);  // 将 (表名、TableInfo的对象) 添加到Map<String, TableInfo>中
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  //查询表中的所有字段
				while(set.next()){  // 获取表中字段的信息
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}
					
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  // 查询表中的主键
				while(set2.next()) {
					ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);  //设置为主键类型
					ti.getPriKeys().add(ci2);
				}
				if(ti.getPriKeys().size()>0){  //取唯一主键。。方便使用。如果是联合主键。则为空！
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	    // 更新类结构
		updateJavaPOField();
		
		//加载po包下面所有的类(使得Class对象与TableInfo相对应)，便于重用，提高效率！
		loadPOTables();
	}
	
	/**
	 * 根据表结构，更新po包下的java文件
	 * 实现了从表结构到类结构的转化
	 */
	public static void updateJavaPOField() {
		Map<String, TableInfo> map = TableContext.tables;  // map为与tables一样的容器   
		for(TableInfo tableInfo : map.values()) {
			JavaFileUtils.createJavaPOFile(tableInfo, new MySqlTypeConvertor());
		}
	}
	
	/**
	 * 加载po包下的类，与相应的表信息对应
	 */
	public static void loadPOTables() {
		for(TableInfo tableInfo : tables.values()) {
			try {
				Class class1 = Class.forName(DBManager.getConfiguration().getPoPackage() + "." + StringUtils.firstChar2UpperCase(tableInfo.getTname()));
				poClassMap.put(class1, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
//		 Map<String,TableInfo>  tables = TableContext.tables;  
//		 System.out.println(tables);
		
		TableContext.updateJavaPOField();
	}
}











