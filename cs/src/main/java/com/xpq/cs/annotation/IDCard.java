package com.xpq.cs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import com.xpq.cs.annotation.validator.IDCardValidator;

@Constraint(validatedBy = IDCardValidator.class) // 具体的实现
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface IDCard {
	
	String value() default "";
	
	/**
	 * 是否为空，即当空值是不判断
	 * @return
	 */
	boolean nullable() default false;
	
	/**
	 * 校验后的错误信息
	 * @return
	 */
	String message() default "身份证格式错误";
	
	/**
	 * 为空时返回的错误信息
	 * @return
	 */
	String nullMessage() default "";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};
	
}
