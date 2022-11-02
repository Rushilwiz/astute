package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.requests.ChangeOrderRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.ChangeOrderService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/changeOrder")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChangeOrderResource {

    private com.astute.service.ChangeOrderService service = new ChangeOrderService();
    private com.astute.service.AuthService authService = new AuthService();


    public ChangeOrderResource() {
    }

    @GET
    public Response getChangeOrders(@QueryParam("sessionId") String sessionId, @QueryParam("poNum") String poNum)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getChangeOrders(poNum)).toResponse();
    }

    @Path("/{PONum}/{changeOrderNum}")
    @PUT
    public Response updateChangeOrder(@QueryParam("sessionId") String sessionId, ChangeOrderRequest request, @PathParam("PONum") String PONum, @PathParam("changeOrderNum") int changeOrderNum)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        service.updateChangeOrder(PONum,changeOrderNum, request.getChangeOrderAmt(), request.getChangeOrderDate(), request.getDescription());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createChangeOrder(@QueryParam("sessionId") String sessionId, ChangeOrderRequest request)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.createChangeOrder(request.getPoNum(), request.getChangeOrderNum(), request.getChangeOrderAmt(),
                request.getChangeOrderDate(), request.getDescription())).toResponse();
    }
}
