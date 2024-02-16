package com.taskage.core.exception;

public class UsernameConflictException extends RuntimeException{
    public UsernameConflictException() {
        super("Username already exists!");
    }
}
