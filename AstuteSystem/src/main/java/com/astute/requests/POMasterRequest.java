package com.astute.requests;

import java.sql.Date;

public class POMasterRequest {

    String poNum;
    String contractNum;
    String PODate;
    Double contractAmt;
    String customerId;
    String astuteProjectNumber;
    String title;
    String notes;

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getPODate() {
        return PODate;
    }

    public void setPODate(String PODate) {
        this.PODate = PODate;
    }

    public Double getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(Double contractAmt) {
        this.contractAmt = contractAmt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAstuteProjectNumber() {
        return astuteProjectNumber;
    }

    public void setAstuteProjectNumber(String astuteProjectNumber) {
        this.astuteProjectNumber = astuteProjectNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
