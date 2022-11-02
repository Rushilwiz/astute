package com.astute.model;

import java.util.Date;

public class Customer {
    String customerId;
    String customerName;
    String billToDept;
    String add1;
    String add2;
    String city;
    String state;
    int zip;
    int ziplast4;
    String email;
    String phone;
    int phExt;
    String fax;

    public Customer(String customerId, String customerName, String billToDept, String add1, String add2, String city, String state, int zip, int ziplast4, String email, String phone, int phExt, String fax) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.billToDept = billToDept;
        this.add1 = add1;
        this.add2 = add2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.ziplast4 = ziplast4;
        this.email = email;
        this.phone = phone;
        this.phExt = phExt;
        this.fax = fax;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillToDept() {
        return billToDept;
    }

    public void setBillToDept(String billToDept) {
        this.billToDept = billToDept;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getZiplast4() {
        return ziplast4;
    }

    public void setZiplast4(int ziplast4) {
        this.ziplast4 = ziplast4;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public int getPhExt() {
        return phExt;
    }

    public void setPhExt(int phExt) {
        this.phExt = phExt;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

}
