package com.sorm.utils;

import java.lang.reflect.Method;

/**
 * 封装反射常用的操作
 * 
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class ReflectUtils {

	/**
	 * 调用obj对象对应属性的get方法(获取obj对象的属性)
	 * 
	 * @param obj     对象
	 * @param keyName 对象所在表中的 属性的名称
	 * @return
	 */
	// 注意：普通对象调用时，返回的是该对象在表中的指定属性的 键值
	public static Object invokeGet(Object obj, String keyName) {
		try {
			Class c = obj.getClass(); // 返回此 Object 的运行时类

			// 在obj所运行的类中找一个方法：该方法可以获取一个对象的字段名称 getId()
			Method m = c.getDeclaredMethod("get" + StringUtils.firstChar2UpperCase(keyName), null);
			return m.invoke(obj, null); // 获取obj对象的主键值 --> obj.getId()

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置rowObj对象的columeName的属性为columeValue
	 * 
	 * @param rowObj      clazz的对象
	 * @param columeName  列名
	 * @param columeValue 列名所对应的值
	 */
	public static void invokeSet(Object rowObj, String columeName, Object columeValue) {
		// 调用rowObj对象的setUsername(String username)方法，将columnValue的值设置进去
//		
//		  Method method = clazz.getDeclaredMethod("set " +
//		  StringUtils.firstChar2UpperCase(columeName), columeValue.getClass()); //
//		  如果columeValue是String，则columeValue.getClass()返回String对象(其他类型也一样)
//		  method.invoke(rowObj, columeValue);
		 

		// 调用rowObj对象的setUsername(String username)方法，将columnValue的值设置进去
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
