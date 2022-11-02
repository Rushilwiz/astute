package com.astute.model;

public class PaymentStatus {
    int paymentStatusId;
    String desc;

    public PaymentStatus(int paymentStatusId, String desc) {
        this.paymentStatusId = paymentStatusId;
        this.desc = desc;
    }

    public int getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
