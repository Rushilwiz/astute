package com.astute.service;

import com.astute.exceptions.AstuteException;
import com.astute.model.ServiceType;
import com.astute.model.User;

import java.util.List;

import static com.astute.dao.DAO.getDao;
import static com.astute.exceptions.AstuteException.AUTH_ERROR;

public class ServiceTypeService extends Service{
    public ServiceTypeService(){
        super();
    }

    public List<ServiceType> getServiceTypes()
            throws AstuteException {
        return getDao().getServiceTypes();
    }

    public void createServiceType(String serviceTypeDesc) throws AstuteException {
        getDao().createServiceType(serviceTypeDesc);
    }

    public void updateServiceType(int serviceTypeId, String serviceTypeDesc) throws AstuteException {
        getDao().updateServiceType(serviceTypeId, serviceTypeDesc);
    }

    public void deleteServiceType(int serviceTypeId) throws AstuteException {
        getDao().deleteServiceType(serviceTypeId);
    }

}
