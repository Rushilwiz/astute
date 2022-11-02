package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.requests.PODetailRequest;
import com.astute.requests.POMasterRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.POService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Path("/po")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class POResource {

    private POService POService = new POService();
    private AuthService authService = new AuthService();

    public POResource() { }

    @GET
    public Response getPOMaster(@QueryParam("sessionId") String sessionId,
                                @QueryParam("PONum") String PONum,
                                @QueryParam("ContractNum") String contractNum,
                                @QueryParam("PODate") String PODate, String astuteProjectNumber)
            throws AstuteException, ParseException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(POService.getPOMaster(PONum, contractNum, PODate, astuteProjectNumber)).toResponse();
    }

    @Path("/detail")
    @GET
    public Response getPODetail(@QueryParam("sessionId") String sessionId, @QueryParam("PONum") String PONum, @QueryParam("lineItemNo") int lineItemNo)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(POService.getPODetail(PONum, lineItemNo)).toResponse();
    }

    @Path("/{PONum}")
    @PUT
    public Response updatePOMaster(@QueryParam("sessionId") String sessionId, @PathParam("PONum") String PONum, POMasterRequest request)
            throws AstuteException, ParseException {
        System.out.println("PODate in Resource is "+ request.getPODate());

        authService.authenticateSession(sessionId);
        POService.updatePOMaster(PONum, request.getContractNum(), request.getPODate(), request.getContractAmt(), request.getAstuteProjectNumber(), request.getTitle(), request.getNotes());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/detail/{PONum}/{lineItemNo}")
    @PUT
    public Response updatePODetail(@QueryParam("sessionId") String sessionId, @PathParam("PONum") String PONum, @PathParam("lineItemNo") int lineItemNo, PODetailRequest request)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        POService.updatePODetail(PONum, lineItemNo, request.getServiceDesc(), request.getFeeTypeId(),
                request.getQty(), request.getFee(), request.getServiceTypeId(), request.getRemainingQuantity());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/detail/{PONum}/{lineItemNo}")
    @DELETE
    public Response deletePODetail(@QueryParam("sessionId") String sessionId, @PathParam("PONum") String PONum, @PathParam("lineItemNo") int lineItemNo)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        POService.deletePODetail(PONum, lineItemNo);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createPOMaster(@QueryParam("sessionId") String sessionId, POMasterRequest request)
            throws AstuteException, ParseException {

        authService.authenticateSession(sessionId);
        POService.createPOMaster(request.getPoNum(), request.getContractNum(), request.getPODate(), request.getContractAmt(), request.getCustomerId(), request.getAstuteProjectNumber(),request.getTitle(), request.getNotes());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }
    @Path("/detail")
    @POST
    public Response createPODetail(@QueryParam("sessionId") String sessionId, PODetailRequest request)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        POService.createPODetail(request.getPoNum(), request.getLineItemNo(), request.getServiceDesc(), request.getFeeTypeId(),
                request.getQty(), request.getFee(), request.getServiceTypeId(), request.getRemainingQuantity());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    // Finalize
    @Path("/{PONum}/finalize")
    @PUT
    public Response finalizePO(@QueryParam("sessionId") String sessionId, @PathParam("PONum") String PONum) throws AstuteException {
        authService.authenticateSession(sessionId);
        POService.finalizePO(PONum);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    // delete
    @Path("/{PONum}/delete")
    @PUT
    public Response deletePO(@QueryParam("sessionId") String sessionId, @PathParam("PONum") String PONum) throws AstuteException {
        authService.authenticateSession(sessionId);
        POService.deletePO(PONum);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    // Misc
    @Path("/serviceTypes")
    @GET
    public Response getServiceTypes(@QueryParam("sessionId") String sessionId) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(POService.getServiceTypes()).toResponse();
    }

    @Path("/feeTypes")
    @GET
    public Response getFeeTypes(@QueryParam("sessionId") String sessionId) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(POService.getFeeTypes()).toResponse();
    }
}

