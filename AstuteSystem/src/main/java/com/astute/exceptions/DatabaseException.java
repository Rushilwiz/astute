package com.astute.exceptions;

public class DatabaseException extends AstuteException {
    public DatabaseException(int code, String msg) {
        super(code, msg);
    }
}
