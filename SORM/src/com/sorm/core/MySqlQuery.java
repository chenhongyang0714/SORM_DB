package com.sorm.core;

/**
  * ���� ���MySql���ݿ�Ĳ���
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class MySqlQuery extends Query{

	// ���Ժ���
	public static void testDML() {
		// ����ɾ��
//		Qqq qqq = new Qqq();
//		qqq.setId(8);
//		new MySqlQuery().delete(qqq);
		
		// ���Բ���
//		Qqq qqq = new Qqq();
////		qqq.setId(14);  // �����������ݿ��е�id�ظ�(������Ϊ����ʱ���Բ�ָ��)
//		qqq.setName("AS");
//		new MySqlQuery().insert(qqq);
		
		// �����޸�
//		Qqq qqq = new Qqq();
//		qqq.setName("AS");
//		qqq.setId(3);
//		new MySqlQuery().update(qqq, new String[] {"name"});
		
	    // ���ж��в�ѯ(�������) 	
//		List<Qqq> list = new MySqlQuery().queryRows("select id, name from qqq where id>?", Qqq.class, new Object[] {15});
//		for(Qqq qqq : list) {
//			System.out.println(qqq.getName() + qqq.getId());
//		}
		
		// ���ж��в�ѯ(���Ӳ���)
//		String sqlString = "select id,name,age from qqq;";
//		List<QqqVO> list = new MySqlQuery().queryRows(sqlString, QqqVO.class, null);
//
//		for(QqqVO qqqVO : list) {
//			System.out.println(qqqVO.getId() + "-" + qqqVO.getName() + "-" + qqqVO.getAge());
//		}
		
		// ����һ�ж��в�ѯ���Լ�д�ģ� 
//		String sqlString = "select id,name,age from qqq where id=15;";
//		List<QqqVO> list = new MySqlQuery().queryUniqyeRowMyTeacher(sqlString, QqqVO.class, null);
//
//		for(QqqVO qqqVO : list) {
//			System.out.println(qqqVO.getId() + "-" + qqqVO.getName() + "-" + qqqVO.getAge());
//		}
		
		// ����һ�ж��У���ʦд�ģ�
//		String sqlString = "select id,name,age from qqq where id=15;";
//		List<QqqVO> list = new MySqlQuery().queryUniqyeRowMyTeacher(sqlString, QqqVO.class, null);
//
//		for(QqqVO qqqVO : list) {
//			System.out.println(qqqVO.getId() + "-" + qqqVO.getName() + "-" + qqqVO.getAge());
//		}
		
		// ����һ��һ��
//		String sql = "select count(*) from qqq where id>?;";  // ���ط�������������
//		Object value = new MySqlQuery().queryValue(sql, new Object[] {14});
//		System.out.println(value);	
		
		// ��ѯĳ��ֵ
//		String sql = "select count(*) from qqq where id>?;";  // ���ط�������������
//		Number number = new MySqlQuery().queryNumber(sql, new Object[] {14});
//		System.out.println(number);
	}
	
	public static void main(String[] args) {
		MySqlQuery.testDML();		
	}
	  
	public Object queryPagenate(int pageNum, int size) {
		return null;
	}
	
}





















