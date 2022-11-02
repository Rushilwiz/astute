package com.astute.service;

import com.astute.exceptions.AstuteException;
import com.astute.model.InvoicePayment;
import com.astute.model.PaymentType;

import java.sql.Date;
import java.util.List;

import static com.astute.dao.DAO.getDao;

public class InvoicePaymentService extends Service{
    public InvoicePaymentService(){
        super();
    }

    public List<InvoicePayment> getInvoicePayments(String invoiceNum)
            throws AstuteException {
        return getDao().getInvoicePayments(invoiceNum);
    }

    public void updateInvoicePayment(String invoiceNum, int invoicePaymentId, int InvoicePaymentTypeId, Double paymentAmount, Date paymentDate, String checkNo, String transactionNo)
            throws AstuteException {
        getDao().updateInvoicePayment(invoiceNum, invoicePaymentId, InvoicePaymentTypeId, paymentAmount, paymentDate, checkNo, transactionNo);
    }


    public void createInvoicePayment(String invoiceNum, int invoicePaymentTypeId, Double paymentAmount, Date paymentDate, String checkNo, String transactionNo)
            throws AstuteException {
        getDao().createInvoicePayment(invoiceNum, invoicePaymentTypeId, paymentAmount, paymentDate, checkNo, transactionNo);
    }

    public List<PaymentType> getPaymentTypes() throws AstuteException {
        return getDao().getPaymentTypes();
    }
}
