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
 * 封装 生成Java文件(源代码)常用的操作	
 * @author chenhongyang
 *
 */
public class JavaFileUtils {

	/**
	 * 根据字段信息生成java属性信息。如：varchar username --> private String username;以及相应的set、get方法
	 * @param columnInfo  字段信息
	 * @param typeConvertor  类型转化器
	 * @return  java属性和set、get方法源码
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo, TypeConvertor typeConvertor) {
		JavaFieldGetSet javaFieldGetSet = new JavaFieldGetSet();
		
		String javaFieldType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());
		
		// 生成属性信息
		javaFieldGetSet.setFieldInfo("\tprivate " + javaFieldType + " " + columnInfo.getName() + ";\n");
		
		// 生成get方法源码
		// public String getUsername(){return username;}
		StringBuffer stringBuffer = new StringBuffer();  // 便于字符串的操作
		stringBuffer.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(columnInfo.getName() + "() {\n"));
		stringBuffer.append("\t\treturn " + columnInfo.getName() + ";\n");
		stringBuffer.append("\t}\n");
		javaFieldGetSet.setGetInfo(stringBuffer.toString());
		
		// 生成set方法源码
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
	 * 根据表信息生成java类的源代码(表-->数据库中的table)
	 * @param tableInfo  表信息
	 * @param convertor  数据类型转换器
	 * @return  java类的源代码
	 */ 
	public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor) {
		Map<String, ColumnInfo> columns = tableInfo.getColumns();  // 取出表信息里的字段信息
		List <JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();  // 属性列表
		
		// 一个表信息含有多个字段信息(属性信息、set、get方法)
		for(ColumnInfo c : columns.values()) {
			javaFields.add(createFieldGetSetSRC(c, convertor));  // 取出表信息里所有字段信息的属性信息和get、set方法并添加到javaFields中
		}
		
		StringBuilder src = new StringBuilder();
		
		// 生成package语句
		src.append("package " + DBManager.getConfiguration().getPoPackage() + ";" + "\n\n");
		// 生成import语句
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		// 生成类声明语句
		src.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + " {\n\n");
		// 生成属性列表(一个字段一个字段的取)
		for(JavaFieldGetSet javaFieldGetSet : javaFields) {
			src.append(javaFieldGetSet.getFieldInfo());
		}
		src.append("\n\n");
		// 生成get方法列表
		for(JavaFieldGetSet javaFieldGetSet : javaFields) {
			src.append(javaFieldGetSet.getGetInfo());
		}
		// 生成set方法列表
		for(JavaFieldGetSet javaFieldGetSet : javaFields) {
			src.append(javaFieldGetSet.getSetInfo());
		}
		// 生成类结束符 '}'
		src.append("}\n");
		
//		System.out.println(src);
		return src.toString();
	}
	
	/**
	 * 将源码写入指定路径
	 * @param tableInfo  表信息
	 * @param typeConvertor  类型转换对象
	 */
	public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor typeConvertor) {
		String string = createJavaSrc(tableInfo, typeConvertor);  // 获取Java类的源码
		
		// 获取存放源码的路径
		String srcPath = DBManager.getConfiguration().getSrcPath() + "\\";  
		String packagePath = DBManager.getConfiguration().getPoPackage().replaceAll("\\.", "\\\\");
		String totalString = srcPath + packagePath + "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname() + ".java");
				
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(totalString));  // 创建输出缓冲流的对象(如果没有指定的路径，则自动创建)
			bw.write(string);  // 将源码写入
			System.out.println("简历表" + tableInfo.getTname() + "对应的java类：" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java");
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
		// 测试用(生成属性、get、set) 
//		ColumnInfo columnInfo = new ColumnInfo("chy", "int", 0);
//		JavaFieldGetSet javaFieldGetSet = createFieldGetSetSRC(columnInfo, new MySqlTypeConvertor());
//		System.out.println(javaFieldGetSet.toString());
		
		// 生成chy1中qqq表的源码
//		Map<String, TableInfo> map = TableContext.tables;  // map为与tables一样的容器(此时map中已有数据库中所有表的信息)
//		TableInfo tableInfo = map.get("qqq");
//		createJavaSrc(tableInfo, new MySqlTypeConvertor());
		
		// 测试源码路径
		Map<String, TableInfo> map = TableContext.tables;  // map为与tables一样的容器   
		for(TableInfo tableInfo : map.values()) {
			createJavaPOFile(tableInfo, new MySqlTypeConvertor());
		}
	}
}


















