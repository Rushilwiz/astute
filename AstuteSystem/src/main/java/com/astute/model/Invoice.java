package com.astute.model;

public class Invoice {
    String  invoiceNumber;
    String  invoiceDate;
    String  poNum;
    String  changeOrderNum;
    String  pmtStatusDesc;
    int     pmtStatus;
    Double  billAmt;
    String  specialNotes;
    String  certification;
    int     invoiceStatus;
    String  customerId;
    double  outstandingBalance;

    public Invoice(String invoiceNumber, String invoiceDate, String poNum, int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus, String pmtStatusDesc, String customerId, double outstandingBalance) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.poNum = poNum;
        this.pmtStatus = pmtStatus;
        this.billAmt = billAmt;
        this.specialNotes = specialNotes;
        this.certification = certification;
        this.invoiceStatus = invoiceStatus;
        this.pmtStatusDesc = pmtStatusDesc;
        this.customerId = customerId;
        this.outstandingBalance = outstandingBalance;
    }

    public String getInvoiceNumber() {

        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public String getChangeOrderNum() {
        return changeOrderNum;
    }

    public void setChangeOrderNum(String changeOrderNum) {
        this.changeOrderNum = changeOrderNum;
    }

    public String getPmtStatusDesc() {
        return pmtStatusDesc;
    }

    public void setPmtStatusDesc(String pmtStatusDesc) {
        this.pmtStatusDesc = pmtStatusDesc;
    }

    public int getPmtStatus() {
        return pmtStatus;
    }

    public void setPmtStatus(int pmtStatus) {
        this.pmtStatus = pmtStatus;
    }

    public Double getBillAmt() {
        return billAmt;
    }

    public void setBillAmt(Double billAmt) {
        this.billAmt = billAmt;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public int getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(int invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }
}
