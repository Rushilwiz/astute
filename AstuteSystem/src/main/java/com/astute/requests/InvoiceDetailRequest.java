package com.astute.requests;

public class InvoiceDetailRequest {
    String  invoiceNum;
    int     lineItemNum;
    int     poLineItemNum;
    int     serviceTypeId;
    String  desc;
    double  qty;
    double  fee;
    int     feeTypeId;

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
}