package com.sorm.test;

import java.util.List;

import com.sorm.core.MySqlQuery;
import com.sorm.core.Query;
import com.sorm.core.QueryFactory;
import com.study.po.Qqq;

/**
 * �ͻ��˵��õĲ�����
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class Test { 

	public static void main(String[] args) {
		Query query = QueryFactory.createQuery();  //  ���MysqlQuery����
		
		// ���ж��в���
		List<Qqq> list = query.queryRows("select id, name from qqq where id>?", Qqq.class, new Object[] {10});
		for(Qqq qqq : list) {
			System.out.println(qqq.getName() + qqq.getId());
		}
	}
}
