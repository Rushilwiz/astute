package com.astute.requests;

public class ServiceTypeRequest {
    int serviceTypeId;
    String serviceTypeDesc;

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }

    public ServiceTypeRequest() {}

    public ServiceTypeRequest(int serviceTypeId, String desc) {
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeDesc = serviceTypeDesc;
    }

}
