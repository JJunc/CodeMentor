package com.codementor.exception;

public class LoginFailedException extends RuntimeException {


    public LoginFailedException(String message) {
        super(message);
    }
}
