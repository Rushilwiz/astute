-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.12-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping structure for table astute.change_order
-- Dumping database structure for astute
DROP DATABASE IF EXISTS `astute`;
CREATE DATABASE IF NOT EXISTS `astute` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `astute`;
-- CREATE USER 'astute_user'@'localhost' IDENTIFIED BY 'password';

CREATE TABLE IF NOT EXISTS `change_order` (
  `PO_num` varchar(20) NOT NULL,
  `change_order_num` int(20) NOT NULL,
  `change_order_date` date DEFAULT NULL,
  `change_order_amt` double NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`PO_num`,`change_order_num`),
  CONSTRAINT `fk_PO_CO_POnum` FOREIGN KEY (`PO_num`) REFERENCES `po` (`PO_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS astute.update_contract_amt;
CREATE PROCEDURE astute.`update_contract_amt`(PONum varchar(20))
BEGIN
update po set contract_amt = (SELECT sum(qty*fee) from po_detail where PO_num = PONum) where PO_num = PONum;
END;

-- Dumping data for table astute.change_order: ~0 rows (approximately)
/*!40000 ALTER TABLE `change_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `change_order` ENABLE KEYS */;

CREATE FUNCTION astute.`create_customer_contact_fun`(customer_id_in varchar(20), name varchar(100), title varchar(50), work_phone varchar(20), work_phone_ext int, mobile varchar(20), fax varchar(20), email varchar(100), address varchar(500)) RETURNS varchar(20) CHARSET utf8
BEGIN
DECLARE last_inserted_id varchar(20);
DECLARE new_contact_id int;
DECLARE contactCount int;

SELECT COUNT(contact_id) INTO contactCount FROM customer_contact WHERE customer_id = customer_id_in;
if contactCount > 0 then
   SELECT max(contact_id)+1 into new_contact_id FROM customer_contact where customer_id = customer_id_in;
else
   SET new_contact_id = 1;
END IF;
INSERT INTO astute.customer_contact
(customer_id, contact_id, name, title, work_phone, work_phone_ext, mobile, fax, email, address)
VALUES (customer_id_in, new_contact_id, name, title, work_phone, work_phone_ext, mobile, fax, email, address);
SELECT LAST_INSERT_ID() into last_inserted_id;
return last_inserted_id;
END;


-- Dumping structure for function astute.create_customer_fun

CREATE FUNCTION astute.`create_customer_fun`(customerid varchar(20), customerName varchar(100), billToDept varchar(100), add1In varchar(100), add2In varchar(100), cityIn varchar(50),  stateIn varchar(20), zipIn int(5), zipLast4In int(4), emailIn varchar(50), phoneIn varchar(20), extIn int(6), faxIn varchar(20)) RETURNS varchar(20) CHARSET utf8
BEGIN
DECLARE last_inserted_id varchar(20);
INSERT INTO customer (customer_id, customer_name, bill_to_dept, add1, add2, city, state ,zip, zip_last_4, email, phone, phext, fax)
VALUES (customerid, customerName, billToDept, add1In, add2In, cityIn, stateIn, zipIn, ziplast4In, emailIn, phoneIn, extIn, faxIn);
SELECT LAST_INSERT_ID() into last_inserted_id;
return last_inserted_id;
END;


-- Dumping structure for procedure astute.create_invoice

CREATE DEFINER=`root`@`localhost` PROCEDURE `create_invoice`(invNo varchar(20),invDate date,PONo varchar(20),paymentStatus int,billAmt double,specialNotes varchar(500), certClause Varchar(500), invoiceStatus int)
BEGIN
INSERT INTO invoice (inv_no,inv_date,PO_num,pmt_status,bill_amt,special_notes,certification,inv_status)
VALUES (invNo, invDate, PONo, 1, billAmt, specialNotes, certClause, invoiceStatus);
END;


-- Dumping structure for procedure astute.create_invoice_detail

CREATE PROCEDURE astute.`create_invoice_detail`(invoiceNum varchar(20), lineItemNum int, POLineItemNum varchar(20), serviceTypeId int, description varchar(500), qty_in double, fee_in double, fee_type_id_in int)
BEGIN
declare maxlineItemNo int;
DECLARE detailCount int;

SELECT COUNT(line_item_num) INTO detailCount FROM invoice_detail where inv_num = invoiceNum;
if lineItemNum is null or lineItemNum = 0 then
   if detailCount > 0 then
      select max(line_Item_Num)+1 into maxlineItemNo from invoice_detail where inv_num = invoiceNum;
   else
      set maxlineItemNo = 1;
   END IF;
else
   set maxlineItemNo = lineItemNum;
END IF;

INSERT INTO INVOICE_DETAIL (inv_num, line_item_num, PO_line_item_num, service_type_id, description, qty, fee, fee_type_id)
VALUES (invoiceNum, maxlineItemNo, POLineItemNum, serviceTypeId, description, qty_in, fee_in, fee_type_id_in);
END;

-- Dumping structure for procedure astute.create_po

CREATE DEFINER=`root`@`localhost` PROCEDURE `create_po`(PONum varchar(40), contractNum varchar(20), PODate date, contractAmt double(10,2), customerid varchar(20), astute_project_num_in varchar(20), title_in varchar(200), notes_in varchar(200))
BEGIN
DECLARE next_po_id int(11);
SELECT count(*) + 1 INTO next_po_id FROM PO WHERE customer_id = customerid;
INSERT INTO PO (po_id, PO_num, contract_num, PO_date, contract_amt, customer_id,astute_project_num, title, notes )
VALUES (next_po_id, PONum, contractNum, PODate, contractAmt, customerId,astute_project_num_in, title_in, notes_in);
END;


CREATE PROCEDURE astute.`create_po_detail`(PONum varchar(40), lineItemNo int, servicedesc varchar(500), feetypeid int(11), quantity double, fee_in double, servicetypeid int(1), remaining_qty double)
BEGIN
declare maxlineItemNo int;
DECLARE podetailCount int;

SELECT COUNT(line_item_no) INTO podetailCount FROM po_detail where po_num = ponum;

if lineItemNo is null or lineItemNo = 0 then
      if podetailCount > 0 then
         select max(line_item_no)+1 into maxlineItemNo from po_detail where po_num = ponum;
      else
         SET maxlineItemNo = 1;
      END IF;
else
   set maxlineItemNo = lineItemNo;
END IF;
INSERT INTO PO_DETAIL (PO_num,line_item_no,service_desc,fee_type_id,qty,fee,service_type_id, remaining_qty)
VALUES (POnum,maxlineItemNo,servicedesc,feetypeid,quantity,fee_in, servicetypeid, remaining_qty);
END;

-- Dumping structure for table astute.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` varchar(10) NOT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `bill_to_dept` varchar(50) DEFAULT NULL,
  `add1` varchar(50) DEFAULT NULL,
  `add2` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `zip` int(5) DEFAULT NULL,
  `zip_last_4` int(4) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `phext` int(6) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.customer: ~2 rows (approximately)
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`customer_id`, `customer_name`, `bill_to_dept`, `add1`, `add2`, `city`, `state`, `zip`, `zip_last_4`, `email`, `phone`, `fax`, `phext`) VALUES
	('MDOT', 'Maryland Department of Transportation', 'Billing Department', '123123 Test Drive', '104', 'Germantown', 'MD', 20874, 3452, 'Test@MDOT.gov', '(123) 123-1233', '(121) 231-2323', 0),
	('VDOT', 'Virginia Depart of Transportation', 'Billing Department', '13134 Saturn Drive', 'Unit 100', 'McLean', 'VA', 22043, 0, 'Billing@vdot.gov', '(703) 122-1234', '(703) 122-12', 0);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- Dumping structure for table astute.customer_contact
CREATE TABLE `customer_contact` (
  `customer_id` varchar(20) NOT NULL,
  `contact_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `title` varchar(50) NOT NULL,
  `work_phone` varchar(16) DEFAULT NULL,
  `work_phone_ext` int(10) DEFAULT NULL,
  `mobile` varchar(16) DEFAULT NULL,
  `fax` varchar(16) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.customer_contact: ~2 rows (approximately)
/*!40000 ALTER TABLE `customer_contact` DISABLE KEYS */;
INSERT INTO `customer_contact` (`customer_id`, `contact_id`, `name`, `title`, `work_phone`, `work_phone_ext`, `mobile`, `fax`, `email`, `address`) VALUES
	('MDOT', 1, 'John Shaw', 'Manager', '1231231233', 1233, '1232343455', '234123344', 'Test@Test.com', '123 Test Drive'),
	('MDOT', 2, 'John John', 'Manager', '1231231233', 1233, '1232343455', '234123344', 'Test@Test.com', '123 Test Drive');
/*!40000 ALTER TABLE `customer_contact` ENABLE KEYS */;

-- Dumping structure for function astute.delete_customer

CREATE DEFINER=`root`@`localhost` FUNCTION `delete_customer`(customer_id_in varchar(10)) RETURNS varchar(100) CHARSET utf8
BEGIN

   DECLARE po_count int;

   SELECT count(*)
   INTO po_count
   FROM po
   WHERE customer_id = customer_id_in;

   IF po_count > 0 THEN
      RETURN 'ERROR - Customer with Sales orders can not be deleted';
   END IF;

   delete from customer where customer_id = customer_id_in;

   RETURN 'SUCCESS';

END;


-- Dumping structure for function astute.delete_customer_contact

CREATE DEFINER=`root`@`localhost` FUNCTION `delete_customer_contact`(customer_id_in varchar(20), contact_id_in int) RETURNS varchar(20) CHARSET utf8
BEGIN
	delete from customer_contact where customer_id = customer_id_in and contact_id = contact_id_in;
	RETURN 'Success';
END;


-- Dumping structure for function astute.delete_invoice

CREATE DEFINER=`root`@`localhost` FUNCTION `delete_invoice`(inv_no_in varchar(20)) RETURNS varchar(40) CHARSET utf8
BEGIN

   DECLARE inv_status_in int;

   SELECT inv_status
   INTO inv_status_in
   FROM invoice
   WHERE inv_no = inv_no_in;

   IF inv_status_in <> 1 THEN
      RETURN 'ERROR - ONLY DRAFT INVOICE CAN BE DELETED';
   END IF;

   UPDATE INVOICE_DETAIL SET QTY = 0 WHERE inv_num = inv_no_in;
   CALL update_all_remaining_quantities(inv_no_in);
   DELETE FROM INVOICE WHERE inv_no = inv_no_in;

   RETURN 'SUCCESS';

END;


-- Dumping structure for function astute.duplicate_invoice

CREATE DEFINER=`root`@`localhost` FUNCTION `duplicate_invoice`(inv_no_in varchar(20)) RETURNS varchar(20) CHARSET utf8
BEGIN
   DECLARE generated_inv_number varchar(20);
   DECLARE po_num_in varchar(20);

   SELECT po_num
   INTO po_num_in
   FROM invoice
   WHERE inv_no = inv_no_in;

   SELECT generate_inv_number(po_num_in)
   INTO generated_inv_number;

   INSERT INTO invoice
   (inv_no,inv_date,PO_num,change_order_num,bill_amt,special_notes,certification,inv_status,pmt_status)
   (SELECT generated_inv_number, CURDATE(), PO_num, change_order_num, bill_amt, special_notes, certification, inv_status, pmt_status
   FROM invoice
   WHERE inv_no = inv_no_in);

   INSERT INTO invoice_detail
   (inv_num,line_item_num,PO_line_item_num,service_type_id,description,qty,fee,fee_type_id)
   (SELECT generated_inv_number, line_item_num, PO_line_item_num, service_type_id, description, 0, fee, fee_type_id
    FROM invoice_detail
    WHERE inv_num = inv_no_in);

   return generated_inv_number;

END;


-- Dumping structure for table astute.fee_type
CREATE TABLE IF NOT EXISTS `fee_type` (
  `fee_type_id` int(11) NOT NULL,
  `fee_type_desc` varchar(40) NOT NULL,
  PRIMARY KEY (`fee_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.fee_type: ~2 rows (approximately)
/*!40000 ALTER TABLE `fee_type` DISABLE KEYS */;
INSERT INTO `fee_type` (`fee_type_id`, `fee_type_desc`) VALUES
	(1, 'Fixed fee'),
	(2, 'Hourly');
/*!40000 ALTER TABLE `fee_type` ENABLE KEYS */;

-- Dumping structure for function astute.generate_final_inv_number

CREATE FUNCTION astute.`generate_final_inv_number`(po_num_in varchar(20)) RETURNS varchar(20) CHARSET utf8
BEGIN

   DECLARE _astute_project_num varchar(4);
   DECLARE _customer_id varchar(10);
   DECLARE inv_serial_no varchar(40);
   DECLARE inv_number varchar(20);

   SELECT substr(astute_project_num,1,4)
   INTO _astute_project_num
   FROM po
   WHERE po.po_num = po_num_in;

   SELECT customer_id
   INTO _customer_id
   FROM po
   WHERE po.po_num = po_num_in;

   SELECT inv_seq+1
   INTO inv_serial_no
   FROM po
   WHERE po.PO_num = po_num_in;

   SELECT concat(_astute_project_num, '-',_customer_id, '_', date_format(now(),'%y%m'),'-',inv_serial_no) INTO inv_number;
   RETURN inv_number;

--   SELECT concat(customer_code, '-',LPAD(po_count, 2, '0'), '_', date_format(now(),'%m%d'),'_',inv_count) INTO inv_number;
--   RETURN inv_number;

END;


-- Dumping structure for function astute.generate_inv_number

CREATE DEFINER=`root`@`localhost` FUNCTION `generate_inv_number`(po_num_in varchar(20)) RETURNS varchar(40) CHARSET utf8
BEGIN
   DECLARE customer_id_in varchar(20);
   DECLARE customer_code varchar(4);
   DECLARE po_count int;
   DECLARE inv_count int;
   DECLARE inv_number varchar(40);

   SELECT customer_id
   INTO customer_id_in
   FROM po
   WHERE po.po_num = po_num_in;

   SELECT substr(customer.customer_id, 1, 3)
   INTO customer_code
   FROM customer
   WHERE customer_id = customer_id_in;

   SELECT po_id
   INTO po_count
   FROM po
   WHERE PO_num = po_num_in;

   SELECT inv_seq + 1
   INTO inv_count
   FROM PO
   WHERE PO.PO_num = po_num_in;

   SELECT concat(customer_code, '-',LPAD(po_count, 2, '0'), '_DRAFT_',FLOOR(RAND()*(999))) INTO inv_number;
   RETURN inv_number;

END;


-- Dumping structure for function astute.get_customer_from_po

CREATE DEFINER=`root`@`localhost` FUNCTION `get_customer_from_po`(ponum varchar(20)) RETURNS varchar(20) CHARSET utf8
BEGIN
declare customer_id_out varchar(20);
  SELECT customer_id INTO customer_id_out FROM po WHERE po_num = ponum;
  return customer_id_out;
END;


-- Dumping structure for function astute.get_outstanding_inv_balance

CREATE FUNCTION astute.`get_outstanding_inv_balance`(invno varchar(20)) RETURNS double(10,2)
BEGIN
  declare outstanding_amt double(10,2);
  SELECT invoice.bill_amt - ifnull(sum(invoice_amount),0) INTO outstanding_amt
  FROM invoice join invoice_payment on invoice.inv_no = invoice_payment.inv_no
  where invoice.inv_no = invno;

  return outstanding_amt;
END;


-- Dumping structure for function astute.get_payment_status_name

CREATE DEFINER=`root`@`localhost` FUNCTION `get_payment_status_name`(pmt_status int) RETURNS varchar(20) CHARSET utf8
BEGIN
  declare payment_status_name VARCHAR(20);
  SELECT payment_status_desc INTO payment_status_name FROM payment_status WHERE payment_status_id = pmt_status;
  return payment_status_name;
END;


-- Dumping structure for function astute.get_payment_type

CREATE DEFINER=`root`@`localhost` FUNCTION `get_payment_type`(pmt_type int) RETURNS varchar(20) CHARSET utf8
BEGIN
  declare payment_type VARCHAR(20);
  SELECT payment_type_name INTO payment_type FROM payment_type WHERE payment_type_id = pmt_type;
  return payment_type;
END;


CREATE FUNCTION astute.`submit_invoice_fun`(invNo varchar(20)) RETURNS varchar(20) CHARSET utf8
BEGIN
DECLARE po_no varchar(20);
declare new_invoice_no varchar(20);
-- UPDATE INVOICE SET INV_STATUS = 2 WHERE INV_NO = invNo;
SELECT PO_NUM INTO po_no FROM INVOICE WHERE INV_NO = invNo;
SET new_invoice_no = generate_final_inv_number(po_no);
UPDATE INVOICE SET INV_NO = new_invoice_no, INV_STATUS = 2, submitted_date_time = current_timestamp() WHERE INV_NO = invNo;
UPDATE PO SET INV_SEQ = INV_SEQ + 1 WHERE PO_NUM = po_no;
RETURN new_invoice_no;
END;


-- Dumping structure for function astute.get_previously_billed_amt

CREATE FUNCTION astute.`get_previously_billed_amt`(po_no varchar(20), inv_no_in varchar(20)) RETURNS double(10,2)
BEGIN
  declare billed_amt double(10,2);
  declare submitted_datetime timestamp;
  SELECT submitted_date_time INTO submitted_datetime FROM INVOICE WHERE invoice.inv_no = inv_no_in;
  SELECT ifnull(sum(bill_amt),0) INTO billed_amt FROM invoice WHERE invoice.PO_num = po_no and inv_status = 2 and submitted_date_time < submitted_datetime ;
  return billed_amt;
END;



-- Dumping structure for table astute.invoice
CREATE TABLE `invoice` (
  `inv_no` varchar(20) NOT NULL,
  `inv_date` date NOT NULL,
  `PO_num` varchar(40) NOT NULL,
  `bill_amt` double NOT NULL,
  `special_notes` varchar(500) DEFAULT NULL,
  `certification` varchar(500) DEFAULT 'Certified that the above items and rates are in accordance with the contractual agreement as verified by the undersigned.',
  `inv_status` int(2) DEFAULT '1',
  `pmt_status` int(11) NOT NULL DEFAULT '1',
  `submitted_date_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`inv_no`),
  KEY `fk_InvMaster_POnum` (`PO_num`),
  CONSTRAINT `fk_InvMaster_POnum` FOREIGN KEY (`PO_num`) REFERENCES `po` (`PO_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Dumping data for table astute.invoice: ~3 rows (approximately)
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` (`inv_no`, `inv_date`, `PO_num`, `bill_amt`, `special_notes`, `certification`, `inv_status`, `pmt_status`) VALUES
	('MDO-01_0108_1', '2019-01-08', 'MDOT-123', 30250, 'Test', 'Certified that the above items and rates are in accordance with the contractual agreement as verified by the undersigned', 3, 1),
	('MDO-01_DRAFT_157', '2019-01-15', 'MDOT-123', 2500, 'Trest', 'Certified that the above items and rates are in accordance with the contractual agreement as verified by the undersigned', 1, 1),
	('VDO-01_DRAFT_351', '2019-01-22', 'VDOT-54321', 2000, 'Test', 'Certified that the above items and rates are in accordance with the contractual agreement as verified by the undersigned', 1, 1),
	('VDO-02_0107_2', '2019-01-07', 'ABC-123', 1450, 'Test', 'Certified that the above items and rates are in accordance with the contractual agreement as verified by the undersigned', 3, 1),
	('VDO-02_0108_3', '2019-01-08', 'ABC-123', 5250, 'Test', 'Certified that the above items and rates are in accordance with the contractual agreement as verified by the undersigned', 2, 1);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;

-- Dumping structure for table astute.invoice_detail
CREATE TABLE IF NOT EXISTS `invoice_detail` (
  `inv_num` varchar(20) NOT NULL,
  `line_item_num` int(11) NOT NULL,
  `PO_line_item_num` int(11) DEFAULT NULL,
  `service_type_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `qty` double DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `fee_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`inv_num`,`line_item_num`),
  KEY `fk_InvDetail_FeeType` (`fee_type_id`),
  CONSTRAINT `fk_InvDetail_FeeType` FOREIGN KEY (`fee_type_id`) REFERENCES `fee_type` (`fee_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_InvDetail_InvNum` FOREIGN KEY (`inv_num`) REFERENCES `invoice` (`inv_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE DEFINER=`root`@`localhost` TRIGGER `astute`.`after_invoice_detail_delete` AFTER DELETE ON astute.invoice_detail FOR EACH ROW
BEGIN
    UPDATE invoice
    SET bill_amt = (SELECT SUM(QTY*FEE) FROM invoice_detail WHERE invoice_detail.inv_num = OLD.inv_num)
    WHERE inv_no = OLD.inv_num;
END;

CREATE DEFINER=`root`@`localhost` TRIGGER `astute`.`after_invoice_detail_insert` AFTER INSERT ON astute.invoice_detail FOR EACH ROW
BEGIN
    UPDATE invoice
    SET bill_amt = (SELECT SUM(QTY*FEE) FROM invoice_detail WHERE invoice_detail.inv_num = NEW.inv_num)
    WHERE inv_no = NEW.inv_num;
END;

CREATE DEFINER=`root`@`localhost` TRIGGER `astute`.`after_invoice_detail_update` AFTER UPDATE ON astute.invoice_detail FOR EACH ROW
BEGIN
    UPDATE invoice
    SET bill_amt = (SELECT SUM(QTY*FEE) FROM invoice_detail WHERE invoice_detail.inv_num = OLD.inv_num)
    WHERE inv_no = OLD.inv_num;
END;

-- Dumping data for table astute.invoice_detail: ~14 rows (approximately)
/*!40000 ALTER TABLE `invoice_detail` DISABLE KEYS */;
INSERT INTO `invoice_detail` (`inv_num`, `line_item_num`, `PO_line_item_num`, `service_type_id`, `description`, `qty`, `fee`, `fee_type_id`) VALUES
	('MDO-01_0108_1', 1, 1, 2, 'Design', 0.5, 2500, 1),
	('MDO-01_0108_1', 2, 2, 1, 'Study', 5, 100, 2),
	('MDO-01_0108_1', 3, 3, 3, 'Peer Review', 50, 250, 2),
	('MDO-01_0108_1', 4, 4, 4, 'Cost Estimation', 50, 250, 2),
	('MDO-01_0108_1', 5, 5, 5, 'Forensic Investigation', 0.5, 5000, 1),
	('MDO-01_0108_1', 6, -1, 1, 'Out of Pocket Expenses - gas', 50, 20, 1),
	('MDO-01_DRAFT_157', 1, 1, 2, 'Design', 1, 2500, 1),
	('VDO-01_DRAFT_351', 1, 1, 1, 'Study existing designs', 0.5, 1000, 1),
	('VDO-01_DRAFT_351', 2, 2, 2, 'Modify design', 0.5, 2000, 1),
	('VDO-01_DRAFT_351', 3, 3, 4, 'Cost estimation', 0.5, 1000, 1),
	('VDO-02_0107_2', 1, 1, 2, 'Design', 0.25, 5000, 1),
	('VDO-02_0107_2', 2, -1, 1, 'Out of Pocket Expenses', 20, 10, 1),
	('VDO-02_0108_3', 1, 1, 2, 'Design', 0.25, 5000, 1),
	('VDO-02_0108_3', 2, 3, 1, 'Study', 200, 20, 1);
/*!40000 ALTER TABLE `invoice_detail` ENABLE KEYS */;

-- Dumping structure for table astute.invoice_notes
CREATE TABLE IF NOT EXISTS `invoice_notes` (
  `inv_no` varchar(20) NOT NULL,
  `inv_note` varchar(500) NOT NULL,
  PRIMARY KEY (`inv_no`),
  CONSTRAINT `fk_inv_notes_inv_no` FOREIGN KEY (`inv_no`) REFERENCES `invoice` (`inv_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.invoice_notes: ~0 rows (approximately)
/*!40000 ALTER TABLE `invoice_notes` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_notes` ENABLE KEYS */;

-- Dumping structure for table astute.invoice_payment
CREATE TABLE IF NOT EXISTS `invoice_payment` (
  `inv_no` varchar(20) NOT NULL,
  `invoice_payment_type` int(11) NOT NULL,
  `invoice_amount` double NOT NULL,
  `payment_date` date NOT NULL,
  `invoice_payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `check_no` varchar(50) DEFAULT NULL,
  `void_payment_status` int(11) NOT NULL DEFAULT '0',
  `transaction_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`invoice_payment_id`),
  KEY `fk_inv_pmt_inv_no` (`inv_no`),
  KEY `fk_pinv_pmt_type` (`invoice_payment_type`),
  CONSTRAINT `fk_inv_pmt_inv_no` FOREIGN KEY (`inv_no`) REFERENCES `invoice` (`inv_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_pinv_pmt_type` FOREIGN KEY (`invoice_payment_type`) REFERENCES `payment_type` (`payment_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- Dumping data for table astute.invoice_payment: ~2 rows (approximately)
/*!40000 ALTER TABLE `invoice_payment` DISABLE KEYS */;
INSERT INTO `invoice_payment` (`inv_no`, `invoice_payment_type`, `invoice_amount`, `payment_date`, `invoice_payment_id`, `check_no`, `void_payment_status`, `transaction_no`) VALUES
	('VDO-02_0107_2', 2, 5000, '2019-01-09', 13, '123123', 0, '456456'),
	('VDO-02_0107_2', 2, 5000, '2019-01-08', 15, '123124', 0, '456457'),
	('VDO-02_0108_3', 2, 0.1, '2019-01-22', 16, 'asdfasdf', 0, 'asdfasdf');
/*!40000 ALTER TABLE `invoice_payment` ENABLE KEYS */;

-- Dumping structure for table astute.invoice_status
CREATE TABLE IF NOT EXISTS `invoice_status` (
  `inv_status_id` int(11) NOT NULL,
  `inv_status_desc` varchar(20) NOT NULL,
  PRIMARY KEY (`inv_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.invoice_status: ~3 rows (approximately)
/*!40000 ALTER TABLE `invoice_status` DISABLE KEYS */;
INSERT INTO `invoice_status` (`inv_status_id`, `inv_status_desc`) VALUES
	(1, 'Draft'),
	(2, 'Submitted'),
	(3, 'Void');
/*!40000 ALTER TABLE `invoice_status` ENABLE KEYS */;

-- Dumping structure for function astute.isAnyInvInDraft

CREATE DEFINER=`root`@`localhost` FUNCTION `isAnyInvInDraft`(PO_num_in varchar(20)) RETURNS int(11)
BEGIN
declare isInvInDraft int;
  SELECT count(*) into isInvInDraft from invoice where po_num = PO_num_in and inv_status = 1;
  return isInvInDraft;
END;


-- Dumping structure for table astute.payment_status
CREATE TABLE IF NOT EXISTS `payment_status` (
  `payment_status_id` int(11) NOT NULL,
  `payment_status_desc` varchar(20) NOT NULL,
  PRIMARY KEY (`payment_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.payment_status: ~3 rows (approximately)
/*!40000 ALTER TABLE `payment_status` DISABLE KEYS */;
INSERT INTO `payment_status` (`payment_status_id`, `payment_status_desc`) VALUES
	(1, 'Outstanding'),
	(2, 'Partially paid'),
	(3, 'Paid');
/*!40000 ALTER TABLE `payment_status` ENABLE KEYS */;

-- Dumping structure for table astute.payment_type
CREATE TABLE IF NOT EXISTS `payment_type` (
  `payment_type_id` int(11) NOT NULL,
  `payment_type_name` varchar(20) NOT NULL,
  PRIMARY KEY (`payment_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.payment_type: ~3 rows (approximately)
/*!40000 ALTER TABLE `payment_type` DISABLE KEYS */;
INSERT INTO `payment_type` (`payment_type_id`, `payment_type_name`) VALUES
	(1, 'Credit Card'),
	(2, 'Check'),
	(3, 'ACH'),
	(4, 'Wire Transfer');
/*!40000 ALTER TABLE `payment_type` ENABLE KEYS */;

-- Dumping structure for table astute.po
CREATE TABLE IF NOT EXISTS `po` (
  `PO_num` varchar(40) NOT NULL COMMENT 'Alpha numeric, auto generated in frontend',
  `contract_num` varchar(20) DEFAULT NULL,
  `PO_date` date DEFAULT NULL,
  `contract_amt` double(10,2) DEFAULT NULL,
  `customer_id` varchar(11) NOT NULL,
  `astute_project_num` varchar(20) NOT NULL,
  `po_id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `inv_seq` int(3) NOT NULL DEFAULT '0',
  `notes` varchar(200) DEFAULT NULL,
  `final` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PO_num`),
  KEY `ind_pomaster_contractno` (`contract_num`),
  KEY `ind_pomaster_podate` (`PO_date`),
  KEY `po_customer_id` (`customer_id`),
  CONSTRAINT `po_customer_id_fk` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.po: ~3 rows (approximately)
/*!40000 ALTER TABLE `po` DISABLE KEYS */;
INSERT INTO `po` (`PO_num`, `contract_num`, `PO_date`, `contract_amt`, `customer_id`, `astute_project_num`, `po_id`, `title`, `inv_seq`, `notes`, `final`) VALUES
	('ABC-123', 'ABC-123', '2018-09-23', 27000.00, 'VDOT', 'ABC-123', 2, 'ABC-123', 3, NULL, 0),
	('MDOT-123', 'MDOT-123 ContractNo', '2019-01-08', 58500.00, 'MDOT', 'MDOT-123 ProjNo', 1, 'MDOT-123 SO Title', 1, NULL, 0),
	('VDOT-54321', 'VDOT-54321', '2018-09-22', 10000.00, 'VDOT', 'VDOTProj', 1, 'Supervisor', 1, NULL, 1);
/*!40000 ALTER TABLE `po` ENABLE KEYS */;

-- Dumping structure for table astute.po_detail
CREATE TABLE IF NOT EXISTS `po_detail` (
  `PO_num` varchar(40) NOT NULL,
  `line_item_no` int(11) NOT NULL,
  `service_desc` varchar(500) DEFAULT NULL,
  `fee_type_id` int(11) DEFAULT '1' COMMENT '1-fixed fee, 2-hourly',
  `qty` double DEFAULT NULL,
  `service_type_id` int(1) DEFAULT '1' COMMENT '1-studies, 2-supplemental service, 3-out of pocket, 4-reimbursement, 5-',
  `fee` double DEFAULT NULL,
  `remaining_qty` double DEFAULT NULL,
  PRIMARY KEY (`PO_num`,`line_item_no`),
  KEY `fk_PODetail_ServType` (`service_type_id`),
  CONSTRAINT `fk_PODetail_POnum` FOREIGN KEY (`PO_num`) REFERENCES `po` (`PO_num`),
  CONSTRAINT `fk_PODetail_ServType` FOREIGN KEY (`service_type_id`) REFERENCES `service_type` (`service_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.po_detail: ~12 rows (approximately)
/*!40000 ALTER TABLE `po_detail` DISABLE KEYS */;
INSERT INTO `po_detail` (`PO_num`, `line_item_no`, `service_desc`, `fee_type_id`, `qty`, `service_type_id`, `fee`, `remaining_qty`) VALUES
	('ABC-123', 1, 'Design', 1, 1, 2, 5000, 0.75),
	('ABC-123', 2, 'Out of Expense', 1, 100, 6, 20, 100),
	('ABC-123', 3, 'Study', 1, 1000, 1, 20, 800),
	('MDOT-123', 1, 'Design', 1, 1, 2, 2500, 0),
	('MDOT-123', 2, 'Study', 2, 10, 1, 100, 10),
	('MDOT-123', 3, 'Peer Review', 2, 100, 3, 250, 100),
	('MDOT-123', 4, 'Cost Estimation', 2, 100, 4, 250, 100),
	('MDOT-123', 5, 'Forensic Investigation', 1, 1, 5, 5000, 1),
	('VDOT-54321', 1, 'Study existing designs', 1, 1, 1, 1000, 0.5),
	('VDOT-54321', 2, 'Modify design', 1, 1, 2, 2000, 0.5),
	('VDOT-54321', 3, 'Cost estimation', 1, 1, 4, 1000, 0.5),
	('VDOT-54321', 4, 'Peer Review', 2, 100, 3, 100, 100);
/*!40000 ALTER TABLE `po_detail` ENABLE KEYS */;

-- Dumping structure for table astute.service_type
CREATE TABLE IF NOT EXISTS `service_type` (
  `service_type_id` int(11) NOT NULL,
  `desc` varchar(40) NOT NULL,
  PRIMARY KEY (`service_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.service_type: ~6 rows (approximately)
/*!40000 ALTER TABLE `service_type` DISABLE KEYS */;
INSERT INTO `service_type` (`service_type_id`, `desc`) VALUES
	(1, 'Studies'),
	(2, 'Design'),
	(3, 'Peer Review'),
	(4, 'Cost Estimation'),
	(5, 'Forensic Investigation'),
	(6, 'Out-of-pocket Expense');
/*!40000 ALTER TABLE `service_type` ENABLE KEYS */;

-- Dumping structure for table astute.session
CREATE TABLE `session` (
  `session_id` varchar(200) NOT NULL,
  `user_id` int(11) NOT NULL,
  `session_start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `session_end_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`),
  KEY `fk_session_user_id` (`user_id`),
  CONSTRAINT `fk_session_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.session: ~40 rows (approximately)
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` (`session_id`, `user_id`, `session_start_date`, `session_end_date`) VALUES
	('042ef08346f84d52b98a22dab48c7b1c', 1, NULL, NULL),
	('058cdb87447645da9ec265e566af834c', 1, NULL, NULL),
	('0aa7153e26444ba186254a55169d3ead', 1, NULL, NULL),
	('12c9003cf85145b9a352a0472d062858', 1, NULL, NULL),
	('2781b188aada4751828d9b7369f8c1a0', 1, NULL, NULL),
	('2b4382383bd143f9b995c4321fa6d1ac', 1, NULL, NULL),
	('3492c28027db4033987a62d80714b8d0', 1, NULL, NULL),
	('3779d52b38ea42898e268ad0f151ae64', 1, NULL, NULL),
	('37ac54b5cf5241b080b019d23c151fd8', 1, NULL, NULL),
	('46d91a474f4544a2ab60002f799383a7', 1, NULL, NULL),
	('4aaceb013ea24e07a527b125945cf939', 1, NULL, NULL),
	('4f48b60481ab4729a26b809c077fc7c0', 1, NULL, NULL),
	('4f5da5716384478e9c8f7771a0747ebe', 1, NULL, NULL),
	('50ba05a7d0f3451badd529b7f02cd4c5', 1, NULL, NULL),
	('5df808bb502044ccb6e9da1bce0c63d4', 2, NULL, NULL),
	('66ed2bccbaf34b1e96b2b81393996cf9', 1, NULL, NULL),
	('687e971f46884e968b234c9b47aa8629', 1, NULL, NULL),
	('7a73642eb2ce493f8580599a532cd8e9', 1, NULL, NULL),
	('84237645fad54f5aa4d1ae4e24dcefc6', 1, NULL, NULL),
	('8f8991185a174b87adb7d0b1b40c1475', 1, NULL, NULL),
	('9273d151dabf4bc38e65cb1a568f9504', 1, NULL, NULL),
	('92cd1d01085c4ead892a1c7c137631dd', 1, NULL, NULL),
	('950dfd7addb34ac8930cbaa11a8aee26', 1, NULL, NULL),
	('9c0a7444f67f4de5a789014413445458', 1, NULL, NULL),
	('a067451faf5f401680562bb291295629', 1, NULL, NULL),
	('abeefc05fe8e48e5bac7ffab65c85ca6', 1, NULL, NULL),
	('aeda56d19a7d4fbfa9176b63e14ff4b2', 1, NULL, NULL),
	('b356aab1dbe84d4f9eea9c1cd965c9a4', 1, NULL, NULL),
	('b77de6ef2b4b4e0487689c689f246ab5', 1, NULL, NULL),
	('b9e4507fcc8f487eaf3eb3a9f3b378ed', 1, NULL, NULL),
	('c977d55928a145048078b3e9d874a607', 1, NULL, NULL),
	('c9bbb1118e5a4dd5b2c873db6ccdcc55', 1, NULL, NULL),
	('d14cb15f91454c3ba3bbfda60444bb05', 1, NULL, NULL),
	('d6387d93d84341fc91a0c4a5cbf266db', 1, NULL, NULL),
	('d98f7e0c9a934afaa89f0f0545c1c81c', 1, NULL, NULL),
	('dcb4b261f925464bb69ff685c1c6134d', 1, NULL, NULL),
	('e77c5380fa3041a2a22c311198337d4a', 1, NULL, NULL),
	('eb943e0991d445919b2ab62248c31494', 1, NULL, NULL),
	('f514f61058be4ec18c5fbb80a9908ea0', 1, NULL, NULL),
	('f6546dbfa5454a339f2093b2ac1f3b54', 1, NULL, NULL),
	('fea623a6ff3148899214750707f4f52e', 1, NULL, NULL);
/*!40000 ALTER TABLE `session` ENABLE KEYS */;

-- Dumping structure for procedure astute.submit_invoice

CREATE DEFINER=`root`@`localhost` PROCEDURE `submit_invoice`(invNo varchar(20))
BEGIN
DECLARE po_no varchar(20);
UPDATE INVOICE SET INV_STATUS = 2 WHERE INV_NO = invNo;
SELECT PO_NUM INTO po_no FROM INVOICE WHERE INV_NO = invNo;
 UPDATE INVOICE SET INV_NO = generate_final_inv_number(po_no), INV_STATUS = 2 WHERE INV_NO = invNo;
UPDATE PO SET INV_SEQ = INV_SEQ + 1 WHERE PO_NUM = po_no;
Commit;
END;


CREATE FUNCTION astute.`get_remaining_qty_fun`(po_no_in varchar(40), inv_num_in varchar(40), item_no_in int) RETURNS double
BEGIN
DECLARE rem_qty double;
DECLARE po_no varchar(40);
if inv_num_in <> null or inv_num_in <> '' THEN
  SELECT po_num INTO po_no FROM invoice where inv_no = inv_num_in;
else
  set po_no = po_no_in;
END IF;

select po_detail.qty - ifnull(sum(invoice_detail.qty),0) into rem_qty from invoice_detail, invoice, po_detail
where invoice_detail.inv_num in (select inv_no from invoice where invoice.PO_num = po_no)
and invoice_detail.po_line_item_num = item_no_in
and po_detail.PO_num = po_no
and invoice.PO_num = po_detail.PO_num
and invoice.inv_no = invoice_detail.inv_num
and (invoice.inv_status = 2)
and invoice_detail.po_line_item_num = po_detail.line_item_no;

return rem_qty;
END;
-- Dumping structure for procedure astute.update_all_remaining_quantities

DROP PROCEDURE IF EXISTS astute.update_all_remaining_quantities;
CREATE PROCEDURE astute.`update_all_remaining_quantities`(invNo varchar(20))
BEGIN

DECLARE po_num_in VARCHAR(20);

SELECT PO_NUM INTO po_num_in FROM invoice where inv_no = invNo;
UPDATE PO_DETAIL SET remaining_qty = get_remaining_qty_fun(po_num_in,'',line_item_no);
END;



CREATE FUNCTION astute.`get_draft_remaining_qty_fun`(inv_num_in varchar(40), item_no_in int) RETURNS double
BEGIN
DECLARE draft_qty double;
DECLARE ponum varchar(20);
DECLARE submitted_qty double;
DECLARE po_item_no_in int;

SELECT po_line_item_num into po_item_no_in from invoice_detail where inv_num = inv_num_in and line_item_num = item_no_in;
SELECT po_num into ponum from invoice where inv_no = inv_num_in;
SELECT sum(qty) into draft_qty from invoice_detail where inv_num = inv_num_in and po_line_item_num = po_item_no_in;
SELECT remaining_qty into submitted_qty from po_detail where PO_num = ponum and line_item_no = po_item_no_in;
return submitted_qty-draft_qty;
END;

-- Dumping structure for function astute.update_remaining_qty_fun

CREATE FUNCTION astute.`update_remaining_qty_fun`(po_no_in varchar(40), inv_num_in varchar(40), item_no_in int) RETURNS double
BEGIN
DECLARE rem_qty double;
DECLARE po_no varchar(40);
if inv_num_in <> null or inv_num_in <> '' THEN
  SELECT po_num INTO po_no FROM invoice where inv_no = inv_num_in;
else
  set po_no = po_no_in;
END IF;

select po_detail.qty - ifnull(sum(invoice_detail.qty),0) into rem_qty from invoice_detail, invoice, po_detail
where invoice_detail.inv_num in (select inv_no from invoice where invoice.PO_num = po_no)
and invoice_detail.po_line_item_num = item_no_in
and po_detail.PO_num = po_no
and invoice.PO_num = po_detail.PO_num
and invoice.inv_no = invoice_detail.inv_num
and (invoice.inv_status = 2)
and invoice_detail.po_line_item_num = po_detail.line_item_no;

update po_detail set remaining_qty = rem_qty where PO_num = po_no and line_item_no = item_no_in;
return rem_qty;
END;


CREATE FUNCTION astute.`is_po_fulfilled`(po_no varchar(20)) RETURNS int(1)
BEGIN
  declare count int;
  SELECT count(*) INTO count FROM po_detail WHERE PO_num = po_no and remaining_qty > 0;
  if count > 0 then
      return 0;
  else
      return 1;
  END IF;
END;

-- Dumping structure for table astute.user
CREATE TABLE `user` (
  `user_id` int(5) NOT NULL,
  `username` blob,
  `password` blob,
  `first_name` varchar(20) DEFAULT NULL,
  `middle_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `role` varchar(20) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `office_phone_ext` int(3) DEFAULT NULL,
  `cell_phone` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table astute.user: ~2 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `username`, `password`, `first_name`, `middle_name`, `last_name`, `role`, `email`, `office_phone_ext`, `cell_phone`) VALUES
	(1, ENCODE('sparikh', 'astutesecret'), ENCODE('sparikh', 'astutesecret'), 'Saurin', NULL, 'Parikh', 'Owner', 'sparikh@Astuteng.com', 2024002004, 3014616485),
	(2, ENCODE('humaretiya', 'astutesecret'), ENCODE('humaretiya', 'astutesecret'), 'Haresh', NULL, 'Umaretiya', 'Owner', 'Humaretiya@astuteng.com', 2024002004, 0);


/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

-- create and GRANT ALL PRIVILEGES ON *.* TO 'astute_user'@'localhost' IDENTIFIED BY 'password';
-- CREATE USER 'astute_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'astute_user'@'localhost';
