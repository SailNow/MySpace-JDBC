package org.lg.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/** table代表是映射到指定的表中
	 *  subselect代表是封装子查询结果的对象
	 */   
	public enum TableType{TABLE,SUBSELECT};
	
	/** 表明 */
	public String value();
	/** 类型 */
	public TableType type() default TableType.TABLE;
}
