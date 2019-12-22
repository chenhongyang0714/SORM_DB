package com.sorm.bean;

/**
 * 封装了 字段的  属性、 get、set方法 的源代码
 * @author chenhongyang
 *
 */
public class JavaFieldGetSet {
	
	// 属性的源码信息  如:private int userId;
	private String fieldInfo;
	
	// get方法的源码信息  如: public int getuserId() {}
	private String getInfo;
	
	// set方法的源码信息  如: public int setUserId(int id) {this.id = id}
	private String setInfo;

	
	// 重载Object中的toString
	@Override
	public String toString() {
		System.out.println(fieldInfo);
		System.out.println(getInfo);
		System.out.println(setInfo);
		return super.toString();
	}
	
	public JavaFieldGetSet() {
	}
	public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
		super();
		this.fieldInfo = fieldInfo;
		this.getInfo = getInfo;
		this.setInfo = setInfo;
	}

	public String getFieldInfo() {
		return fieldInfo;
	}

	public void setFieldInfo(String fieldInfo) {
		this.fieldInfo = fieldInfo;
	}

	public String getGetInfo() {
		return getInfo;
	}

	public void setGetInfo(String getInfo) {
		this.getInfo = getInfo;
	}

	public String getSetInfo() {
		return setInfo;
	}

	public void setSetInfo(String setInfo) {
		this.setInfo = setInfo;
	}
	
}
