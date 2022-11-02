package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.model.User;
import com.astute.requests.LoginRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;

import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private com.astute.service.AuthService service = new AuthService();

    public AuthResource() {

    }

    @POST
    public Response login(LoginRequest request) throws AstuteException {
        User user = service.login(request.getUsername(), request.getPassword());
        if (user != null) {
            return new ApiResponse(user).toResponse();
        } else {
            return new ApiResponse(ApiResponse.ACCESS_DENIED).toResponse();
        }
    }
    @Path("/logout")
    @POST
    public Response logout(@QueryParam("sessionId") String sessionId) throws AstuteException {
        service.logout(sessionId);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }
}