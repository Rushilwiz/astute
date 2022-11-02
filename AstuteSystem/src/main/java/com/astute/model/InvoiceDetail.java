package com.astute.model;

public class InvoiceDetail {
    String invoiceNum;
    int lineItemNum;
    int poLineItemNum;
    int serviceTypeId;
    String desc;
    double qty;
    double fee;
    int feeTypeId;
    double draftRemainingQty;

    public InvoiceDetail(String invoiceNum, int lineItemNum, int poLineItemNum, int serviceTypeId, String desc, double qty, double fee, int feeTypeId, double draftRemainingQty) {
        this.invoiceNum = invoiceNum;
        this.lineItemNum = lineItemNum;
        this.poLineItemNum = poLineItemNum;
        this.serviceTypeId = serviceTypeId;
        this.desc = desc;
        this.qty = qty;
        this.fee = fee;
        this.feeTypeId = feeTypeId;
        this.draftRemainingQty = draftRemainingQty;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public int getLineItemNum() {
        return lineItemNum;
    }

    public void setLineItemNum(int lineItemNum) {
        this.lineItemNum = lineItemNum;
    }

    public int getPoLineItemNum() {
        return poLineItemNum;
    }

    public void setPoLineItemNum(int poLineItemNum) {
        this.poLineItemNum = poLineItemNum;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public double getDraftRemainingQty() {
        return draftRemainingQty;
    }

    public void setDraftRemainingQty(double draftRemainingQty) {
        this.draftRemainingQty = draftRemainingQty;
    }
}