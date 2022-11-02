package com.astute.service;


import com.astute.exceptions.AstuteException;
import com.astute.model.User;

import java.sql.SQLException;

import static com.astute.dao.DAO.getDao;
import static com.astute.exceptions.AstuteException.AUTH_ERROR;

public class AuthService extends Service{
    public AuthService(){
        super();
    }

    public User login(String username, String password)
            throws AstuteException {
        return getDao().login(username,password);
    }

    public Integer authenticateSession(String sessionId) throws AstuteException {
        Integer userId = getDao().authenticateSession(sessionId);
        if (userId == null) {
            throw new AstuteException(AUTH_ERROR, "Authentication Error. Please login first!");
        }
        return userId;
    }

    public void logout(String sessionId) throws AstuteException {
        getDao().logout(sessionId);
    }
}
