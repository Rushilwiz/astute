package com.astute.service;

import com.astute.exceptions.AstuteException;
import com.astute.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static com.astute.dao.DAO.getDao;

public class ChangeOrderService extends Service{
    public ChangeOrderService(){
        super();
    }

    public List<ChangeOrder> getChangeOrders(String poNum)
            throws AstuteException {
        return getDao().getChangeOrders(poNum);
    }

    public void updateChangeOrder(String poNum, int changeOrderNum, double changeOrderAmt, Date changeOrderDate, String description)
            throws AstuteException {
        getDao().updateChangeOrder(poNum, changeOrderNum, changeOrderAmt, changeOrderDate, description);
    }


    public int createChangeOrder(String poNum, int changeOrderNum, double changeOrderAmt, Date changeOrderDate, String description)
            throws AstuteException {
        return getDao().createChangeOrder(poNum, changeOrderNum, changeOrderAmt, changeOrderDate, description);
    }

}
