package com.sorm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

import com.sorm.bean.ColumnInfo;
import com.sorm.bean.JavaFieldGetSet;
import com.sorm.bean.TableInfo;
import com.sorm.core.DBManager;
import com.sorm.core.MySqlTypeConvertor;
import com.sorm.core.TableContext;
import com.sorm.core.TypeConvertor;

/**
 * ��װ ����Java�ļ�(Դ����)���õĲ���	
 * @author chenhongyang
 *
 */
public class JavaFileUtils {

	/**
	 * �����ֶ���Ϣ����java������Ϣ���磺varchar username --> private String username;�Լ���Ӧ��set��get����
	 * @param columnInfo  �ֶ���Ϣ
	 * @param typeConvertor  ����ת����
	 * @return  java���Ժ�set��get����Դ��
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo, TypeConvertor typeConvertor) {
		JavaFieldGetSet javaFieldGetSet = new JavaFieldGetSet();
		
		String javaFieldType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());
		
		// ����������Ϣ
		javaFieldGetSet.setFieldInfo("\tprivate " + javaFieldType + " " + columnInfo.getName() + ";\n");
		
		// ����get����Դ��
		// public String getUsername(){return username;}
		StringBuffer stringBuffer = new StringBuffer();  // �����ַ����Ĳ���
		stringBuffer.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(columnInfo.getName() + "() {\n"));
		stringBuffer.append("\t\treturn " + columnInfo.getName() + ";\n");
		stringBuffer.append("\t}\n");
		javaFieldGetSet.setGetInfo(stringBuffer.toString());
		
		// ����set����Դ��
		// public void setUsername(){this.username = username;}
		StringBuffer stringBuffer1 = new StringBuffer();
		stringBuffer1.append("\tpublic void set" + StringUtils.firstChar2UpperCase(columnInfo.getName() + 
				"(" + typeConvertor.databaseType2JavaType(columnInfo.getDataType()) + " " + columnInfo.getName() +") {\n"));
		stringBuffer1.append("\t\tthis." + columnInfo.getName() + " = " + columnInfo.getName() + ";\n");
		stringBuffer1.append("\t}\n");
		javaFieldGetSet.setSetInfo(stringBuffer1.toString());
		return javaFieldGetSet;
	}
	
	/**
	 * ���ݱ���Ϣ����java���Դ����(��-->���ݿ��е�table)
	 * @param tableInfo  ����Ϣ
	 * @param convertor  ��������ת����
	 * @return  java���Դ����
	 */ 
	public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor) {
		Map<String, ColumnInfo> columns = tableInfo.getColumns();  // ȡ������Ϣ����ֶ���Ϣ
		List <JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();  // �����б�
		
		// һ������Ϣ���ж���ֶ���Ϣ(������Ϣ��set��get����)
		for(ColumnInfo c : columns.values()) {
			javaFields.add(createFieldGetSetSRC(c, convertor));  // ȡ������Ϣ�������ֶ���Ϣ��������Ϣ��get��set��������ӵ�javaFields��
		}
		
		StringBuilder src = new StringBuilder();
		
		// ����package���
		src.append("package " + DBManager.getConfiguration().getPoPackage() + ";" + "\n\n");
		// ����import���
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		// �������������
		src.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + " {\n\n");
		// ���������б�(һ���ֶ�һ���ֶε�ȡ)
		for(JavaFieldGetSet javaFieldGetSet : javaFields) {
			src.append(javaFieldGetSet.getFieldInfo());
		}
		src.append("\n\n");
		// ����get�����б�
		for(JavaFieldGetSet javaFieldGetSet : javaFields) {
			src.append(javaFieldGetSet.getGetInfo());
		}
		// ����set�����б�
		for(JavaFieldGetSet javaFieldGetSet : javaFields) {
			src.append(javaFieldGetSet.getSetInfo());
		}
		// ����������� '}'
		src.append("}\n");
		
//		System.out.println(src);
		return src.toString();
	}
	
	/**
	 * ��Դ��д��ָ��·��
	 * @param tableInfo  ����Ϣ
	 * @param typeConvertor  ����ת������
	 */
	public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor typeConvertor) {
		String string = createJavaSrc(tableInfo, typeConvertor);  // ��ȡJava���Դ��
		
		// ��ȡ���Դ���·��
		String srcPath = DBManager.getConfiguration().getSrcPath() + "\\";  
		String packagePath = DBManager.getConfiguration().getPoPackage().replaceAll("\\.", "\\\\");
		String totalString = srcPath + packagePath + "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname() + ".java");
				
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(totalString));  // ��������������Ķ���(���û��ָ����·�������Զ�����)
			bw.write(string);  // ��Դ��д��
			System.out.println("������" + tableInfo.getTname() + "��Ӧ��java�ࣺ" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		// ������(�������ԡ�get��set) 
//		ColumnInfo columnInfo = new ColumnInfo("chy", "int", 0);
//		JavaFieldGetSet javaFieldGetSet = createFieldGetSetSRC(columnInfo, new MySqlTypeConvertor());
//		System.out.println(javaFieldGetSet.toString());
		
		// ����chy1��qqq���Դ��
//		Map<String, TableInfo> map = TableContext.tables;  // mapΪ��tablesһ��������(��ʱmap���������ݿ������б����Ϣ)
//		TableInfo tableInfo = map.get("qqq");
//		createJavaSrc(tableInfo, new MySqlTypeConvertor());
		
		// ����Դ��·��
		Map<String, TableInfo> map = TableContext.tables;  // mapΪ��tablesһ��������   
		for(TableInfo tableInfo : map.values()) {
			createJavaPOFile(tableInfo, new MySqlTypeConvertor());
		}
	}
}


















