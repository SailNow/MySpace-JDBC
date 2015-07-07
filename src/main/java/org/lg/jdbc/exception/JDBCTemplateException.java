package org.lg.jdbc.exception; 
/** 
 * 
* jdbc相关异常类
* FieldAnomalyException.java Create on 2015年7月7日 下午2:36:32        
* @author <a href="sailnow@qq.com">lige</a>
 */
public class JDBCTemplateException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 861913187412204856L;
	
	/**
	 * 构造方法
	 * @param msg
	 */
	public JDBCTemplateException(String msg){
		super(msg);
	}
	
	/**
	 * 构造方法
	 * @param msg
	 * @param e
	 */
	public JDBCTemplateException(String msg, Throwable e){
		super(msg, e);
	}
}
 