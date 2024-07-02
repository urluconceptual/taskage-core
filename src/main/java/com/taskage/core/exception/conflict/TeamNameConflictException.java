package com.taskage.core.exception.conflict;

public class TeamNameConflictException extends ConflictException {
    public TeamNameConflictException() {
        super("Team name already exists!");
    }
}
