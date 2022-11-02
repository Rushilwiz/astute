package com.astute.model;

import java.util.List;

public class GeneratedInvoice {

    Invoice invoice;
    List<InvoiceDetail> invoiceDetail;
    PO po;
    Customer customer;
    Double previouslyPaidAmt;
    Double balanceToBeBilled;


    public GeneratedInvoice(Invoice invoice, List<InvoiceDetail> invoiceDetail, PO po, Customer customer, Double previouslyPaidAmt, Double balanceToBeBilled) {
        this.invoice = invoice;
        this.invoiceDetail = invoiceDetail;
        this.po = po;
        this.customer = customer;
        this.previouslyPaidAmt = previouslyPaidAmt;
        this.balanceToBeBilled = balanceToBeBilled;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<InvoiceDetail> getInvoiceDetail() {
        return invoiceDetail;
    }

    public void setInvoiceDetail(List<InvoiceDetail> invoiceDetail) {
        this.invoiceDetail = invoiceDetail;
    }

    public PO getPo() {
        return po;
    }

    public void setPo(PO po) {
        this.po = po;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getPreviouslyPaidAmt() {
        return previouslyPaidAmt;
    }

    public void setPreviouslyPaidAmt(Double previouslyPaidAmt) {
        this.previouslyPaidAmt = previouslyPaidAmt;
    }

    public Double getBalanceToBeBilled() {
        return balanceToBeBilled;
    }

    public void setBalanceToBeBilled(Double balanceToBeBilled) {
        this.balanceToBeBilled = balanceToBeBilled;
    }
}
