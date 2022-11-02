package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.requests.CustomerContactRequest;
import com.astute.requests.CustomerRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.CustomerContactService;
import com.astute.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/customer/contact")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerContactResource {

    private com.astute.service.CustomerContactService service = new CustomerContactService();
    private com.astute.service.AuthService authService = new AuthService();

    public CustomerContactResource() {
    }

    @GET
    public Response getCustomerContacts(@QueryParam("sessionId") String sessionId, @QueryParam("customerId") String customerId)
            throws AstuteException {
        // TODO , @QueryParam("sessionId") String sessionId
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getCustomerContacts(customerId)).toResponse();
    }

    @Path("/{customerId}")
    @PUT
    public Response updateCustomerContact(@QueryParam("sessionId") String sessionId, @PathParam("customerId") String customerId, CustomerContactRequest request)
            throws AstuteException {
        System.out.println("in updateCustomerContact()");
        authService.authenticateSession(sessionId);
        service.updateCustomerContact(customerId, request.getContactId(), request.getName(), request.getTitle(), request.getWorkPhone(),
                request.getPhExt(), request.getMobile(), request.getFax(), request.getEmail(), request.getAddress());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/{customerId}/{contactId}/delete")
    @PUT
    public Response deleteCustomerContact(@QueryParam("sessionId") String sessionId, @PathParam("customerId") String customerId, @PathParam("contactId") int contactId)
            throws AstuteException {
        System.out.println("in deleteCustomerContact()");
        authService.authenticateSession(sessionId);
        service.deleteCustomerContact(customerId, contactId);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createCustomerContact(@QueryParam("sessionId") String sessionId, CustomerContactRequest request)
            throws AstuteException {
        System.out.println("in AstuteSyste CustomerContactRequest()");
        authService.authenticateSession(sessionId);
        service.createCustomerContact(request.getCustomerId(), request.getName(), request.getTitle(), request.getWorkPhone(),
                request.getPhExt(), request.getMobile(), request.getFax(), request.getEmail(), request.getAddress());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }
}
