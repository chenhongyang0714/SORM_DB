package com.sorm.bean;

/** 
  * 封装表中  一个字段的信息
  * 字段: 字段是比记录更小的单位，字段集合组成记录，每个字段描述文献的某一特征，即数据项
 * @author chenhongyang
 *
 */
public class ColumnInfo {

	 // 字段名称(如果是主键，则是主键名称；如果是普通对象,则是主键值)
	private String name;
	
	// 字段的数据类型
	private String dataType;
	
	// 字段的键类型(0:普通键， 1:主键,   2:外键)
	private int keyType;

	
	public ColumnInfo() {
	}
	public ColumnInfo(String name, String dataType, int keyType) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.keyType = keyType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getKeyType() {
		return keyType;
	}

	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}
}
