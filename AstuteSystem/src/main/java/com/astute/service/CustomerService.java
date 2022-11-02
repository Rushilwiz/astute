package com.astute.service;

import com.astute.exceptions.AstuteException;
import com.astute.model.Customer;

import java.sql.SQLException;
import java.util.List;

import static com.astute.dao.DAO.getDao;

public class CustomerService extends Service{
    public CustomerService(){
        super();
    }

    public List<Customer> getCustomers(String customerId)
            throws AstuteException {

        return getDao().getCustomers(customerId);
    }

    public Customer getCustomer(String poNumber)
            throws AstuteException {
        return getDao().getCustomer(poNumber);
    }

    public void updateCustomer( String customerId, String customerName, String billToDept, String add1, String add2, String city, String state, int zip, int ziplast4, String email, String phone, int phExt, String fax)
            throws AstuteException {
        getDao().updateCustomer(customerId, customerName,billToDept, add1, add2, city, state, zip, ziplast4, email, phone, phExt, fax);
    }

    public void deleteCustomer(String customerId)
            throws AstuteException {
        getDao().deleteCustomer(customerId);
    }

    public String createCustomer(String customerId, String customerName, String billToDept, String add1, String add2, String city, String state, int zip, int ziplast4, String email, String phone, int phExt, String fax)
            throws AstuteException {
        return getDao().createCustomer(customerId, customerName,billToDept, add1, add2, city, state, zip, ziplast4, email, phone, phExt, fax);
    }

}
