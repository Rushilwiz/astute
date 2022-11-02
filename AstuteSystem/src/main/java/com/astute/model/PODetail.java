package com.astute.model;

import java.io.Serializable;
import java.util.Date;

public class PODetail implements Serializable{
    String POnum;
    int lineItemNo;
    String serviceDesc;
    int feeTypeId;
    Double qty;
    Double fee;
    int serviceTypeId;
    Double remainingQty;

    public PODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, double fee, int serviceTypeId, Double remainingQty) {
        this.POnum = POnum;
        this.lineItemNo = lineItemNo;
        this.serviceDesc = serviceDesc;
        this.feeTypeId = feeTypeId;
        this.qty = qty;
        this.fee = fee;
        this.serviceTypeId = serviceTypeId;
        this.remainingQty = remainingQty;
    }

    public String getPOnum() {
        return POnum;
    }

    public void setPOnum(String POnum) {
        this.POnum = POnum;
    }

    public int getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(int lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public int getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getFee() { return fee; }

    public void setFee(Double fee) { this.fee = fee; }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Double getRemainingQty() {return remainingQty; }

    public void setRemainingQty(Double remainingQty) { this.remainingQty = remainingQty;}

}

