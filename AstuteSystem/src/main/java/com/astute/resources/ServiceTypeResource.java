package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.requests.CustomerRequest;
import com.astute.requests.ServiceTypeRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.CustomerService;
import com.astute.service.ServiceTypeService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/serviceType")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceTypeResource {

    private com.astute.service.ServiceTypeService service = new ServiceTypeService();
    private com.astute.service.AuthService authService = new AuthService();

    public ServiceTypeResource() {
    }

    @GET
    public Response getServiceTypes(@QueryParam("sessionId") String sessionId)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getServiceTypes()).toResponse();
    }

    @Path("/{serviceTypeId}")
    @PUT
    public Response updateServiceType(@QueryParam("sessionId") String sessionId, ServiceTypeRequest request)
            throws AstuteException {
        System.out.println("in updateServiceType()");
        authService.authenticateSession(sessionId);
        service.updateServiceType(request.getServiceTypeId(), request.getServiceTypeDesc());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/delete/{serviceTypeId}")
    @PUT
    public Response deleteServiceType(@QueryParam("sessionId") String sessionId, @PathParam("serviceTypeId") int serviceTypeId)
            throws AstuteException {
        System.out.println("in deleteServiceType()");
        authService.authenticateSession(sessionId);
        service.deleteServiceType(serviceTypeId);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createServiceType(@QueryParam("sessionId") String sessionId, ServiceTypeRequest request)
            throws AstuteException {
        System.out.println("in AstuteSyste createServiceType()");
        authService.authenticateSession(sessionId);
        service.createServiceType(request.getServiceTypeDesc());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }
}
