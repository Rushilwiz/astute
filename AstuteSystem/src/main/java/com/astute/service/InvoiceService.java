package com.astute.service;

import com.astute.exceptions.*;
import com.astute.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static com.astute.dao.DAO.getDao;

public class InvoiceService extends Service{
    public InvoiceService(){
        super();
    }

    public List<Invoice> getInvoiceMaster(String invoiceNumber, int pmtStatus)
            throws AstuteException {
        return getDao().getInvoiceMaster(invoiceNumber, pmtStatus);
    }

    public List<InvoiceDetail>  getInvoiceDetail(String invoiceNumber, int lineItemNo)
            throws AstuteException {
        return getDao().getInvoiceDetail(invoiceNumber, lineItemNo);
    }

    public void updateInvoiceMaster(String invoiceNum, String invoiceDate, String PONum,
                                    int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus)
            throws AstuteException {
        getDao().updateInvoiceMaster(invoiceNum, invoiceDate, PONum, pmtStatus, billAmt, specialNotes, certification, invoiceStatus);
    }

    public void deleteInvoice(String invoiceNum)
            throws AstuteException {
        getDao().deleteInvoice(invoiceNum);
    }

    public void updateInvoiceDetail(String invoiceNum, int lineItemNum, int POLineItemNum, int serviceTypeId, String desc,
                                    double qty, double fee, int feeTypeId)
            throws AstuteException {
        getDao().updateInvoiceDetail(invoiceNum, lineItemNum, POLineItemNum, serviceTypeId, desc,
                qty, fee, feeTypeId);
    }

    public void deleteInvoiceDetail(String invoiceNum, int lineItemNum, int poLineItemNum)
            throws AstuteException {
        getDao().deleteInvoiceDetail(invoiceNum, lineItemNum, poLineItemNum);
    }


    public void createInvoiceMaster(String invoiceNum, String invoiceDate, String PONum,
                                    int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus)
            throws AstuteException, ParseException {
        getDao().createInvoiceMaster(invoiceNum, invoiceDate, PONum, pmtStatus, billAmt, specialNotes, certification, invoiceStatus);
    }

    public void createInvoiceDetail(String invoiceNum, int lineItemNum, int POLineItemNum, int serviceTypeId, String desc,
                                    double qty, double fee, int feeTypeId)
            throws AstuteException {
        getDao().createInvoiceDetail(invoiceNum, lineItemNum, POLineItemNum, serviceTypeId, desc,
                qty, fee, feeTypeId);
    }

    public GeneratedInvoice getGeneratedInvoice(String invoiceNumber)
            throws AstuteException {
        return getDao().getGeneratedInvoice(invoiceNumber);
    }

    public String generateInvoiceNumber(String poNum)
            throws AstuteException {
        return getDao().generateInvoiceNumber(poNum);
    }

    public void submitInvoice(String InvoiceNumber) throws AstuteException {
        getDao().submitInvoice(InvoiceNumber);
    }

    public void voidInvoice(String InvoiceNumber) throws AstuteException {
        getDao().voidInvoice(InvoiceNumber);
    }

    public String dupliateInvoice(String InvoiceNumber) throws AstuteException {
        return getDao().dupliateInvoice(InvoiceNumber);
    }

    public List<PaymentStatus> getPaymentStatuses() throws AstuteException {
        return getDao().getPaymentStatuses();
    }

    public List<Invoice> getSubmittedInvoiceNumbers() throws AstuteException {
        return getDao().getSubmittedInvoiceNumbers();
    }

}
