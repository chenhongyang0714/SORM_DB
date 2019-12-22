package com.sorm.core;

/**
 * 创建Query对象的工厂类
 * @author chenhongyang
 *
 */
public class QueryFactory {

	private QueryFactory queryFactory = new QueryFactory();
	private static Query prototypeObj;  // 原型对象（克隆模式）
	
	static {
		try {
			Class class1 = Class.forName(DBManager.getConfiguration().getQueryClass());  // 加载指定的query类
			prototypeObj = (Query) class1.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	private QueryFactory() {  // 私有化构造器
		
	}
	
	public static Query createQuery() {  // 用于返回Query对象
		try {
			return (Query) prototypeObj.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
		
	
}
