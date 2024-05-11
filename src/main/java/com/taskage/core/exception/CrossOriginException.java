package com.taskage.core.exception;

public class CrossOriginException extends RuntimeException {
    public CrossOriginException() {
        super("Invalid origin for request.");
    }
}
