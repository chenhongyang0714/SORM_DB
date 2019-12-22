package com.sorm.core;

/**
 * ����Query����Ĺ�����
 * @author chenhongyang
 *
 */
public class QueryFactory {

	private QueryFactory queryFactory = new QueryFactory();
	private static Query prototypeObj;  // ԭ�Ͷ��󣨿�¡ģʽ��
	
	static {
		try {
			Class class1 = Class.forName(DBManager.getConfiguration().getQueryClass());  // ����ָ����query��
			prototypeObj = (Query) class1.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	private QueryFactory() {  // ˽�л�������
		
	}
	
	public static Query createQuery() {  // ���ڷ���Query����
		try {
			return (Query) prototypeObj.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
		
	
}
