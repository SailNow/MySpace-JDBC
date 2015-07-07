package org.lg.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lg.jdbc.em.FieldType;

/**
* 
* Field.java Create on 2015年6月24日 下午5:33:36        
* @author <a href="sailnow@qq.com">lige</a>
*/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	
	/** 字段名称 */
	public String fieldName();
	/** 字段长度，不填写则不做验证 */
	public int length() default -1;
	/** 字段类型 */
	public FieldType type();
}
