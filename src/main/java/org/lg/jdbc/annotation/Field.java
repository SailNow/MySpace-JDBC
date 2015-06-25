package org.lg.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
* 
* Field.java Create on 2015年6月24日 下午5:33:36        
* @author <a href="sailnow@qq.com">lige</a>
*/
@Target(ElementType.FIELD)
public @interface Field {
	
	/** 类型字段，先用常用的，以后用到再进行添加 */
	public enum FieldType{STRING,INTEGER,DOUBLE,FLOAT,DATA};
	
	/** 字段名称 */
	public String fieldName();
	/** 字段长度，不填写则不做验证 */
	public int length() default -1;
	/** 字段类型 */
	public FieldType type();
}
