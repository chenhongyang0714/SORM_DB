package com.sorm.Vo;

/**
 * �������Ӳ�ѯʱ���Ե�������һ��po����д洢��Ӧ������
 * @author chenhongyang
 *
 */
public class QqqVO {

	// select e.id, salary+bonus 'xinshui',age,d.name 'deptName',d.address 'deptAddr' from qqq q join  dept d on e.deptId=d.id;
	private Integer id;
	private String name;
	private Integer age;
	
	public QqqVO() {
	}

	public QqqVO(Integer id, String nameString, Integer age) {
		super();
		this.id = id;
		this.name = nameString;
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
