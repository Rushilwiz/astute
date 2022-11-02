package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.requests.CustomerRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/customer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private com.astute.service.CustomerService service = new CustomerService();
    private com.astute.service.AuthService authService = new AuthService();

    public CustomerResource() {
    }

    @GET
    public Response getCustomers(@QueryParam("sessionId") String sessionId, @QueryParam("customerId") String customerId)
            throws AstuteException {
        // TODO , @QueryParam("sessionId") String sessionId
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getCustomers(customerId)).toResponse();
    }

    @Path("/{poNumber}")
    @GET
    public Response getCustomer(@QueryParam("sessionId") String sessionId, @PathParam("poNumber") String poNumber)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getCustomer(poNumber)).toResponse();
    }

    @Path("/{customerId}")
    @PUT
    public Response updateCustomer(@QueryParam("sessionId") String sessionId, @PathParam("customerId") String customerId, CustomerRequest request)
            throws AstuteException {
        System.out.println("in updateCustomer()");
        authService.authenticateSession(sessionId);
        service.updateCustomer(customerId, request.getCustomerName(), request.getBillToDept(), request.getAdd1(),
                request.getAdd2(), request.getCity(), request.getState(), request.getZip(), request.getZiplast4(), request.getEmail(), request.getPhone(), request.getPhExt(), request.getFax());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/{customerId}/delete")
    @PUT
    public Response deleteCustomer(@QueryParam("sessionId") String sessionId, @PathParam("customerId") String customerId)
            throws AstuteException {
        System.out.println("in deleteCustomer()");
        authService.authenticateSession(sessionId);
        service.deleteCustomer(customerId);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createCustomer(@QueryParam("sessionId") String sessionId, CustomerRequest request)
            throws AstuteException {
        System.out.println("in AstuteSyste createCustomer()");
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.createCustomer(request.getCustomerId(), request.getCustomerName(), request.getBillToDept(), request.getAdd1(),
                request.getAdd2(), request.getCity(), request.getState(), request.getZip(), request.getZiplast4(), request.getEmail(), request.getPhone(), request.getPhExt(), request.getFax())).toResponse();
    }
}
