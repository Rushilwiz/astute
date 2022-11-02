package com.astute.servlets;

import com.astute.exceptions.AstuteException;
import com.astute.exceptions.DatabaseException;
import com.astute.dao.DAO;

import javax.servlet.http.HttpServlet;

public class StartupServlet extends HttpServlet {

    @Override
    public void init() {
        try {
            DAO.init();
        }
        catch (AstuteException e) {
            e.printStackTrace();
        }

    }
}
