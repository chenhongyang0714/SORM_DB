package com.sorm.core;

/**
  * 负责 针对MySql数据库的操作
 * @author chenhongyang
 *
 */
@SuppressWarnings("all")
public class MySqlQuery extends Query{

	// 测试函数
	public static void testDML() {
		// 测试删除
//		Qqq qqq = new Qqq();
//		qqq.setId(8);
//		new MySqlQuery().delete(qqq);
		
		// 测试插入
//		Qqq qqq = new Qqq();
////		qqq.setId(14);  // 不可以与数据库中的id重复(当主键为自增时可以不指定)
//		qqq.setName("AS");
//		new MySqlQuery().insert(qqq);
		
		// 测试修改
//		Qqq qqq = new Qqq();
//		qqq.setName("AS");
//		qqq.setId(3);
//		new MySqlQuery().update(qqq, new String[] {"name"});
		
	    // 多行多列查询(常规操作) 	
//		List<Qqq> list = new MySqlQuery().queryRows("select id, name from qqq where id>?", Qqq.class, new Object[] {15});
//		for(Qqq qqq : list) {
//			System.out.println(qqq.getName() + qqq.getId());
//		}
		
		// 多行多列查询(复杂操作)
//		String sqlString = "select id,name,age from qqq;";
//		List<QqqVO> list = new MySqlQuery().queryRows(sqlString, QqqVO.class, null);
//
//		for(QqqVO qqqVO : list) {
//			System.out.println(qqqVO.getId() + "-" + qqqVO.getName() + "-" + qqqVO.getAge());
//		}
		
		// 测试一行多列查询（自己写的） 
//		String sqlString = "select id,name,age from qqq where id=15;";
//		List<QqqVO> list = new MySqlQuery().queryUniqyeRowMyTeacher(sqlString, QqqVO.class, null);
//
//		for(QqqVO qqqVO : list) {
//			System.out.println(qqqVO.getId() + "-" + qqqVO.getName() + "-" + qqqVO.getAge());
//		}
		
		// 测试一行多列（老师写的）
//		String sqlString = "select id,name,age from qqq where id=15;";
//		List<QqqVO> list = new MySqlQuery().queryUniqyeRowMyTeacher(sqlString, QqqVO.class, null);
//
//		for(QqqVO qqqVO : list) {
//			System.out.println(qqqVO.getId() + "-" + qqqVO.getName() + "-" + qqqVO.getAge());
//		}
		
		// 测试一行一列
//		String sql = "select count(*) from qqq where id>?;";  // 返回符合条件的行数
//		Object value = new MySqlQuery().queryValue(sql, new Object[] {14});
//		System.out.println(value);	
		
		// 查询某个值
//		String sql = "select count(*) from qqq where id>?;";  // 返回符合条件的行数
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





















