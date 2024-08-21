package com.learn.authserviceforproject.exceptions;

public class DuplicatePlaceNameException extends RuntimeException {
    public DuplicatePlaceNameException(String message) {
        super(message);
    }
}
