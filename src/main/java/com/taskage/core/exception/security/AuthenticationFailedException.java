package com.taskage.core.exception.security;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super("Authentication failed.");
    }
}
