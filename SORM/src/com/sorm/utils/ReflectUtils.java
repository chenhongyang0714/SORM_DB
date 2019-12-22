package com.sorm.utils;

import java.lang.reflect.Method;

/**
 * ��װ���䳣�õĲ���
 * 
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class ReflectUtils {

	/**
	 * ����obj�����Ӧ���Ե�get����(��ȡobj���������)
	 * 
	 * @param obj     ����
	 * @param keyName �������ڱ��е� ���Ե�����
	 * @return
	 */
	// ע�⣺��ͨ�������ʱ�����ص��Ǹö����ڱ��е�ָ�����Ե� ��ֵ
	public static Object invokeGet(Object obj, String keyName) {
		try {
			Class c = obj.getClass(); // ���ش� Object ������ʱ��

			// ��obj�����е�������һ���������÷������Ի�ȡһ��������ֶ����� getId()
			Method m = c.getDeclaredMethod("get" + StringUtils.firstChar2UpperCase(keyName), null);
			return m.invoke(obj, null); // ��ȡobj���������ֵ --> obj.getId()

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����rowObj�����columeName������ΪcolumeValue
	 * 
	 * @param rowObj      clazz�Ķ���
	 * @param columeName  ����
	 * @param columeValue ��������Ӧ��ֵ
	 */
	public static void invokeSet(Object rowObj, String columeName, Object columeValue) {
		// ����rowObj�����setUsername(String username)��������columnValue��ֵ���ý�ȥ
//		
//		  Method method = clazz.getDeclaredMethod("set " +
//		  StringUtils.firstChar2UpperCase(columeName), columeValue.getClass()); //
//		  ���columeValue��String����columeValue.getClass()����String����(��������Ҳһ��)
//		  method.invoke(rowObj, columeValue);
		 

		// ����rowObj�����setUsername(String username)��������columnValue��ֵ���ý�ȥ
		try {
			if(columeValue != null) {
				Method method = rowObj.getClass().getDeclaredMethod("set" + StringUtils.firstChar2UpperCase(columeName), columeValue.getClass());
				method.invoke(rowObj, columeValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
