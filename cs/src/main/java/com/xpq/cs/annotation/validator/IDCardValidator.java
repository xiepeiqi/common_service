package com.xpq.cs.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.xpq.cs.annotation.IDCard;
import com.xpq.cs.util.IDCardUtils;

/**
 * 身份证校验
 * @author xiepeiqi @date 2019年8月6日
 */
public class IDCardValidator implements ConstraintValidator<IDCard, String> {
	
	private String nullMessage;

	@Override
	public void initialize(IDCard idCard) {
		nullMessage = idCard.nullMessage();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return IDCardUtils.validateCard(value);
		} else if(StringUtils.isNotBlank(nullMessage)){
			// 禁用默认的message的值
			context.disableDefaultConstraintViolation();
			// 重新添加错误提示语句
			context.buildConstraintViolationWithTemplate(nullMessage).addConstraintViolation();
			return false;
		}
		return true;
	}

}
