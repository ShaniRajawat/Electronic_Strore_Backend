package com.sr.electronic.store.Electronic_Store.exceptions;

public class MaxUploadSizeExceededException extends RuntimeException {
    public MaxUploadSizeExceededException(String message) {
        super(message);
    }

    public MaxUploadSizeExceededException() {
        super("Max Upload Size is Exceeded !!");
    }
}