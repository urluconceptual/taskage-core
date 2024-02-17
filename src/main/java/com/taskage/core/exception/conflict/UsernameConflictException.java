package com.taskage.core.exception.conflict;

public class UsernameConflictException extends ConflictException {
    public UsernameConflictException() {
        super("Username already exists!");
    }
}
