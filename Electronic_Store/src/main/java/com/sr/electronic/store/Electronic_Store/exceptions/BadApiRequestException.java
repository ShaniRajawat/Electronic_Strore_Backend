package com.sr.electronic.store.Electronic_Store.exceptions;

public class BadApiRequestException extends RuntimeException{
    public BadApiRequestException(String message){
        super(message);
    }
    public BadApiRequestException(){
        super("Bad Request !!");
    }
}
