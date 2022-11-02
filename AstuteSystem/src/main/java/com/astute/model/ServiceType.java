package com.astute.model;


public class ServiceType {
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

    public ServiceType(int serviceTypeId, String serviceTypeDesc) {
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeDesc = serviceTypeDesc;
    }

}
