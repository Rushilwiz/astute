package com.astute.service;

import com.astute.exceptions.*;
import com.astute.model.FeeType;
import com.astute.model.PO;
import com.astute.model.PODetail;
import com.astute.model.ServiceType;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static com.astute.dao.DAO.getDao;

public class POService extends Service{
    public POService(){
        super();
    }

    public List<PO> getPOMaster(String PONum, String contractNum, String PODate, String astuteProjectNumber)
            throws AstuteException {
        return getDao().getPOMaster(PONum, contractNum, PODate, astuteProjectNumber);
    }

    public List<PODetail>  getPODetail(String PONum, int lineItemNo)
            throws AstuteException {
        return getDao().getPODetail(PONum,lineItemNo);

    }


    public void updatePOMaster(String PONum, String contractNum, String PODate, Double contractAmt, String astuteProjectNumber, String title, String notes)
            throws AstuteException {
        System.out.print("PODate in Service is "+ PODate);
         getDao().updatePOMaster(PONum, contractNum, PODate, contractAmt, astuteProjectNumber, title, notes);
    }

    public void updatePODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, Double fee, int serviceTypeId, Double remainingQuantity)
            throws AstuteException {
         getDao().updatePODetail(POnum, lineItemNo, serviceDesc, feeTypeId, qty, fee, serviceTypeId, remainingQuantity);
    }

    public void createPOMaster(String PONum, String contractNum, String PODate, Double contractAmt, String customerId, String astuteProjectNumber, String title, String notes)
            throws AstuteException, ParseException {
        getDao().createPOMaster(PONum, contractNum, PODate, contractAmt, customerId, astuteProjectNumber, title, notes);
    }

    public void createPODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, Double fee, int serviceTypeId, Double remainingQuantity)
            throws AstuteException {
        getDao().createPODetail(POnum, lineItemNo, serviceDesc, feeTypeId, qty, fee, serviceTypeId, remainingQuantity);
    }

    public void finalizePO(String PONum) throws AstuteException {
        getDao().finalizePO(PONum);
    }

    public void deletePO(String PONum) throws AstuteException {
        getDao().deletePO(PONum);
    }

    public void deletePODetail(String PONum, int lineItemNo) throws AstuteException {
        getDao().deletePODetail(PONum, lineItemNo);
    }

    public List<ServiceType> getServiceTypes() throws AstuteException {
        return getDao().getServiceTypes();
    }


    public List<FeeType> getFeeTypes() throws AstuteException {
        return getDao().getFeeTypes();
    }

}
