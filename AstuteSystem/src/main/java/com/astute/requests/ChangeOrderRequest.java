package com.astute.requests;

import java.sql.Date;

public class ChangeOrderRequest {
    int changeOrderNum;
    String poNum;
    Date changeOrderDate;
    Double changeOrderAmt;
    String description;

    public int getChangeOrderNum() {
        return changeOrderNum;
    }

    public void setChangeOrderNum(int changeOrderNum) {
        this.changeOrderNum = changeOrderNum;
    }

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public Date getChangeOrderDate() {
        return changeOrderDate;
    }

    public void setChangeOrderDate(Date changeOrderDate) {
        this.changeOrderDate = changeOrderDate;
    }

    public Double getChangeOrderAmt() {
        return changeOrderAmt;
    }

    public void setChangeOrderAmt(Double changeOrderAmt) {
        this.changeOrderAmt = changeOrderAmt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
