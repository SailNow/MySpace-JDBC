package org.lg.jdbc;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.lg.jdbc.exception.FieldAnomalyException;
import org.lg.jdbc.exception.JDBCTemplateException;
import org.lg.jdbc.exception.NoAnnotationException;
import org.lg.jdbc.template.JDBCTemplate;

import com.alibaba.druid.pool.DruidDataSource;

public class JDBCTemplateTest extends TestCase {
	
	private JDBCTemplate template;
	
	@Before
	public void setUp(){
		template = new JDBCTemplate();
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		dataSource.setUrl("jdbc:jtds:sqlserver://127.0.0.1:1433/MySpace");
		dataSource.setUsername("sa");
		dataSource.setPassword("root");
		dataSource.setMaxActive(20);
		dataSource.setInitialSize(1);
		dataSource.setMaxWait(60000);
		dataSource.setConnectionProperties("clientEncoding=UTF-8");
		template.setDateSource(dataSource);
	}
	
	@Test
	public void testSave(){
		User u = new User();
		u.setId("1");
		u.setName("张三");
		u.setPwd("lg3625922");
		u.setCreateTime(new Date());
		u.setDesc("我叫张三");
		u.setAvg(2.2294);
		try {
			template.save(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testUpdate(){
		User u = new User();
		u.setId("1");
		u.setName("李四1");
		u.setPwd("1231231");
		u.setCreateTime(new Date());
		u.setDesc("my name is LiSi1");
		u.setAvg(1.4523);
		try {
			template.update(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
