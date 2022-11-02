package com.astute.dao;

import com.astute.exceptions.AstuteException;
import com.astute.model.*;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.astute.exceptions.AstuteException.DB_ERROR;

public abstract class DAO {
    public static DAO dao;

    static String database;
    static String host;
    static int port;
    static String username;
    static String password;
    static String schema;
    static int interval = 30;

    /**
     * @return The static instance of the DaoHelper
     */
    public static DAO getDao() throws AstuteException {
        if (dao == null) {
            throw new AstuteException(0,"Review db.config file.");
        }
        return dao;
    }

    public static String getDatabase() {
        return database;
    }

    /**
     * This method is called from the StartupServlet, with the properties being read from db.conf
     *
     * @param
     * @throws AstuteException
     */
    public static void init() throws AstuteException {
//        try {
//            Properties props = new Properties();
//            InputStream input = new FileInputStream("db.config");

            // load a properties file
//            props.load(input);

            //get properties
//            database = props.getProperty("database");
//            host = props.getProperty("host");
//            port = Integer.parseInt(props.getProperty("port"));
//            schema = props.getProperty("schema");
//            username = props.getProperty("username");
//            password = props.getProperty("password");
            database = "sql";
            host = "localhost";
            port = 3306;
            schema = "astute";
            username = "astute_user";
            password = "password";
//            System.out.println("=============================================");
//            System.out.println("host is " + host);
//            System.out.println("port is " + port);
//            System.out.println("schema is " + schema);
//            System.out.println("username is " + username);
//            System.out.println("password is " + password);
//            System.out.println("=============================================");
            dao = new SqlDAO();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public Connection conn;

    public DAO() throws AstuteException {
        //connect to database
        connect();
    }

    /**
     * Abstract method to establish a connection to the database
     *
     * @throws AstuteException if the database is not running or if there are any other connection issues
     */
    public abstract void connect() throws AstuteException;

    public abstract List<PO> getPOMaster(String PONum, String contractNum, String PODate, String astuteProjectNumber) throws AstuteException;

    public abstract List<PODetail> getPODetail(String PONum, int lineItemNo) throws AstuteException;

    public abstract void updatePOMaster(String PONum, String contractNum, String PODate, Double contractAmt, String astuteProjectNumber, String title, String notes) throws AstuteException;

    public abstract void updatePODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, Double fee, int serviceTypeId, Double remainingQuantity) throws AstuteException;

    public abstract void deletePODetail(String POnum, int lineItemNo) throws AstuteException;

    public abstract boolean isPOFinalized(String POnum) throws AstuteException;

    public abstract void createPOMaster(String PONum, String contractNum, String PODate, Double contractAmt, String customerId, String astuteProjectNumber, String title, String notes) throws AstuteException, ParseException;

    public abstract void createPODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, Double fee, int serviceTypeId, Double remainingQuantity) throws AstuteException;

    public abstract void finalizePO(String PONum) throws AstuteException;

    public abstract void deletePO(String PONum) throws AstuteException;

    public abstract List<ServiceType> getServiceTypes() throws AstuteException;

    public abstract List<FeeType> getFeeTypes() throws AstuteException;

    public abstract void createServiceType(String desc) throws AstuteException;

    public abstract void updateServiceType(int serviceTypeId, String desc) throws AstuteException;

    public abstract void deleteServiceType(int serviceTypeId) throws AstuteException;

    public abstract List<Invoice> getInvoiceMaster(String invoiceNumber, int pmtStatus) throws AstuteException;

    public abstract List<InvoiceDetail> getInvoiceDetail(String invoiceNum, int lineItemNo) throws AstuteException;

    public abstract void updateInvoiceMaster(String invoiceNum, String invoiceDate, String PONum, int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus) throws AstuteException;

    public abstract void deleteInvoice(String invoiceNum) throws AstuteException;

    public abstract void updateInvoiceDetail(String invoiceNum, int lineItemNum, int POLineItemNum, int serviceTypeId, String desc, double qty, double fee, int feeTypeId) throws AstuteException;

    public abstract void deleteInvoiceDetail(String invoiceNum, int lineItemNum, int poLineItemNum) throws AstuteException;

    public abstract void createInvoiceMaster(String invoiceNum, String invoiceDate, String PONum, int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus) throws AstuteException, ParseException;

    public abstract void createInvoiceDetail(String invoiceNum, int lineItemNum, int POLineItemNum, int serviceTypeId, String desc, double qty, double fee, int feeTypeId) throws AstuteException;

    public abstract String generateInvoiceNumber(String poNum) throws AstuteException;

    public abstract GeneratedInvoice getGeneratedInvoice(String invoiceNumber)throws AstuteException;

    public abstract void submitInvoice(String InvoiceNumber) throws AstuteException;

    public abstract void voidInvoice(String InvoiceNumber) throws AstuteException;

    public abstract String dupliateInvoice(String InvoiceNumber) throws AstuteException;

    public abstract List<Invoice> getSubmittedInvoiceNumbers() throws AstuteException;

    public abstract List<PaymentStatus> getPaymentStatuses() throws AstuteException;

    public abstract double getOutstandingBalance(String InvoiceNumber) throws AstuteException;

    public abstract List<Customer> getCustomers(String customerId) throws AstuteException;

    public abstract Customer getCustomer(String poNumber) throws AstuteException;

    public abstract String createCustomer(String customerId, String customerName, String billToDept, String add1, String add2, String city, String state, int zip, int ziplast4, String email, String phone, int phExt, String fax) throws AstuteException;

    public abstract void updateCustomer(String customerId, String customerName, String billToDept, String add1, String add2, String city, String state, int zip, int ziplast4, String email, String phone, int phExt, String fax) throws AstuteException;

    public abstract void deleteCustomer(String customerId) throws AstuteException;


    /*=============================== Customer Contacts Methods ===============================================
     */
    public abstract List<CustomerContact> getCustomerContacts(String customerId) throws AstuteException;

    public abstract String createCustomerContact(String customerId, String name,String title,String workPhone,int phExt,String mobile, String fax, String email, String address) throws AstuteException;

    public abstract void updateCustomerContact(String customerId, int contactId, String name,String title,String workPhone,int phExt,String mobile, String fax, String email, String address) throws AstuteException;

    public abstract void deleteCustomerContact(String customerId, int contactId) throws AstuteException;

    // User and session method implementation

    public abstract ResultSet executeQuery(String sessionId, String sql) throws AstuteException ;

    public abstract Integer authenticateSession(String sessionId) throws AstuteException;

    public abstract User getUser(String username) throws AstuteException ;

    public abstract void createSession(int userId, String sessionId) throws AstuteException;

    public abstract void deleteSession(String sessionId);

    public abstract String getSessionUsername(String sessionId) throws AstuteException;

    public abstract User login(String username, String password) throws AstuteException;

    public abstract void logout(String sessionId) throws AstuteException;

    public abstract List<ChangeOrder> getChangeOrders(String poNum) throws AstuteException;

    public abstract void updateChangeOrder(String poNum, int changeOrderNum, double changeOrderAmt, Date changeOrderDate, String description) throws AstuteException;

    public abstract int createChangeOrder(String poNum, int changeOrderNum, double changeOrderAmt, Date changeOrderDate, String description) throws AstuteException;

    public abstract List<InvoicePayment> getInvoicePayments(String invoiceNum) throws AstuteException;

    public abstract void updateInvoicePayment(String invoiceNum, int invoicePaymentId, int InvoicePaymentTypeId, Double paymentAmount, Date paymentDate, String checkNo, String transactionNo) throws AstuteException;

    public abstract void createInvoicePayment(String invoiceNum, int invoicePaymentTypeId, Double paymentAmount, Date paymentDate, String checkNo, String transactionNo) throws AstuteException;

    public abstract List<PaymentType> getPaymentTypes() throws AstuteException;
}