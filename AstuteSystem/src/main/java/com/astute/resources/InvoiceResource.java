package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.exceptions.DatabaseException;
import com.astute.requests.InvoiceDetailRequest;
import com.astute.requests.InvoiceMasterRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.InvoiceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Path("/invoice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResource {

    private com.astute.service.InvoiceService service = new InvoiceService();
    private com.astute.service.AuthService authService = new AuthService();

    public InvoiceResource() {
    }

    @GET
    public Response getInvoiceMaster(@QueryParam("sessionId") String sessionId, @QueryParam("invoiceNumber") String invoiceNumber, @QueryParam("pmtStatus") int pmtStatus)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getInvoiceMaster(invoiceNumber, pmtStatus)).toResponse();
    }

    @Path("/paymentStatuses")
    @GET
    public Response getPaymentStatuses(@QueryParam("sessionId") String sessionId) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getPaymentStatuses()).toResponse();
    }

    @Path("/detail")
    @GET
    public Response getInvoiceDetail(@QueryParam("sessionId") String sessionId, @QueryParam("invoiceNumber") String invoiceNumber, @QueryParam("lineItemNo") int lineItemNo)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getInvoiceDetail(invoiceNumber,lineItemNo)).toResponse();
    }

    @Path("/{InvoiceNum}")
    @PUT
    public Response updateInvoiceMaster(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNum") String InvoiceNum, InvoiceMasterRequest request)
            throws AstuteException, ParseException {

        authService.authenticateSession(sessionId);
        service.updateInvoiceMaster(InvoiceNum, request.getInvoiceDate(), request.getPoNum(),
                request.getPmtStatus(), request.getBillAmt(), request.getSpecialNotes(), request.getCertification(), request.getInvoiceStatus());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/{InvoiceNum}/delete")
    @PUT
    public Response deleteInvoice(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNum") String InvoiceNum)
            throws AstuteException {

        authService.authenticateSession(sessionId);
        service.deleteInvoice(InvoiceNum);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/detail/{InvoiceNum}/{lineItemNum}")
    @PUT
    public Response updateInvoiceDetail(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNum") String InvoiceNum, @PathParam("lineItemNum") int lineItemNum, InvoiceDetailRequest request)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        service.updateInvoiceDetail(InvoiceNum, lineItemNum, request.getPoLineItemNum(), request.getServiceTypeId(),
                request.getDesc(), request.getQty(), request.getFee(), request.getFeeTypeId());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/detail/{InvoiceNum}/{lineItemNum}")
    @DELETE
    public Response deleteInvoiceDetail(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNum") String InvoiceNum, @PathParam("lineItemNum") int lineItemNum, @QueryParam("poLineItemNum") int poLineItemNum)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        service.deleteInvoiceDetail(InvoiceNum, lineItemNum,poLineItemNum);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createInvoiceMaster(@QueryParam("sessionId") String sessionId, InvoiceMasterRequest request)
            throws AstuteException, ParseException {

        authService.authenticateSession(sessionId);
        service.createInvoiceMaster(request.getInvoiceNumber(), request.getInvoiceDate(), request.getPoNum(),
                request.getPmtStatus(), request.getBillAmt(), request.getSpecialNotes(), request.getCertification(), request.getInvoiceStatus());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }
    @Path("/detail")
    @POST
    public Response createInvoiceDetail(@QueryParam("sessionId") String sessionId, InvoiceDetailRequest request)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        service.createInvoiceDetail(request.getInvoiceNum(), request.getLineItemNum(), request.getPoLineItemNum(), request.getServiceTypeId(),
                request.getDesc(), request.getQty(), request.getFee(), request.getFeeTypeId());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/generatedInvoice/{InvoiceNum}")
    @GET
    public Response getGeneratedInvoice(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNum") String InvoiceNum) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getGeneratedInvoice(InvoiceNum)).toResponse();
    }

    @Path("/generateInvoiceNumber/{PONum}")
    @GET
    public Response generateInvoiceNumber(@QueryParam("sessionId") String sessionId, @PathParam("PONum") String PONum) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.generateInvoiceNumber(PONum)).toResponse();
    }

    @Path("/{InvoiceNumber}/submit")
    @PUT
    public Response submitInvoice(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNumber") String InvoiceNumber) throws AstuteException {
        authService.authenticateSession(sessionId);
        service.submitInvoice(InvoiceNumber);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/{InvoiceNumber}/void")
    @PUT
    public Response voidInvoice(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNumber") String InvoiceNumber) throws AstuteException {
        authService.authenticateSession(sessionId);
        service.voidInvoice(InvoiceNumber);
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @Path("/{InvoiceNumber}/duplicate")
    @PUT
    public Response duplicateInvoice(@QueryParam("sessionId") String sessionId, @PathParam("InvoiceNumber") String InvoiceNumber) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.dupliateInvoice(InvoiceNumber)).toResponse();
    }

    @Path("/submitted")
    @GET
    public Response getSubmittedInvoiceNumbers(@QueryParam("sessionId") String sessionId) throws AstuteException {
        System.out.println("In getSubmittedInvoiceNumbers");
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getSubmittedInvoiceNumbers()).toResponse();
    }

}
