package com.sr.electronic.store.Electronic_Store.exceptions;

public class ResourceNOtFoundException extends RuntimeException{
    public ResourceNOtFoundException(){
        super("Resource Not Found !!");
    }
    public ResourceNOtFoundException(String message){
        super(message);
    }
}
