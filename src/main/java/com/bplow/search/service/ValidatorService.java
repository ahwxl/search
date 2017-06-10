/**
 * www.bplow.com
 */
package com.bplow.search.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @desc 
 * @author wangxiaolei
 * @date 2017年6月10日 下午10:23:43
 */
@Service
public class ValidatorService implements InitializingBean {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Validator validator ;
	
	public boolean validator(Object object){
		
		boolean result = true;
		
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
		
		if(constraintViolations.size() > 0){
			
			result = false;
			
			logger.error("{}",constraintViolations.iterator().next().getMessage());
		}
		
		return result;
	}

	public void afterPropertiesSet() throws Exception {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();                
		validator = factory.getValidator();
		
	}

}
