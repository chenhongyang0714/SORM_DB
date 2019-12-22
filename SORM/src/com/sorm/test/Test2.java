package com.sorm.test;

import java.util.List;

import com.sorm.core.Query;
import com.sorm.core.QueryFactory;
import com.study.po.Qqq;

/**
 *  测试连接池的效率
 * @author chenhongyang
 * 
 */
@SuppressWarnings("all")
public class Test2 { 

	public static void test() {
		Query query = QueryFactory.createQuery();  //  获得MysqlQuery对象
		// 多行多列测试
		List<Qqq> list = query.queryRows("select id, name from qqq where id>?", Qqq.class, new Object[] {10});
		for(Qqq qqq : list) {
			System.out.println(qqq.getName() + qqq.getId());
		} 
	}
	
	public static void main(String[] args) {
		long a = System.currentTimeMillis();
		for(int i=0;i<10000;i++) {
			Test2.test();
		}
		long b = System.currentTimeMillis();
		System.out.println(b-a);  // 不加连接池的耗时  13236; 使用连接池技术耗时  2069
	}
}
