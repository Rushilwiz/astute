package com.astute.resources;

import com.astute.exceptions.AstuteException;
import com.astute.requests.InvoicePaymentRequest;
import com.astute.response.ApiResponse;
import com.astute.service.AuthService;
import com.astute.service.InvoicePaymentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Path("/invoicePayment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InvoicePaymentResource {

    private com.astute.service.InvoicePaymentService service = new InvoicePaymentService();
    private com.astute.service.AuthService authService = new AuthService();

    public InvoicePaymentResource() {
    }

    @GET
    public Response getInvoicePayments(@QueryParam("sessionId") String sessionId, @QueryParam("invoiceNum") String invoiceNum)
            throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getInvoicePayments(invoiceNum)).toResponse();
    }

    @Path("/{invoiceNum}/{invoicePaymentId}")
    @PUT
    public Response updateInvoicePayment(@QueryParam("sessionId") String sessionId, InvoicePaymentRequest request, @PathParam("invoiceNum") String invoiceNum, @PathParam("invoicePaymentId") int invoicePaymentId, @PathParam("checkTransactionNo")String checkTransactionNo)
            throws AstuteException, ParseException {

        authService.authenticateSession(sessionId);
        String dateStr = request.getPaymentDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new java.sql.Date(df.parse(dateStr).getTime());
        service.updateInvoicePayment(invoiceNum,invoicePaymentId, request.getPaymentTypeId(), request.getInvoiceAmount(), date, request.getCheckNo(), request.getTransactionNo());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @POST
    public Response createInvoicePayment(@QueryParam("sessionId") String sessionId, InvoicePaymentRequest request)
            throws AstuteException, ParseException {
        authService.authenticateSession(sessionId);
        String dateStr = request.getPaymentDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new java.sql.Date(df.parse(dateStr).getTime());
        service.createInvoicePayment(request.getInvoiceNum(), request.getPaymentTypeId(), request.getInvoiceAmount(),date, request.getCheckNo(), request.getTransactionNo());
        return new ApiResponse(ApiResponse.UPDATE_ACCESS_SUCESS).toResponse();
    }

    @GET
    @Path("/paymentTypes")
    public Response getInvoicePaymentTypes(@QueryParam("sessionId") String sessionId) throws AstuteException {
        authService.authenticateSession(sessionId);
        return new ApiResponse(service.getPaymentTypes()).toResponse();
    }

}
