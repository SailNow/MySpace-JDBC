package org.lg.jdbc;

import java.io.Serializable;
import java.util.Date;

import org.lg.jdbc.annotation.Field;
import org.lg.jdbc.annotation.ID;
import org.lg.jdbc.annotation.Table;
import org.lg.jdbc.em.AutoStrategy;
import org.lg.jdbc.em.FieldType;

/**
 * 测试po
 * @author Administrator
 *
 */
@Table("t_user")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1080247693014019704L;
	
	@ID(auto=true,strategy=AutoStrategy.AUTOINCREAMENT)
	@Field(fieldName="id",type=FieldType.STRING)
	private String id;
	@Field(fieldName="username",type=FieldType.STRING)
	private String name;
	@Field(fieldName="password",type=FieldType.STRING)
	private String pwd;
	@Field(fieldName="create_time", type=FieldType.DATA)
	private Date createTime;
	@Field(fieldName="remark", type=FieldType.STRING)
	private String desc;
	@Field(fieldName="avg", type=FieldType.DOUBLE)
	private double avg;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
}
