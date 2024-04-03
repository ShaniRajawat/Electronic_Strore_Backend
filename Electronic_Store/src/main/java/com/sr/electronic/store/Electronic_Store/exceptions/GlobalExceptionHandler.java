package com.sr.electronic.store.Electronic_Store.exceptions;

import com.sr.electronic.store.Electronic_Store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handler resource not found exception
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNOtFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundHandler(ResourceNOtFoundException ex){

        logger.info("Exception Handler Invoked !!");
        ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //PropertyReferenceException
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiResponseMessage> propertyReferenceException(PropertyReferenceException ex){
        ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //BadApiRequestException
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> badApiResponse(BadApiRequestException ex){

        logger.info("Exception Handler Invoked !!");
        ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //Max size Exception
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseMessage> maxUploadSizeExceededException(BadApiRequestException ex){

        logger.info("Exception Handler Invoked !!");
        ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_ACCEPTABLE).success(false).build();
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
    }
}
