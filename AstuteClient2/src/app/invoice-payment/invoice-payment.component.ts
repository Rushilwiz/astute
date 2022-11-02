import {Component, OnInit, ViewChild} from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {formatCurrency} from "@angular/common";
import {ToastManagerService} from "../services/toast-manager/toast-service.service";

@Component({
    selector: 'app-invoice-payment',
    templateUrl: './invoice-payment.component.html',
    styleUrls: ['./invoice-payment.component.css']
})
export class InvoicePaymentComponent implements OnInit {
    @ViewChild('agGrid', {'static': false}) agGrid;

    loggedIn: boolean;
    selected = null;
    chosenInv: any = 0;
    chosenPaymentType: any = 0;
    invoices;
    rowData;
    paymentTypes;
    invoicePaymentData;
    columnDefs = [
        {headerName: 'Invoice Payment Id', field: 'invoicePaymentId'},
        {headerName: 'Invoice Number', field: 'invoiceNum'},
        {headerName: 'Payment Received', field: 'invoiceAmount'},
        {headerName: 'Date Received', field: 'paymentDate'},
        {headerName: 'Payment Type', field: 'paymentType'},
        {headerName: 'Check / ACH #', field: 'checkNo'},
        {headerName: 'Transaction #', field: 'transactionNo'}
    ];
    constructor(protected astuteClientService: AstuteClientService, protected toastService: ToastManagerService) {
    }

    ngOnInit() {
        if (localStorage.getItem('SESSION_ID') && localStorage.getItem('SESSION_USER')) {
            this.loggedIn = true;
            this.refreshData();
        } else {
            this.loggedIn = false;
        }
    }

    invoiceDropdownChange(index) {
        this.chosenInv = this.invoices[index].invoiceNumber;
    }

    paymentTypeDropdownChange(index) {
        this.chosenPaymentType = this.paymentTypes[index].paymentTypeId;
    }

    getSelectedRows() {
        const selectedNodes = this.agGrid.api.getSelectedNodes();
        if (selectedNodes.length) {
            this.selected = selectedNodes.map(node => node.data)[0];
        } else {
            this.selected = null;
        }
    }

    addInvoicePayment(invoiceNum, invoicePaymentId, paymentTypeId, paymentDate, paymentReceived, checkNo, transactionNo, ref) {
        let invoicePaymentData = {
            "invoiceNum": invoiceNum,
            "invoicePaymentId":invoicePaymentId,
            "paymentTypeId":paymentTypeId,
            "paymentDate": paymentDate,
            "invoiceAmount": paymentReceived,
            "checkNo": checkNo,
            "transactionNo": transactionNo
    };
        this.astuteClientService.addInvoicePayment(invoicePaymentData).then((data) => {
            if (data) {
                this.refreshData();
                ref.close();
            } else {
                this.notif("Adding Invoice Payment Failed, Check Input Fields")
            }
        }, (reason) => {
            this.notif("Adding Invoice Payment failed for " + reason);
        });
    }

    updateInvoicePayment(invoiceNum, invoicePaymentId, paymentTypeId, dateReceived, paymentReceived, checkNo, transactionNo, ref) {
        const invoicePaymentData = {
            "invoiceNum": invoiceNum,
            "invoicePaymentId": invoicePaymentId,
            "paymentTypeId": paymentTypeId,
            "paymentDate": dateReceived,
            "invoiceAmount": paymentReceived,
            "checkNo": checkNo,
            "transactionNo": transactionNo
        };

        this.astuteClientService.updateInvoicePayment(invoiceNum, invoicePaymentId, invoicePaymentData).then((data) => {
            if (data) {
                this.refreshData();
                ref.close();
            } else {
                this.notif("Updating Invoice Payment Failed, Check Input Fields")
            }
        }, (reason) => {
            this.notif("Updating Invoice Payment failed for " + reason);
        });
    }

    open(ref) {
        this.getSelectedRows();
        ref.open();
    }

    close(ref) {
        ref.close();
    }

    refreshData() {
        this.astuteClientService.getSumittedInvoices().then((data) => {
            this.invoices = data;
        });
        this.astuteClientService.getInvoicePaymentTypes().then((data) => {
            this.paymentTypes = data;
        });

        this.astuteClientService.getInvoicePayments().then ((data) => {
            if (data) {
                this.rowData = data;
                this.rowData.forEach((row) => {
                    row.invoiceAmount = formatCurrency(row.invoiceAmount, 'en-US', '$', 'USD');
                });
            }
        });
        this.astuteClientService.getInvoicePayments().then((data) => {
            this.invoicePaymentData = data;
        });
    }
    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }

}
