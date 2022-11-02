package com.astute.dao;

import com.astute.exceptions.AstuteException;
import com.astute.model.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.text.SimpleDateFormat;

import static com.astute.exceptions.AstuteException.AUTH_ERROR;
import static com.astute.exceptions.AstuteException.DB_ERROR;

public class SqlDAO extends DAO {
    String key = "astutesecret";
    public SqlDAO() throws AstuteException {
    }

    @Override
    public void connect() throws AstuteException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + schema, username, password);
            System.out.println("Connected to MySQL");
        } catch (Exception e) {
            throw new AstuteException(e.hashCode(), e.getMessage());
        }
    }

    /*
    =============================== PO Methods ===============================================
    */

    public List<PO> getPOMaster(String PONum, String contractNum, String PODate, String astuteProjectNumber) throws AstuteException {
        try {
            List<PO> pos = new ArrayList<>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT PO_num, contract_num, PO_date, customer_id, contract_amt, astute_project_num , title, 0, inv_seq, notes, final, isAnyInvInDraft(PO_num), is_po_fulfilled(PO_num) as fulfilled FROM PO ";
            if (PONum != null && !PONum.isEmpty()) {
                sql += "WHERE UPPER(PO_num) = '" + PONum.toUpperCase() + "'";
            } else if (contractNum != null && !contractNum.isEmpty()) {
                sql += "WHERE UPPER(contract_num) = '" + contractNum.toUpperCase()+ "'";
            } else if (PODate != null) {
                sql += "WHERE PO_date = STR_TO_DATE('" + PODate + "', '%Y-%m-%d')";
            } else if (astuteProjectNumber!= null && !astuteProjectNumber.isEmpty() ) {
                sql += "WHERE UPPER(astute_project_num) = '" + astuteProjectNumber.toUpperCase()+ "'";
            }

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String poNum = rs.getString(1);
                String cntrctNum = rs.getString(2);
                Date poDate = rs.getDate(3);
                String customerId = rs.getString(4);
                Double contractAmt = rs.getDouble(5);
                String astuteProjectNum = rs.getString(6);
                String title = rs.getString(7);
                Double previouslyBilledAmount = rs.getDouble(8);
                int invoiceSequence = rs.getInt(9);
                String notes = rs.getString(10);
                boolean isFinal = rs.getInt(11) == 0 ? false : true;
                boolean oneInvInDraft = rs.getInt(12) == 0 ? false : true;
                boolean isfulfilled = rs.getInt(13) == 0 ? false : true;
                String date = null;
                if (poDate != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    date = formatter.format(poDate);
                }
                PO po = new PO(poNum, cntrctNum, date, customerId, contractAmt,astuteProjectNum,title,previouslyBilledAmount,invoiceSequence, notes, isFinal, oneInvInDraft, isfulfilled);
                pos.add(po);
            }
            return pos;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<PODetail> getPODetail(String PONum, int lineItemNo) throws AstuteException {
        try {
            List<PODetail> poDetails = new ArrayList<>();
            Statement stmt = conn.createStatement();
            String whereClause = " WHERE ";
            boolean whereClauseCriteria = false;
            String sql = "SELECT PO_num, line_item_no, service_desc, fee_type_id, qty, fee, service_type_id, remaining_qty FROM PO_DETAIL ";
            if (PONum != null) {
                whereClause = whereClause + " UPPER(PO_num) = '" + PONum.toUpperCase() + "'";
                whereClauseCriteria = true;
            }
            if (lineItemNo > 0) {
                if (whereClauseCriteria) {
                    whereClause += " AND ";
                }
                whereClause = whereClause + " line_item_no = " + lineItemNo;
            }
            sql += whereClause;
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String poNum = rs.getString(1);
                int lineItemNum = rs.getInt(2);
                String serviceDesc = rs.getString(3);
                int feeTypeId = rs.getInt(4);
                Double qty = rs.getDouble(5);
                Double fee = rs.getDouble(6);
                int serviceTypeId = rs.getInt(7);
                double remainingQty = rs.getDouble(8);
                PODetail poDetail = new PODetail(poNum, lineItemNum, serviceDesc, feeTypeId, qty, fee, serviceTypeId, remainingQty);
                poDetails.add(poDetail);
            }
            return poDetails;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void updatePOMaster(String PONum, String contractNum, String PODate, Double contractAmt, String astuteProjectNumber, String title, String notes) throws AstuteException {
        try {
            System.out.println("PODate in SQLDAO is "+ PODate);

            String sql = "UPDATE PO ";
            String updateClause = " SET ";
            String whereClause = "";
            if (PONum == null || PONum.isEmpty()) {
                throw new AstuteException(0, "PO Number null.");
            } else {
                whereClause = " WHERE UPPER(PO_num) ='" + PONum.toUpperCase() + "'";
            }

            updateClause = updateClause + " contract_num = '" + contractNum + "',";
            updateClause = updateClause + " PO_Date = STR_TO_DATE('" + PODate + "', '%Y-%m-%d')" + ",";
            updateClause = updateClause + " contract_amt = " + contractAmt+ ",";
            updateClause = updateClause + " astute_project_num = '" + astuteProjectNumber +"',";
            updateClause = updateClause + " title = '" + title +"',";
            updateClause = updateClause + " notes = '" + notes +"'";
            sql = sql+ updateClause + whereClause;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public String trimLastCharacter(String sql, String character) {
        sql = sql.trim();
        if (sql.endsWith(character)) {
            sql = sql.substring(0, sql.length() - 1);
        }
        return sql;

    }

    public void updateContractAmount(String PONum) throws AstuteException {
        try {
            CallableStatement stmt = conn.prepareCall("{call update_contract_amt(?)}");
            stmt.setString(1, PONum);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void updatePODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, Double fee, int serviceTypeId, Double remainingQuantity) throws AstuteException {
        try {
            // remainingQuantity is not used , need to take it out from signature eventually
            String sql = "UPDATE PO_DETAIL ";
            String updateClause = " SET ";
            String whereClause = " WHERE UPPER(PO_num) = '" + POnum.toUpperCase() + "' AND line_item_no = " + lineItemNo;
            if (POnum == null || POnum.isEmpty() || lineItemNo < 0) {
                throw new AstuteException(0, "PO Number should not be null and line item number must be greater than 0.");
            } else {
                whereClause = " WHERE UPPER(PO_num) = '" + POnum.toUpperCase() + "' AND line_item_no = " + lineItemNo;
            }
            if (lineItemNo < 0) {
                throw new AstuteException(0, "Line item Number must be greater than 0.");
            }

            List<PODetail> lineItem = getPODetail(POnum, lineItemNo);
            if (lineItem.size() == 0) {
                // new line item
                createPODetail(POnum, lineItemNo, serviceDesc, feeTypeId, qty, fee, serviceTypeId, qty);
            } else {
                updateClause += " service_desc = '" + serviceDesc + "',";
                updateClause += " fee_type_id = " + feeTypeId + ",";
                updateClause += " qty = " + qty + ",";
                updateClause += " fee = " + fee + ",";
                updateClause += " service_type_id = " + serviceTypeId;

                if (!updateClause.equalsIgnoreCase(" SET ")) {
                    sql = sql + trimLastCharacter(updateClause, ",") + whereClause;

                } else {
                    throw new AstuteException(0, "No values to update.");
                }
                System.out.println(sql);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                updateRemainingQty(POnum,null, lineItemNo);
            }
            updateContractAmount(POnum);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public boolean isPOFinalized(String POnum) throws AstuteException {
        boolean flag = false;
        int finalIndicator = 0;
        String sql = "SELECT final from PO ";
        try {
            Statement stmt = conn.createStatement();
            if (POnum == null || POnum.isEmpty()) {
                throw new AstuteException(0, "PO Number should not be null.");
            }
            sql+=  "WHERE UPPER(PO_num) = '" + POnum.toUpperCase() + "'";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                finalIndicator = rs.getInt(1);
            }
            if (finalIndicator > 0) {
                flag = true;
            }
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void deletePODetail(String POnum, int lineItemNo) throws AstuteException {
        try {
            if (isPOFinalized(POnum)) {
                throw new AstuteException(0, "PO " + POnum + " is already finalyzed and can not be changed.");
            }
            if (POnum == null || POnum.isEmpty() || lineItemNo <= 0) {
                throw new AstuteException(0, "PO Number should not be null and line item number must be greater than 0.");
            }
            String sql = "DELETE FROM PO_DETAIL ";
            sql += " WHERE UPPER(PO_num) = '" + POnum.toUpperCase() + "' AND line_item_no = " + lineItemNo;

            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//            updateRemainingQty(POnum,null, lineItemNo);
            updateContractAmount(POnum);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void createPOMaster(String PONum, String contractNum, String PODate, Double contractAmt, String customerId, String astuteProjectNumber, String title, String notes) throws AstuteException, ParseException {
        try {
            java.util.Date date = null;
            java.sql.Date poDate = null;
            if (PODate!= null && !PODate.isEmpty()) {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(PODate);
                poDate = new java.sql.Date(date.getTime());
            }
            CallableStatement stmt = conn.prepareCall("{call create_PO(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, PONum);
            stmt.setString(2, contractNum);
            stmt.setDate(3, poDate);
            stmt.setDouble(4, contractAmt);
            stmt.setString(5, customerId);
            stmt.setString(6, astuteProjectNumber);
            stmt.setString(7, title);
            stmt.setString(8, notes);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void createPODetail(String POnum, int lineItemNo, String serviceDesc, int feeTypeId, Double qty, Double fee, int serviceTypeId, Double remainingQuantity) throws AstuteException {
        try {
            // TODO remainingQuantity not used, need to take it out from signature
            System.out.println("Calling create_po_detail Procedure");
            System.out.println("POnum is " + POnum);
            CallableStatement stmt = conn.prepareCall("{call create_po_detail(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, POnum);
            stmt.setInt(2, lineItemNo);
            stmt.setString(3, serviceDesc);
            stmt.setInt(4, feeTypeId);
            stmt.setDouble(5, qty);
            stmt.setDouble(6, fee);
            stmt.setInt(7, serviceTypeId);
            stmt.setDouble(8, qty);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
        updateContractAmount(POnum);
    }

    public void finalizePO(String PONum) throws AstuteException {
        try {
            String sql = "UPDATE PO SET final = 1 WHERE PO_Num = '" + PONum + "'";
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void deletePO(String PONum) throws AstuteException {
        try {
            String sql = "DELETE FROM PO WHERE PO_Num = '" + PONum + "' AND final = 0";
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<ServiceType> getServiceTypes() throws AstuteException {
        try {
            List<ServiceType> serviceTypes = new ArrayList<ServiceType>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT SERVICE_TYPE_ID, SERVICE_TYPE.DESC FROM SERVICE_TYPE ORDER BY 1 ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int serviceTypeId = rs.getInt(1);
                String desc = rs.getString(2);
                ServiceType serviceType = new ServiceType(serviceTypeId, desc);
                serviceTypes.add(serviceType);
            }
            return serviceTypes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<FeeType> getFeeTypes() throws AstuteException {
        try {
            List<FeeType> feeTypes = new ArrayList<FeeType>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT FEE_TYPE_ID, FEE_TYPE_DESC FROM FEE_TYPE ORDER BY 1 ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int feeTypeId = rs.getInt(1);
                String desc = rs.getString(2);
                FeeType feeType = new FeeType(feeTypeId, desc);
                feeTypes.add(feeType);
            }
            return feeTypes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }

    }
    public void createServiceType(String desc) throws AstuteException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO service_type (service_type_id, `desc`) VALUES ((SELECT MAX(service_type_id)+1 FROM SERVICE_TYPE B), '" + desc + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }
    public void deleteServiceType(int serviceTypeId) throws AstuteException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "delete from service_type where service_type_id = " + serviceTypeId;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }

    };

    public void updateServiceType(int serviceTypeId, String desc) throws AstuteException {
        try {
        Statement stmt = conn.createStatement();
        String sql = "UPDATE service_type SET `desc` = '" + desc + "' WHERE service_type_id = " + serviceTypeId;
        stmt.executeUpdate(sql);
    } catch (SQLException e) {
        e.printStackTrace();
        throw new AstuteException(DB_ERROR,e.getMessage());
    }
}
    /*
    =============================== Invoice Methods ===============================================
    */

    public Double getPreviouslyBilledAmount(String poNum, String invoiceNumber) throws AstuteException {
        try {
            // TODO remainingQuantity not used, need to take it out from signature
            System.out.println("Calling create_po_detail Procedure");
            double billAmt=0.0;
            CallableStatement stmt = conn.prepareCall("{? = call get_previously_billed_amt(?,?)}");
            stmt.registerOutParameter(1,Types.DOUBLE);
            stmt.setString(2, poNum);
            stmt.setString(3, invoiceNumber);
            stmt.execute();
            billAmt = stmt.getDouble(1);
            return billAmt;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public String generateInvoiceNumber(String poNum) throws AstuteException {
        try {
            String generatedInvoiceNumber;
            System.out.println("Calling generate_inv_number DB function");
            System.out.println("poNum is " + poNum);
            CallableStatement stmt = conn.prepareCall("{? = call generate_inv_number(?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);

            stmt.setString(2, poNum);
            stmt.execute();
            generatedInvoiceNumber = stmt.getString(1);
            return generatedInvoiceNumber;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<PaymentStatus> getPaymentStatuses() throws AstuteException {
        try {
            List<PaymentStatus> paymentStatuses = new ArrayList<PaymentStatus>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT payment_status_id, payment_status_desc FROM payment_status ORDER BY 1 ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int paymentStatusId = rs.getInt(1);
                String desc = rs.getString(2);
                PaymentStatus paymentStatus = new PaymentStatus(paymentStatusId, desc);
                paymentStatuses.add(paymentStatus);
            }
            return paymentStatuses;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public double getOutstandingBalance(String InvoiceNumber) throws AstuteException {
        double balance;
        try {
            System.out.println("Calling get_outstanding_inv_balance DB function");
            CallableStatement stmt = conn.prepareCall("{? = call get_outstanding_inv_balance(?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, InvoiceNumber);
            stmt.executeUpdate();
            balance = stmt.getDouble(1);
            return balance;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    };

    public GeneratedInvoice getGeneratedInvoice(String invoiceNumber) throws AstuteException{
        GeneratedInvoice generatedInvoice = null;
        Invoice invoice = getInvoiceMaster(invoiceNumber, 0).get(0);
        String PONo = invoice.getPoNum();
        List<InvoiceDetail> invoiceDetail = getInvoiceDetail(invoiceNumber, 0);
        PO po = getPOMaster(PONo, null, null, null).get(0);
        String customerId = po.getCustomerId();
        Customer customer = getCustomers(customerId).get(0);

        Double previouslyBilledAmt = getPreviouslyBilledAmount(PONo,invoiceNumber);

        if (invoice.getInvoiceStatus()==2) {
            // TODO subtract current invoice's bill amount from previouslyBilledAmt
            // if it is a submitted invoice
        }
        Double toBeBilledAmt = po.getContractAmt() - previouslyBilledAmt - invoice.getBillAmt();
        generatedInvoice = new GeneratedInvoice(invoice, invoiceDetail, po, customer, previouslyBilledAmt, toBeBilledAmt );
        return generatedInvoice;
    }


    public List<Invoice> getInvoiceMaster(String invoiceNumber, int pmtStatus)throws AstuteException {
        try {
            List<Invoice> invoices = new ArrayList<>();
            Statement stmt = conn.createStatement();
            String whereClause = " WHERE ";
            boolean whereClauseIndicator = false;

            String sql = "SELECT inv_no, inv_date, PO_num, pmt_status, bill_amt, special_notes, certification, inv_status, get_payment_status_name(pmt_status), get_customer_from_po(PO_num), ifnull(get_outstanding_inv_balance(inv_no), bill_amt) FROM INVOICE ";
            if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
                whereClause = whereClause + " UPPER(inv_no) = '"+ invoiceNumber.toUpperCase() +"'";
                whereClauseIndicator = true;
            }
            if (pmtStatus > 0) {
                if (whereClauseIndicator) {
                    whereClause += " AND ";
                }
                whereClause = whereClause + " pmt_status = "+ pmtStatus;
            }
            if (!whereClause.equals(" WHERE ")) {
                sql += whereClause;
            }
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String invNo = rs.getString(1);
                String invdDate = rs.getString(2);
                String PONo = rs.getString(3);
                int paymentStatus = rs.getInt(4);
                Double billAmt = rs.getDouble(5);
                String specialNotes = rs.getString(6);
                String certification = rs.getString(7);
                int invoiceStatus = rs.getInt(8);
                String paymentStatusDesc = rs.getString(9);
                String customerId = rs.getString(10);
                double outstandingBalance = rs.getDouble(11);
                Invoice invoice = new Invoice(invNo, invdDate, PONo, paymentStatus, billAmt, specialNotes, certification, invoiceStatus, paymentStatusDesc, customerId, outstandingBalance);
                invoices.add(invoice);
            }
            return invoices;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<InvoiceDetail> getInvoiceDetail(String invoiceNum, int lineItemNo)throws AstuteException {
        try {
            List<InvoiceDetail> services = new ArrayList<>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT inv_num, line_item_num, PO_line_item_num, service_type_id, description, fee_type_id, fee, qty, get_draft_remaining_qty_fun('"+invoiceNum+"',line_item_num) as draftRemainingQty FROM INVOICE_DETAIL ";
            System.out.println(sql);
            String whereClause = " WHERE ";
            boolean whereClauseIndicator = false;
            if (invoiceNum != null) {
                whereClause = whereClause + " UPPER(inv_num) = '"+ invoiceNum.toUpperCase() +"'";
                whereClauseIndicator = true;
            }
            if (lineItemNo > 0) {
                if (whereClauseIndicator) {
                    whereClause += " AND ";
                }
                whereClause = whereClause + " line_item_num = "+ lineItemNo;
            }

            sql += whereClause;
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String invNo = rs.getString(1);
                int lineItemNum = rs.getInt(2);
                int POLineItemNum = rs.getInt(3);
                int serviceTypeId = rs.getInt(4);
                String desc = rs.getString(5);
                int feeTypeId = rs.getInt(6);
                Double fee = rs.getDouble(7);
                Double qty = rs.getDouble(8);
                Double draftRemainingQty = rs.getDouble(9);
                InvoiceDetail service = new InvoiceDetail(invNo, lineItemNum, POLineItemNum, serviceTypeId, desc, qty, fee, feeTypeId,draftRemainingQty);
                services.add(service);
            }
            return services;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }


    public void updateInvoiceMaster(String invoiceNum, String invoiceDate, String PONum, int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus)throws AstuteException {
        try {
            String sql = "UPDATE INVOICE ";
            String updateClause = " SET ";
            String whereClause = "";
            if (invoiceNum == null || invoiceNum.isEmpty()) {
                throw new AstuteException(0, "Invoice Number can't be null.");
            } else {
                whereClause = " WHERE UPPER(inv_no) ='" + invoiceNum.toUpperCase() + "'";
            }
            updateClause = updateClause + " inv_date = STR_TO_DATE('" + invoiceDate + "', '%Y-%m-%d')" + ",";
            updateClause = updateClause + " PO_num = '" + PONum + "',";
            updateClause = updateClause + " bill_amt = " + billAmt + ",";
            updateClause = updateClause + " special_notes = '" + specialNotes + "',";
            updateClause = updateClause + " certification = '" + certification + "',";
            updateClause = updateClause + " inv_status = " + invoiceStatus;
            sql = sql + updateClause + whereClause;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void deleteInvoice(String invoiceNum) throws AstuteException {
        String result = "";
        try {
            System.out.println("Calling delete_invoice DB function");
            CallableStatement stmt = conn.prepareCall("{? = call delete_invoice(?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, invoiceNum);
            stmt.executeUpdate();
            result = stmt.getString(1);
            System.out.println(result);
            if (!result.equals("SUCCESS")) {
                throw new AstuteException(DB_ERROR,result);
            }
            updateAllRemainingQuantities(invoiceNum);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void updateAllRemainingQuantities(String invNo) throws AstuteException {
        try {
            System.out.println("Calling update_all_remaining_quantities DB procedure");
            CallableStatement stmt = conn.prepareCall("{call update_all_remaining_quantities(?)}");
            stmt.setString(1, invNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public Double getRemainingQty(String poNum, String invoiceNum, int lineItemNo) throws AstuteException {
        Double remainingQty=0.0;
        String sql;
        try {
            Statement stmt = conn.createStatement();
            if (!poNum.isEmpty()) {
                sql = "SELECT remaining_qty FROM PO_DETAIL WHERE po_Num = '" + poNum + "' AND line_item_no = " + lineItemNo;
            } else {
                sql = " SELECT remaining_qty FROM PO_DETAIL, INVOICE " +
                " WHERE invoice.PO_num = po_detail.PO_num " +
                " AND line_item_no = " + lineItemNo +
                " AND invoice.inv_no = '" + invoiceNum + "'";
            }
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                remainingQty = rs.getDouble(1);
            }
            return remainingQty;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public Double updateRemainingQty(String poNum, String invNo, int lineItemNo) throws AstuteException {
        Double remainingQty;
        try {
            System.out.println("Calling update_remaining_qty DB procedure");
            System.out.println("poNum is " + poNum);
            CallableStatement stmt = conn.prepareCall("{? = call update_remaining_qty_fun(?,?,?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, poNum);
            stmt.setString(3, invNo);
            stmt.setInt(4, lineItemNo);
            stmt.executeUpdate();
            remainingQty = stmt.getDouble(1);
            System.out.println("remaining qty is updated to " + remainingQty);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
        return remainingQty;
    }

    public void updateInvoiceDetail(String invoiceNum, int lineItemNum, int POLineItemNum, int serviceTypeId, String desc, double qty, double fee, int feeTypeId)throws AstuteException {
        try {
//            if (qty > getRemainingQty( "",invoiceNum, POLineItemNum)) {
//                throw new AstuteException(DB_ERROR,"Qty can not exceed Remaining Qty");
//            }
            String sql = "UPDATE INVOICE_DETAIL ";
            String updateClause = " SET ";
            String whereClause = "";
            if(invoiceNum == null || invoiceNum.isEmpty() || lineItemNum < 0) {
                throw new AstuteException(0, "Invoice Number should not be null and line item number must be greater than 0.");
            } else {
                whereClause = " WHERE UPPER(inv_num) = '" + invoiceNum.toUpperCase() + "' AND line_item_num = " + lineItemNum;
            }
            updateClause += " service_type_id = " + serviceTypeId + ",";
            updateClause += " description = '" + desc + "',";
            updateClause += " qty = " + qty + ",";
            updateClause += " fee = " + fee + ",";
            updateClause += " fee_type_id = " + feeTypeId + ",";
            if (!updateClause.equalsIgnoreCase(" SET ")) {
                sql = sql + trimLastCharacter(updateClause,",") + whereClause;

            } else {
                throw new AstuteException(0, "No values to update.");
            }
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//            updateRemainingQty("",invoiceNum, POLineItemNum);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void deleteInvoiceDetail(String invoiceNum, int lineItemNum, int poLineItemNum)throws AstuteException {
        try {
            String sql = "DELETE FROM INVOICE_DETAIL ";

            String whereClause = "";
            if(invoiceNum == null || invoiceNum.isEmpty() || lineItemNum < 0) {
                throw new AstuteException(0, "Invoice Number should not be null and line item number must be greater than 0.");
            } else {
                whereClause = " WHERE UPPER(inv_num) = '" + invoiceNum.toUpperCase() + "' AND line_item_num = " + lineItemNum;
            }
            sql += whereClause;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//            updateAllRemainingQuantities(invoiceNum);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void createInvoiceMaster(String invoiceNum, String invoiceDate, String PONum, int pmtStatus, Double billAmt, String specialNotes, String certification, int invoiceStatus) throws AstuteException, ParseException {
        try {
            java.util.Date date = null;
            java.sql.Date invDate = null;
            if (invoiceDate != null) {
                date= new SimpleDateFormat("yyyy-MM-dd").parse(invoiceDate);
                invDate = new java.sql.Date(date.getTime());
            }
            System.out.println("Calling create_invoice Procedure");
            System.out.println("invoiceNum is "+invoiceNum);
            CallableStatement stmt = conn.prepareCall("{call create_invoice(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, invoiceNum);
            stmt.setDate(2, invDate);
            stmt.setString(3, PONum);
            stmt.setInt(4, pmtStatus);
            stmt.setDouble(5, billAmt);
            stmt.setString(6, specialNotes);
            stmt.setString(7, certification);
            stmt.setInt(8, invoiceStatus);
            stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new AstuteException(DB_ERROR,e.getMessage());
    }
    }


    public void createInvoiceDetail(String invoiceNum, int lineItemNum, int POLineItemNum, int serviceTypeId, String desc, double qty, double fee, int feeTypeId)throws AstuteException {
        try {
            System.out.println("Calling create_invoice_detail Procedure");
            System.out.println("invoiceNum is "+invoiceNum);
            CallableStatement stmt = conn.prepareCall("{call create_invoice_detail(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, invoiceNum);
            stmt.setInt(2, lineItemNum);
            stmt.setInt(3, POLineItemNum);
            stmt.setInt(4, serviceTypeId);
            stmt.setString(5, desc);
            stmt.setDouble(6, qty);
            stmt.setDouble(7, fee);
            stmt.setInt(8, feeTypeId);
            stmt.executeUpdate();
            System.out.println("invoiceNum is " + invoiceNum);
            System.out.println("POLineItemNum is " + POLineItemNum);
//            updateRemainingQty(null,invoiceNum, POLineItemNum);
    } catch (SQLException e) {
        e.printStackTrace();
        throw new AstuteException(DB_ERROR,e.getMessage());
    }
   }

    public void submitInvoice(String InvoiceNumber) throws AstuteException {
        try {
            System.out.println("Calling submit_invoice_fun Procedure ");
            System.out.println("invoiceNum is "+InvoiceNumber);
            CallableStatement stmt = conn.prepareCall("{? = call submit_invoice_fun(?)}");
            stmt.registerOutParameter(1,Types.VARCHAR);
            stmt.setString(2, InvoiceNumber);
            stmt.execute();
            String newInvoiceNumber = stmt.getString(1);
            updateAllRemainingQuantities(newInvoiceNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
        }

    public void voidInvoice(String InvoiceNumber) throws AstuteException {
        try {
            if (InvoiceNumber == null || InvoiceNumber.isEmpty()) {
                throw new AstuteException(0, "Invoice Number should not be null!");
            } else {
                String sql = "UPDATE INVOICE SET INV_STATUS = 3 WHERE UPPER(INV_NO) =  UPPER('" + InvoiceNumber + "') AND PMT_STATUS = 1";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("Updating all remaining quantities");
                updateAllRemainingQuantities(InvoiceNumber);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new AstuteException(DB_ERROR, e.getMessage());
        }
    }

    public String dupliateInvoice(String InvoiceNumber) throws AstuteException {
        try {
            String generatedInvoiceNumber;
            CallableStatement stmt = conn.prepareCall("{? = call duplicate_invoice(?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, InvoiceNumber);
            stmt.execute();
            generatedInvoiceNumber = stmt.getString(1);
            return generatedInvoiceNumber;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<Invoice> getSubmittedInvoiceNumbers() throws AstuteException {
        String sql = "select inv_no from invoice where inv_status = 2";
        String invoiceNumber = null;
        Invoice invoice = null;
        List<Invoice> invoices = new ArrayList<Invoice>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                invoiceNumber = resultSet.getString(1);
                invoice = new Invoice(invoiceNumber,null,null,1,null,null,null,0, "Outstanding","", 0);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
        System.out.println("Returning invoices " + invoices.size());
        return invoices;
    }
   /*
=============================== Customer Methods ===============================================
 */

    public List<Customer> getCustomers(String customerId) throws AstuteException {
        try {
        List<Customer> customers = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT customer_id, customer_name, bill_to_dept, add1, add2, city, state ,zip, zip_last_4, email, phone, phext, fax FROM customer ";
        if (customerId!=null && !customerId.isEmpty()) {
            sql += " WHERE customer_id = '" + customerId + "'";
        }
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String customerID = rs.getString(1);
            String customerName = rs.getString(2);
            String billToDept = rs.getString(3);
            String add1 = rs.getString(4);
            String add2 = rs.getString(5);
            String city = rs.getString(6);
            String state = rs.getString(7);
            int zip = rs.getInt(8);
            int ziplast4 = rs.getInt(9);
            String email = rs.getString(10);
            String phone = rs.getString(11);
            int phExt = rs.getInt(12);
            String fax = rs.getString(13);
            Customer customer = new Customer(customerID, customerName,billToDept, add1, add2, city, state, zip, ziplast4, email, phone, phExt, fax);
            customers.add(customer);
        }
        return customers;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public String getCustomerId(String poNumber) throws AstuteException {
        String sql = "select po.customer_id from po " +
                "where po.po_num = '" + poNumber + "'";
        String custId = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            if(resultSet.next()) {
                custId = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
        return custId;
    }
    public Customer getCustomer(String poNumber) throws AstuteException {
            String customerId = getCustomerId(poNumber);
            List<Customer> customers = getCustomers(customerId);
            return customers.get(0);
    }


    public String createCustomer(String customerId, String customerName, String billToDept, String add1, String add2, String city, String state, int zip, int ziplast4, String email, String phone, int phExt, String fax) throws AstuteException {
        try {
            CallableStatement stmt = conn.prepareCall("{? = call create_customer_fun(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, customerId);
            stmt.setString(3, customerName);
            stmt.setString(4, billToDept);
            stmt.setString(5, add1);
            stmt.setString(6, add2);
            stmt.setString(7, city);
            stmt.setString(8, state);
            stmt.setInt(9, zip);
            stmt.setInt(10, ziplast4);
            stmt.setString(11, email);
            stmt.setString(12, phone);
            stmt.setInt(13, phExt);
            stmt.setString(14, fax);
            stmt.executeUpdate();
            String customerIdOut = stmt.getString(1);
            return customerIdOut;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }
        public void updateCustomer( String customerId, String customerName, String billToDept, String add1, String
        add2, String city, String state, int zip, int ziplast4, String email,String phone, int phExt, String fax) throws
        AstuteException {
            try {
                String sql = "UPDATE CUSTOMER ";
                String updateClause = " SET ";
                String whereClause = "";
                if (customerId==null || customerId.isEmpty()) {
                    throw new AstuteException(0, "CustomerId can't be null.");
                } else {
                    whereClause = " WHERE customer_id ='" + customerId + "'";
                }

                updateClause = updateClause + " customer_name = '" + customerName + "',";
                updateClause = updateClause + " bill_to_dept = '" + billToDept + "',";
                updateClause = updateClause + " add1 = '" + add1 + "',";
                updateClause = updateClause + " add2 = '" + add2 + "',";
                updateClause = updateClause + " city = '" + city + "',";
                updateClause = updateClause + " state = '" + state + "',";
                updateClause = updateClause + " zip = " + zip + ",";
                updateClause = updateClause + " zip_last_4 = " + ziplast4 + ",";
                updateClause = updateClause + " email = '" + email + "',";
                updateClause = updateClause + " phone = '" + phone + "',";
                updateClause = updateClause + " phext = " + phExt + ",";
                updateClause = updateClause + " fax = '" + fax + "',";
                if (!updateClause.equalsIgnoreCase(" SET ")) {
                    sql = sql + trimLastCharacter(updateClause, ",") + whereClause;

                } else {
                    System.out.println(updateClause);
                    throw new AstuteException(0, "No values to update.");
                }
                System.out.println(sql);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AstuteException(DB_ERROR,e.getMessage());
            }
        }

    public void deleteCustomer(String customerId) throws AstuteException {
        String result = "";
        try {
            System.out.println("Calling delete_customer DB function");
            CallableStatement stmt = conn.prepareCall("{? = call delete_customer(?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, customerId);
            stmt.executeUpdate();
            result = stmt.getString(1);
            System.out.println(result);
            if (!result.equals("SUCCESS")) {
                throw new AstuteException(DB_ERROR, result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR, e.getMessage());
        }
    }
/*=============================== Customer Contacts Methods ===============================================
*/

    public List<CustomerContact> getCustomerContacts(String customerId) throws AstuteException {
        try {
            List<CustomerContact> contacts = new ArrayList<>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT customer_id, contact_id, `name`, title, work_phone, work_phone_ext, mobile, fax, email, address FROM customer_contact";
            if (customerId!=null && !customerId.isEmpty()) {
                sql += " WHERE customer_id = '" + customerId + "'";
            }
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String customerID = rs.getString(1);
                int contactId = rs.getInt(2);
                String name = rs.getString(3);
                String title = rs.getString(4);
                String workPhone = rs.getString(5);
                int phoneExt = rs.getInt(6);
                String mobile = rs.getString(7);
                String fax = rs.getString(8);
                String email = rs.getString(9);
                String address = rs.getString(10);
                CustomerContact contact = new CustomerContact(customerID, contactId, name, title, workPhone, phoneExt, mobile, fax, email, address);
                contacts.add(contact);
            }
            return contacts;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public String createCustomerContact(String customerId, String name,String title, String workPhone,int phExt,String mobile, String fax, String email, String address) throws AstuteException {
        try {
            CallableStatement stmt = conn.prepareCall("{? = call create_customer_contact_fun(?,?,?,?,?,?,?,?,?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, customerId);
            stmt.setString(3, name);
            stmt.setString(4, title);
            stmt.setString(5, workPhone);
            stmt.setInt(6, phExt);
            stmt.setString(7, mobile);
            stmt.setString(8, fax);
            stmt.setString(9, email);
            stmt.setString(10, address);
            stmt.executeUpdate();
            String customerIdOut = stmt.getString(1);
            return customerIdOut;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }
    public void updateCustomerContact(String customerId, int contactId, String name,String title,String workPhone,int phExt,String mobile, String fax, String email, String address) throws
            AstuteException {
        try {
            String sql = "UPDATE CUSTOMER_CONTACT ";
            String updateClause = " SET ";
            String whereClause = "";
            if (customerId==null || customerId.isEmpty() || contactId <=0 ) {
                throw new AstuteException(0, "CustomerId or contactId can't be null.");
            } else {
                whereClause = " WHERE customer_id ='" + customerId + "' and contact_id = " + contactId;
            }

            updateClause = updateClause + " name = '" + name + "',";
            updateClause = updateClause + " title = '" + title + "',";
            updateClause = updateClause + " work_phone = '" + workPhone + "',";
            updateClause = updateClause + " work_phone_ext = " + phExt + ",";
            updateClause = updateClause + " mobile = '" + mobile + "',";
            updateClause = updateClause + " fax = '" + fax + "',";
            updateClause = updateClause + " address = '" + address + "',";
            updateClause = updateClause + " email = '" + email + "',";
            if (!updateClause.equalsIgnoreCase(" SET ")) {
                sql = sql + trimLastCharacter(updateClause, ",") + whereClause;

            } else {
                System.out.println(updateClause);
                throw new AstuteException(0, "No values to update.");
            }
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public void deleteCustomerContact(String customerId, int contactId) throws AstuteException {
        String result = "";
        try {
            System.out.println("Calling delete_customer_contact DB function");
            CallableStatement stmt = conn.prepareCall("{? = call delete_customer_contact(?,?)}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, customerId);
            stmt.setInt(3, contactId);
            stmt.executeUpdate();
            result = stmt.getString(1);
            System.out.println(result);
            if (!result.equals("Success")) {
                throw new AstuteException(DB_ERROR, result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR, e.getMessage());
        }
    }
/*
=============================== User/Session Methods ===============================================
 */

        // user and session methods
        public ResultSet executeQuery(String sessionId, String sql) throws AstuteException {
            String username = getSessionUsername(sessionId);

            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                return stmt.executeQuery("/*user=" + username + "*/ " + sql);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AstuteException(DB_ERROR,e.getMessage());
            }
        }

    public Integer authenticateSession(String sessionId) throws AstuteException {
        String sql = "select user_id, TIME_TO_SEC(CURRENT_TIMESTAMP()) - TIME_TO_SEC(session_end_date) from session where session_id='" + sessionId + "'";
        System.out.println(sql);
        int userId;

        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            Integer timeLapse;
            java.util.Date utilDate = new java.util.Date();
            java.sql.Timestamp currentTime = new java.sql.Timestamp(utilDate.getTime());
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
                timeLapse = resultSet.getInt(2);
                if (timeLapse > 1200) {
                    logout(sessionId);
                    throw new AstuteException(AUTH_ERROR, "Session expred. Please login again!");
                }
            } else {
                return null;
            }
            sql = "update session set session_end_date = current_timestamp() where session_id='" + sessionId + "'";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            return userId;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR, e.getMessage());
        }
    }


        public User getUser(String username) throws AstuteException {
            String sql = "select user_id, DECODE(username,'"+key+"'), DECODE(password,'"+key+"'), CONCAT(first_name, ' ', last_name) as name from user where username=ENCODE('" + username + "','"+key+"')";
            try {
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(sql);

                User user = null;
                if(resultSet.next()) {
                    user = new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4), null
                    );
                }
                return user;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AstuteException(DB_ERROR,e.getMessage());
            }
        }

    public void createSession(int userId, String sessionId) throws AstuteException {
    try {
        String sql = "insert into session (user_id, session_id) values (" + userId + ", '" + sessionId + "')";

        Statement stmt = conn.createStatement();
        stmt.execute(sql);

    } catch (SQLException e) {
        e.printStackTrace();
        throw new AstuteException(DB_ERROR,e.getMessage());
    }
    }

    public void deleteSession(String sessionId) {
    }

    public String getSessionUsername(String sessionId) throws AstuteException {
        try {
            String sql = "select username from users join sessions on users.user_id = sessions.user_id where sessions.session_id = '" + sessionId + "'";

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            resultSet.next();
            String sessionUserName = resultSet.getString(1);

            conn.close();

            return sessionUserName;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    /*
    =============================== Change Order ===============================================
    */


    public List<ChangeOrder> getChangeOrders(String poNum) throws AstuteException {
        try {
            List<ChangeOrder> changeOrders = new ArrayList<ChangeOrder>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT PO_num, change_order_num, change_order_date, change_order_amt, description FROM change_order; ";
            if (poNum != null) {
                sql += " WHERE po_num = '" + poNum + "'";
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String PONum = rs.getString(1);
                int changeOrderNum = rs.getInt(2);
                Date changeOrderDate = rs.getDate(3);
                Double changeOrderAmt = rs.getDouble(4);
                String description = rs.getString(5);
                ChangeOrder changeOrder = new ChangeOrder(changeOrderNum, PONum, changeOrderDate, changeOrderAmt, description);
                changeOrders.add(changeOrder);
            }
            return changeOrders;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    };

    public void updateChangeOrder(String poNum, int changeOrderNum, double changeOrderAmt, Date changeOrderDate, String description) throws AstuteException {
        try {
            String sql = "UPDATE CHANGE_ORDER ";
            String updateClause = " SET ";
            String whereClause = "";
            if (poNum == null || poNum.isEmpty() || changeOrderNum <=0 ) {
                throw new AstuteException(DB_ERROR,"PO Number can't be null and Change Order Number should be a positive number! ");
            } else {
                whereClause = " WHERE UPPER(PO_num) ='" + poNum.toUpperCase() + "' and change_order_num = " + changeOrderNum;
            }

            updateClause = updateClause + " changeOrderAmt = " + changeOrderAmt ;
            if (changeOrderDate != null) {
                updateClause = updateClause + ", changeOrderDate = STR_TO_DATE(" + changeOrderDate + ", '%Y-%m-%d')" + ",";
            }
            if (description != null) {
                updateClause = updateClause + ", description = '" + description + "'";
            }
            sql = sql + updateClause + whereClause;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }

    }

    public int createChangeOrder(String poNum, int changeOrderNum, double changeOrderAmt, Date changeOrderDate, String description) throws AstuteException{
        try {
            String dateString = "STR_TO_DATE(" + changeOrderDate + ", '%Y-%m-%d')";
            String sql = "insert into change_order (po_num, change_order_num, change_order_amt, change_order_date, description) values ('" + poNum + "', " + changeOrderNum + ", " + changeOrderAmt + ", " + dateString + ", '" + description + "')";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            return changeOrderNum;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    /*
    =============================== Invoice Payment ===============================================
    */


    public List<InvoicePayment> getInvoicePayments(String invoiceNum) throws AstuteException {
        try {
            List<InvoicePayment> invoicePayments = new ArrayList<InvoicePayment>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT invoice_payment_id, invoice_payment_type, get_payment_type(invoice_payment_type), invoice_amount, payment_date, inv_no, check_no, transaction_no FROM invoice_payment ";
            if (invoiceNum != null) {
                sql += " WHERE inv_no = '" + invoiceNum + "'";
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int invoicePaymentId = rs.getInt(1);
                int invoicePaymentTypeId = rs.getInt(2);
                String invoicePaymentType = rs.getString(3);
                Double paymentAmount = rs.getDouble(4);
                Date paymentDate = rs.getDate(5);
                String invNo = rs.getString(6);
                String checkNo = rs.getString(7);
                String transactionNo = rs.getString(8);
                InvoicePayment invoicePayment = new InvoicePayment(invNo, invoicePaymentId, invoicePaymentTypeId, invoicePaymentType, paymentDate, paymentAmount, checkNo, transactionNo);
                invoicePayments.add(invoicePayment);
            }
            return invoicePayments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    };

    public void updateInvoicePayment(String invoiceNum, int invoicePaymentId, int invoicePaymentTypeId, Double paymentAmount, Date paymentDate, String checkNo, String transactionNo) throws AstuteException {
        try {
            if (paymentAmount > getOutstandingBalance(invoiceNum) ) {
                throw new AstuteException(DB_ERROR,"Payment amount can't be greater than outstanding balance for Invoice " + invoiceNum);
            }
            String sql = "UPDATE INVOICE_PAYMENT ";
            String updateClause = " SET ";
            String whereClause = "";
            if (invoiceNum == null || invoiceNum.isEmpty() || invoicePaymentId <=0 ) {
                throw new AstuteException(DB_ERROR,"Invoice Number can't be null and Invoice Payment Id should be a positive number! ");
            }


            updateClause = updateClause + " invoice_payment_type = " + invoicePaymentTypeId + ", invoice_amount = " + paymentAmount + ", check_No = '" + checkNo + "', transaction_no = '" + transactionNo + "'";
//            if (paymentDate != null) {
                updateClause = updateClause + ", payment_date = STR_TO_DATE('" + paymentDate + "', '%Y-%m-%d')";
//            }
            whereClause = " WHERE UPPER(inv_no) ='" + invoiceNum.toUpperCase() + "' and invoice_payment_id = " + invoicePaymentId;

            sql = sql + updateClause + whereClause;
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }

    }

    public void createInvoicePayment(String invoiceNum, int invoicePaymentTypeId, Double paymentAmount, Date paymentDate, String checkNo, String transactionNo) throws AstuteException{
        try {
            if (paymentAmount > getOutstandingBalance(invoiceNum) ) {
                throw new AstuteException(DB_ERROR,"Payment amount can't be greater than outstanding balance for Invoice " + invoiceNum);
            }
            if (paymentAmount <= 0)  {
                throw new AstuteException(DB_ERROR,"Payment amount must be greater than 0");
            }
            String dateString = "STR_TO_DATE('" + paymentDate + "', '%Y-%m-%d')";
            String sql = "insert into invoice_payment (inv_no, invoice_payment_type, invoice_amount, payment_date, check_no, transaction_no) values ('" + invoiceNum + "', " + invoicePaymentTypeId + ", " + paymentAmount + ", " + dateString + ", '" + checkNo +"', '" + transactionNo + "')";
            Statement stmt = conn.createStatement();
            System.out.println(sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    public List<PaymentType> getPaymentTypes() throws AstuteException {
        try {
            List<PaymentType> paymentTypes = new ArrayList<PaymentType>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT payment_type_id, payment_type_name FROM payment_type ORDER BY 1 ";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int paymentTypeId = rs.getInt(1);
                String paymentTypeName = rs.getString(2);
                PaymentType paymentType = new PaymentType(paymentTypeId, paymentTypeName);
                paymentTypes.add(paymentType);
            }
            return paymentTypes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }

    };
    /*
    =============================== Utility Methods ===============================================
    */
    public User login(String username, String password) throws AstuteException{

        User user = getUser(username);
        if (user != null && password.equals(user.getPassword())) {
            //create session
            String sessionId = UUID.randomUUID().toString().replaceAll("-", "");

            createSession(user.getUserId(), sessionId);
            user.setSessionId(sessionId);
            return user;
        } else {
            return null; //"Username or password was not correct";
        }
    }

    public void logout(String sessionId) throws AstuteException{
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM SESSION WHERE SESSION_ID = '" + sessionId + "'";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new AstuteException(DB_ERROR,e.getMessage());
        }
    }

    }
