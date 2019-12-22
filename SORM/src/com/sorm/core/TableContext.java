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

/**  �Ѷ�ϵ���ϴ󣬿ɹ��Ժ�ѧϰ(�ֽ׶�ֱ����)
 * �����ȡ����  ���ݿ����б�ṹ����ṹ�Ĺ�ϵ�������Ը��� ��ṹ������ṹ
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class TableContext {

	/**
	 * ���ɵ�Դ����������Ӧ�ı��Ӧ
	 * ����ΪKey, ����Ϣ����ΪValue(��ֵ��)
	 */
	public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
	
	/**
	 * ��po��class����ͱ���Ϣ�����������������ã�һ�������洢��һ�ű������Դ����Ϣ��
	 */
	public static Map<Class, TableInfo> poClassMap = new HashMap<Class, TableInfo>();
	
	private TableContext() {}  // ������˽�л�(private���޲ι�����-->��ĵط���Ȩnew����Ķ����൱�ڽ����������˽�л�)
	
	// ������Ϣ���ؽ���
	static {
		try {
			// ��ʼ����ñ����Ϣ
			Connection connection = DBManager.getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();  // ������ݿ��Դ��Ϣ(�������е�������Ϣ)
			
			ResultSet tableRet = dbmd.getTables(null, "%","%",new String[]{"TABLE"});  // ��ȡ���б������
			
			while(tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");  // ��ȡ����
				
				// ��ʼ����TableInfo�Ķ���ti�ı�������
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());  
				
				tables.put(tableName, ti);  // �� (������TableInfo�Ķ���) ��ӵ�Map<String, TableInfo>��
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  //��ѯ���е������ֶ�
				while(set.next()){  // ��ȡ�����ֶε���Ϣ
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}
					
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  // ��ѯ���е�����
				while(set2.next()) {
					ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);  //����Ϊ��������
					ti.getPriKeys().add(ci2);
				}
				if(ti.getPriKeys().size()>0){  //ȡΨһ������������ʹ�á������������������Ϊ�գ�
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	    // ������ṹ
		updateJavaPOField();
		
		//����po���������е���(ʹ��Class������TableInfo���Ӧ)���������ã����Ч�ʣ�
		loadPOTables();
	}
	
	/**
	 * ���ݱ�ṹ������po���µ�java�ļ�
	 * ʵ���˴ӱ�ṹ����ṹ��ת��
	 */
	public static void updateJavaPOField() {
		Map<String, TableInfo> map = TableContext.tables;  // mapΪ��tablesһ��������   
		for(TableInfo tableInfo : map.values()) {
			JavaFileUtils.createJavaPOFile(tableInfo, new MySqlTypeConvertor());
		}
	}
	
	/**
	 * ����po���µ��࣬����Ӧ�ı���Ϣ��Ӧ
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











