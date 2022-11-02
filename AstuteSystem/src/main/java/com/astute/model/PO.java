package com.astute.model;

import java.io.Serializable;
import java.util.Date;


public class PO implements Serializable{

    private String         contractNum;
    private String         PODate;
    private Double         contractAmt;
    private String         customerId;
    private String         astuteProjectNumber;
    private String         title;
    private Double         previouslyBilledAmount;
    private int            invoiceSequence;
    private String         notes;
    private boolean        isFinal;



    private boolean        oneInvInDraft;

    private boolean        fulfilled;

    public PO(String PONum, String contractNum, String PODate, String customerId, Double contractAmt, String astuteProjectNum, String title, Double previouslyBilledAmount, int invoiceSequence, String notes, boolean isFinal, boolean oneInvInDraft, boolean fulfilled) {
        this.PONum = PONum;
        this.contractNum = contractNum;
        this.PODate = PODate;
        this.customerId = customerId;
        this.contractAmt = contractAmt;
        this.astuteProjectNumber = astuteProjectNum;
        this.title = title;
        this.previouslyBilledAmount = previouslyBilledAmount;
        this.invoiceSequence = invoiceSequence;
        this.notes = notes;
        this.isFinal = isFinal;
        this.oneInvInDraft = oneInvInDraft;
        this.fulfilled = fulfilled;
    }

    private String         PONum;

    public String getPONum() {
        return PONum;
    }

    public void setPONum(String PONum) {
        this.PONum = PONum;
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

    public Double getPreviouslyBilledAmount() {
        return previouslyBilledAmount;
    }

    public void setPreviouslyBilledAmount(Double previouslyBilledAmount) {
        this.previouslyBilledAmount = previouslyBilledAmount;
    }

    public int getInvoiceSequence() {
        return invoiceSequence;
    }

    public void setInvoiceSequence(int invoiceSequence) {
        this.invoiceSequence = invoiceSequence;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isOneInvInDraft() {
        return oneInvInDraft;
    }

    public void setOneInvInDraft(boolean oneInvInDraft) {
        this.oneInvInDraft = oneInvInDraft;
    }


    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
}