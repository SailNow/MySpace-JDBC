package org.lg.jdbc;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
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
	public void testSaveUser(){
		template.saveUser();
	}
	
	@Test
	public void testSave(){
		User u = new User();
		u.setId("1");
		u.setName("张三");
		u.setPwd("lg3625922");
		u.setCreateTime(new Date());
		template.save(u);
	}
}
