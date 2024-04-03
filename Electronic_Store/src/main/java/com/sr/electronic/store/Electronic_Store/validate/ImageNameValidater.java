package com.sr.electronic.store.Electronic_Store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageNameValidater implements ConstraintValidator<ImageNameValid,String> {

    private Logger logger = LoggerFactory.getLogger(ImageNameValidater.class);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        logger.info("Message From isValid: {}",value);
        //Logic
        if(value.isBlank()){
            return false;
        }else {
            return true;
        }
    }
}
