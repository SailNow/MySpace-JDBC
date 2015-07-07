package org.lg.jdbc.annotation; 

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lg.jdbc.em.AutoStrategy;

/** 
 * @author 作者姓名  E-mail: email地址 
 * @version 创建时间：2015年6月30日 下午2:26:46 
 * 类说明 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ID {
	/**
	 * 是否自动，目前是
	 * @return
	 */
	public boolean auto() default false;
	
	/**
	 * 自增策略
	 * @return
	 */
	public AutoStrategy strategy() default AutoStrategy.UUID;
}
 