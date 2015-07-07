package org.lg.jdbc.exception; 
/** 
 * 
* 封装对象字段异常
* FieldAnomalyException.java Create on 2015年7月7日 下午2:36:32        
* @author <a href="sailnow@qq.com">lige</a>
 */
public class FieldAnomalyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 861913187412204856L;
	
	/**
	 * 构造方法
	 * @param msg
	 */
	public FieldAnomalyException(String msg){
		super(msg);
	}
	
	/**
	 * 构造方法
	 * @param msg
	 * @param e
	 */
	public FieldAnomalyException(String msg, Throwable e){
		super(msg, e);
	}
}
 