package com.sorm.test;

import java.util.List;

import com.sorm.core.Query;
import com.sorm.core.QueryFactory;
import com.study.po.Qqq;

/**
 *  �������ӳص�Ч��
 * @author chenhongyang
 * 
 */
@SuppressWarnings("all")
public class Test2 { 

	public static void test() {
		Query query = QueryFactory.createQuery();  //  ���MysqlQuery����
		// ���ж��в���
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
		System.out.println(b-a);  // �������ӳصĺ�ʱ  13236; ʹ�����ӳؼ�����ʱ  2069
	}
}
