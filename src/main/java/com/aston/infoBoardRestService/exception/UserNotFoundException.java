package com.aston.infoBoardRestService.exception;

public class UserNotFoundException extends RuntimeException{
    public  UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
