package com.astute.service;

import com.astute.exceptions.AstuteException;
import com.astute.model.CustomerContact;

import java.util.List;

import static com.astute.dao.DAO.getDao;

public class CustomerContactService extends Service{
    public CustomerContactService(){
        super();
    }

    public List<CustomerContact> getCustomerContacts(String customerId)
            throws AstuteException {
        return getDao().getCustomerContacts(customerId);
    }

    public String createCustomerContact(String customerId, String name,String title,String workPhone,int phExt,String mobile, String fax, String email, String address) throws AstuteException {
        return getDao().createCustomerContact(customerId, name, title, workPhone, phExt, mobile, fax, email, address);
    }

    public void updateCustomerContact(String customerId, int contactId, String name,String title,String workPhone,int phExt,String mobile, String fax, String email, String address) throws AstuteException {
        getDao().updateCustomerContact(customerId, contactId, name,title,workPhone,phExt,mobile, fax, email, address);
    }

    public void deleteCustomerContact(String customerId, int contactId) throws AstuteException {
            getDao().deleteCustomerContact(customerId, contactId);
    }
}
