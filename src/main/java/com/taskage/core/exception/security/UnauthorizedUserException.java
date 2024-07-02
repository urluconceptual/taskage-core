package com.taskage.core.exception.security;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("Unauthorized.");
    }
}
