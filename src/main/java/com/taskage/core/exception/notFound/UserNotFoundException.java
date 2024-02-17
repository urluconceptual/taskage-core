package com.taskage.core.exception.notFound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("Username is not registered.");
    }
}
