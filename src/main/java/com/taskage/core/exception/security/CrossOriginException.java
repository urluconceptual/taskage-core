package com.taskage.core.exception.security;

public class CrossOriginException extends RuntimeException {
    public CrossOriginException() {
        super("Invalid origin for request.");
    }
}
