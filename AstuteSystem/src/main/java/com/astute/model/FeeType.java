package com.astute.model;

public class FeeType {
    int feeTypeId;
    String feeTypeDesc;

    public FeeType(int feeTypeId, String feeTypeDesc) {
        this.feeTypeId = feeTypeId;
        this.feeTypeDesc = feeTypeDesc;
    }

    public int getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(int feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public String getFeeTypeDesc() {
        return feeTypeDesc;
    }

    public void setFeeTypeDesc(String feeTypeDesc) {
        this.feeTypeDesc = feeTypeDesc;
    }
}
