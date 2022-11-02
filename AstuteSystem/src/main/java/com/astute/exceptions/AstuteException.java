package com.astute.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"stackTrace", "cause", "localizedMessage", "suppressed"})
public class AstuteException extends Exception {
    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 200;
    public static final int CLIENT_ERROR = 400;
    public static final int SERVER_ERROR = 500;
    public static final int DB_ERROR = 600;
    public static final int AUTH_ERROR = 700;

    private int code;

    public AstuteException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getErrorCode() {
        return code;
    }
}
