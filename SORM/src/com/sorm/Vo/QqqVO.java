package com.sorm.Vo;

/**
 * 遇到复杂查询时可以单独定义一个po类进行存储相应的属性
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
