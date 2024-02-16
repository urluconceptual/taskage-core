package com.taskage.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Username is not registered.");
    }
}
