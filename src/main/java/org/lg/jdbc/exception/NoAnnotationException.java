package org.lg.jdbc.exception; 
/** 
 * 没有标记注解异常
 * @author 作者姓名  E-mail: email地址 
 * @version 创建时间：2015年6月30日 下午2:02:41 
 * 类说明 
 */
public class NoAnnotationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 861913187412204856L;
	
	/**
	 * 构造方法
	 * @param msg
	 */
	public NoAnnotationException(String msg){
		super(msg);
	}
	
	/**
	 * 构造方法
	 * @param msg
	 * @param e
	 */
	public NoAnnotationException(String msg, Throwable e){
		super(msg, e);
	}
}
 