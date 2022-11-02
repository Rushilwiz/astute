package com.astute.model;

public class CustomerContact {
    String customerId;
    int contactId;
    String name;
    String title;
    String workPhone;
    int phExt;
    String mobile;
    String fax;
    String email;
    String address;

    public CustomerContact(String customerId,int contactId,String name,String title,String workPhone,int phExt,String mobile, String fax, String email, String address) {
        this.customerId = customerId;
        this.contactId = contactId;
        this.name = name;
        this.title = title;
        this.workPhone = workPhone;
        this.phExt = phExt;
        this.mobile = mobile;
        this.fax = fax;
        this.email = email;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public int getPhExt() {
        return phExt;
    }

    public void setPhExt(int phExt) {
        this.phExt = phExt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
