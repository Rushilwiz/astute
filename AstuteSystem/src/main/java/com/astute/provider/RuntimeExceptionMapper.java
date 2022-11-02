package com.astute.provider;

import com.astute.response.ApiResponse;
import com.astute.response.ApiResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        e.printStackTrace();
        return new ApiResponse(e.hashCode(), e.getMessage()).toResponse();
    }
}

