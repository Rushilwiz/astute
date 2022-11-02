package com.astute.requests;


public class InvoicePaymentRequest {
    String invoiceNum;
    int invoicePaymentId;
    int paymentTypeId;
    String paymentType;
    String paymentDate;
    Double invoiceAmount;
    String checkNo;
    String transactionNo;

    public InvoicePaymentRequest(String invoiceNum, int invoicePaymentId,  int paymentTypeId, String paymentType, String paymentDate, Double invoiceAmount, String checNo, String transactionNo) {
        this.invoiceNum = invoiceNum;
        this.invoicePaymentId = invoicePaymentId;
        this.paymentTypeId = paymentTypeId;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
        this.invoiceAmount = invoiceAmount;
        this.checkNo  = checkNo;
        this.transactionNo  = transactionNo;
    }

    public InvoicePaymentRequest() {

    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public int getInvoicePaymentId() {
        return invoicePaymentId;
    }

    public void setInvoicePaymentId(int invoicePaymentId) {
        this.invoicePaymentId = invoicePaymentId;
    }

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }
}
