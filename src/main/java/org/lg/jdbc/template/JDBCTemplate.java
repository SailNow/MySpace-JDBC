package org.lg.jdbc.template;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.lg.jdbc.annotation.ID;
import org.lg.jdbc.annotation.Table;
import org.lg.jdbc.exception.FieldAnomalyException;
import org.lg.jdbc.exception.JDBCTemplateException;
import org.lg.jdbc.exception.NoAnnotationException;

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
	
	public void update(Object po) throws NoAnnotationException, JDBCTemplateException, FieldAnomalyException{
		Class pClass = po.getClass();
		Table table = (Table)pClass.getAnnotation(Table.class);
		if(table == null){
			throw new NoAnnotationException("传入po没有标记table注解");
		}
		//表名
		String tableName = table.value();
		Field[] fields = pClass.getDeclaredFields();
		//取得字段映射关系map
		Map<String, org.lg.jdbc.annotation.Field> mappingMap = this.getMappingMap(fields);
		//取得id属性名称及注解
		ID id = null;
		String idFieldName = null;
		for(Field field : fields){
			ID tableId = field.getAnnotation(ID.class);
			if(tableId != null){
				id = tableId;
				idFieldName = field.getName();
				break;
			}
		}
		if(id == null){
			throw new JDBCTemplateException("传入对象没有主键，无法执行更新操作");
		}
		//组装sql
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE " + tableName + " SET ");
		for(Iterator iter = mappingMap.entrySet().iterator();iter.hasNext();){
			Map.Entry entry = (Map.Entry) iter.next();
			//跳过id字段
			if(idFieldName.equals(entry.getKey())){
				continue;
			}
			sql.append(((org.lg.jdbc.annotation.Field)entry.getValue()).fieldName() + "=? ");
			if(iter.hasNext()){
				sql.append(",");
			}
		}
		sql.append(" where id=? ");
		//开始执行更新操作
		Connection conn = this.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			int index = 1;
			for(Iterator iter = mappingMap.entrySet().iterator();iter.hasNext();){
				Map.Entry entry = (Map.Entry) iter.next();
				//跳过id字段
				if(idFieldName.equals(entry.getKey())){
					continue;
				}
				try {
					 switch(((org.lg.jdbc.annotation.Field)entry.getValue()).type()){
					 case STRING:
						 pstmt.setString(index++, (String)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case INTEGER:
						 pstmt.setInt(index++, (Integer)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case DOUBLE:
						 pstmt.setDouble(index++, (Double)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case FLOAT:
						 pstmt.setFloat(index++, (Float)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case DATA:
						 Date date = (Date)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po);
						 pstmt.setTimestamp(index++, new Timestamp(date.getTime()));
						 break;
					 }
				 } catch (Exception e) {
					 throw new FieldAnomalyException("取得字段时出现异常，异常字段：" + entry.getKey(), e);
				 }
			}
			try {
				switch(id.strategy()){
				case UUID:
					pstmt.setString(index++, (String)pClass.getMethod("get" + idFieldName.substring(0, 1).toUpperCase() + idFieldName.substring(1)).invoke(po));
				case AUTOINCREAMENT:
					pstmt.setInt(index++, Integer.parseInt((String)pClass.getMethod("get" + idFieldName.substring(0, 1).toUpperCase() + idFieldName.substring(1)).invoke(po)));
				}
			} catch (Exception e) {
				 throw new FieldAnomalyException("取得主键时出现异常，异常字段：" + idFieldName, e);
			}
			pstmt.executeUpdate();
			pstmt.close();
			this.closeConn(conn);
		} catch (SQLException e) {
			throw new JDBCTemplateException("更新对象出错", e);
		}
	}
	
	/**
	 * 保存
	 * @param po
	 * @throws NoAnnotationException 
	 * @throws FieldAnomalyException 
	 * @throws JDBCTemplateException 
	 */
	public void save(Object po) throws NoAnnotationException, FieldAnomalyException, JDBCTemplateException{
		Class pClass = po.getClass();
		Table table = (Table)pClass.getAnnotation(Table.class);
		if(table == null){
			throw new NoAnnotationException("传入po没有标记table注解");
		}
		//表名
		String tableName = table.value();
		Field[] fields = pClass.getDeclaredFields();
		//取得字段映射关系map
		Map<String, org.lg.jdbc.annotation.Field> mappingMap = this.getMappingMap(fields);
		//取得id属性名称及注解
		ID id = null;
		String idFieldName = null;
		for(Field field : fields){
			ID tableId = field.getAnnotation(ID.class);
			if(tableId != null){
				id = tableId;
				idFieldName = field.getName();
				break;
			}
		}
		//组装字段和参数
		StringBuilder paramString = new StringBuilder();
		StringBuilder fieldString = new StringBuilder();
		for(Iterator iter = mappingMap.entrySet().iterator();iter.hasNext();){
			 Map.Entry entry = (Map.Entry) iter.next();
			 paramString.append("?");
			 fieldString.append(((org.lg.jdbc.annotation.Field)entry.getValue()).fieldName());
			 if(iter.hasNext()){
				 paramString.append(",");
				 fieldString.append(",");
			 }
		}
		//开始组装sql语句
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ");
		sql.append(tableName);
		sql.append(" (" + fieldString + ") ");
		sql.append(" values( " + paramString + ") ");
		//开始执行插入操作
		Connection conn = this.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			int index = 1;
			for(Iterator iter = mappingMap.entrySet().iterator();iter.hasNext();){
				 Map.Entry entry = (Map.Entry) iter.next();
				 //判断是否是id，如果是id判断是否需要自动生成id
				 if(id != null && idFieldName.equals(entry.getKey()) && id.auto()){
					 //是id字段，并且需要特殊处理
					 switch(id.strategy()){
					 case UUID:
						 String idValue = UUID.randomUUID().toString().replaceAll("-", "");
						 pstmt.setString(index++, idValue);
						 break;
					 case AUTOINCREAMENT:
						 //取得当前最大的id
						 try{
							 String maxId = this.getMaxIdField(tableName, idFieldName, conn);
							 pstmt.setInt(index++, maxId == null ? 1 : Integer.parseInt(maxId) + 1);
						 }catch(Exception e){
							 throw new JDBCTemplateException("生成自增长id时出错", e);
						 }
						 break;
					 }
					 continue;
				 }
				 try {
					 switch(((org.lg.jdbc.annotation.Field)entry.getValue()).type()){
					 case STRING:
						 pstmt.setString(index++, (String)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case INTEGER:
						 pstmt.setInt(index++, (Integer)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case DOUBLE:
						 pstmt.setDouble(index++, (Double)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case FLOAT:
						 pstmt.setFloat(index++, (Float)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po));
						 break;
					 case DATA:
						 Date date = (Date)pClass.getMethod("get" + ((String)entry.getKey()).substring(0, 1).toUpperCase() + ((String)entry.getKey()).substring(1)).invoke(po);
						 pstmt.setTimestamp(index++, new Timestamp(date.getTime()));
						 break;
					 }
				 } catch (Exception e) {
					 throw new FieldAnomalyException("取得字段时出现异常，异常字段：" + entry.getKey(), e);
				 }
			}
			pstmt.executeUpdate();
			pstmt.close();
			this.closeConn(conn);
		} catch (SQLException e) {
			throw new JDBCTemplateException("存储对象出错", e);
		}
	}
	
	/**
	 * 获取属性和字段映射关系map,key是对象中属性名称，value是Field注解，里面有表中字段信息
	 * @param fields
	 * @return
	 */
	private Map<String, org.lg.jdbc.annotation.Field> getMappingMap(Field[] fields){
		Map<String, org.lg.jdbc.annotation.Field> mappingMap = new LinkedHashMap<String, org.lg.jdbc.annotation.Field>();
		for(Field field : fields){
			org.lg.jdbc.annotation.Field tableField = field.getAnnotation(org.lg.jdbc.annotation.Field.class);
			if(tableField == null){
				continue;
			}
			mappingMap.put(field.getName(), tableField);
		}
		return mappingMap;
	}
	
	private String getMaxIdField(String tableName, String fieldName, Connection conn) throws SQLException{
		String sql = " SELECT top 1 " + fieldName + " FROM " + tableName + " ORDER BY " + fieldName + " DESC ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			return rs.getString(1);
		}
		return null;
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
