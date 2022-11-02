package com.astute.response;

import javax.ws.rs.core.Response;

import static com.astute.common.Constants.SUCCESS;

public class ApiResponse {
    private Integer code                                     = null;
    private String  message                                  = null;
    private Object  entity                                   = null;

    public static final String GRANT_ACCESS_SUCCESS         = "Access successfully granted";
    public static final String UPDATE_ACCESS_SUCESS         = "Successfully updated access";
    public static final String ACCESS_DENIED                = "Logon failed";


    private static final String SUCCESS_MSG = "Success";

    public ApiResponse() {
    }

    public ApiResponse(Object entity) {
        this.code = SUCCESS;
        this.message = SUCCESS_MSG;
        this.entity = entity;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Exception exception) {
        this.code = exception.hashCode();
        this.message = exception.toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Response toResponse() {
        return Response.ok()
                .entity(this)
                .build();
    }
}
