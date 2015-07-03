package org.lg.jdbc.template;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.lg.jdbc.annotation.Table;

import com.alibaba.druid.pool.DruidDataSource;

public class JDBCTemplate {
	/** 数据源 */
	private static DruidDataSource dataSource;
	
	/**
	 * 取得数据库连接
	 * @return
	 */
	private Connection getConn(){
		//FIXME 使用的druid连接池  目前还啥都不会  文档打不开  简单的根据api取得数据库连接  有待修改
		try {
			System.out.println("连接池连接数量:" + dataSource.getPoolingCount());
			System.out.println("连接池激活数量:" + dataSource.getActiveCount());
			Connection conn = dataSource.getConnection();
			System.out.println("连接池激活数量:" + dataSource.getActiveCount());
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 保存
	 * @param po
	 */
	public void save(Object po){
		Class pClass = po.getClass();
		Annotation table = pClass.getAnnotation(Table.class);
	}
	
	public void saveUser(){
		String sql = " INSERT INTO t_user(id,username,password,create_time) values(?,?,?,?) ";
		Connection conn = this.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "654321");
			pstmt.setString(2, "lisi");
			pstmt.setString(3, "321321");
			pstmt.setDate(4, new java.sql.Date(new Date().getTime()));
			System.out.println(pstmt.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectUser(){
		String sql = " SELECT id,username from t_user ";
		Connection conn = this.getConn();
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			int columnCount = rs.getMetaData().getColumnCount();
			System.out.println("column count : " + columnCount);
			while(rs.next()){
				//System.out.println(rs.getObject(0, String.class));
				System.out.println(rs.getString(1));
				System.out.println(rs.getObject(1, String.class));
				System.out.println(rs.getObject("name", String.class));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	private void closeConn(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setDateSource(DruidDataSource dateSource) {
		this.dataSource = dateSource;
	}
}
