import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ToastManagerService} from './toast-manager/toast-service.service';

@Injectable()
export class AstuteClientService {
    headers = {
        headers: new HttpHeaders().set('Content-Type', 'application/json'),
    };
    private authUrl = 'http://localhost:8080/astutesystem/auth';
    private customerUrl = 'http://localhost:8080/astutesystem/customer';
    private customerContactUrl = 'http://localhost:8080/astutesystem/customer/contact';
    private POUrl = 'http://localhost:8080/astutesystem/po';
    private POServiceTypesUrl = 'http://localhost:8080/astutesystem/po/serviceTypes';
    private PODetailUrl = 'http://localhost:8080/astutesystem/po/detail';
    private invoiceUrl = 'http://localhost:8080/astutesystem/invoice';
    private invoiceDetailUrl = 'http://localhost:8080/astutesystem/invoice/detail';
    private invoiceGenUrl = 'http://localhost:8080/astutesystem/invoice/generatedInvoice';
    private invoicePaymentUrl = 'http://localhost:8080/astutesystem/invoicePayment';
    private serviceTypeUrl = 'http://localhost:8080/astutesystem/serviceType';
    private sessionString = `?sessionId=${localStorage.getItem('SESSION_ID')}`;


    constructor(private http: HttpClient, private toastService: ToastManagerService) { }

    // **************************************** AUTH Service methods
    public login(username: string, password: string): Promise<string> {
        console.log('*** In login()');
        const data = {
            'username': username,
            'password': password
        };
        return this.http
            .post(this.authUrl, data)
            .toPromise()
            .then(response => {
                console.log(response['entity']);
                const name = response['entity'].name;
                const sessionId = response['entity'].sessionId;
                if (sessionId != null) {
                    localStorage.setItem('SESSION_ID', sessionId);
                    this.sessionString = `?sessionId=${localStorage.getItem('SESSION_ID')}`;
                    console.log(sessionId);
                    localStorage.setItem('SESSION_USER', name);
                    return sessionId;
                } else {
                    return null;
                }
            });
    }
    public logout() {
        console.log('*** In logout()');
        return this.http
            .post(`${this.authUrl}/logout${this.sessionString}`, {})
            .toPromise()
            .then(response => {
                localStorage.removeItem('SESSION_ID');
                localStorage.removeItem('SESSION_USER');
                return response['entity'];
            });
    }
    public getSessionId(): string {
        console.log('*** In getSessionId()');
        console.log(localStorage.getItem('SESSION_ID'));
        return localStorage.getItem('SESSION_ID');
    }
    public getSessionUser(): string {
        console.log('*** In getSessionUser()');
        console.log(localStorage.getItem('SESSION_USER'));
        return localStorage.getItem('SESSION_USER');
    }

    // **************************************** Customer Service methods
    public getCustomers(): Promise<any> {
        console.log('*** In getCustomers()');
        const url = `${this.customerUrl}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Customers Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Customers Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updateCustomer(customerId: number, data: any): Promise<any> {
        console.log('*** In updateCustomer()');
        const url = `${this.customerUrl}/${customerId}${this.sessionString}`;
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Customer Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Customer Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createCustomer(data: any): Promise<any> {
        console.log('*** In createCustomer()');
        const url = `${this.customerUrl}${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Customer Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Customer Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public deleteCustomer(customerId) {
        console.log('*** In deleteCustomer()');
        const url = `${this.customerUrl}/${customerId}/delete${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Delete Customer Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Delete Customer Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }

    // **************************************** Customer Contact Service methods
    public getCustomerContacts(customerId): Promise<any> {
        console.log('*** In getCustomerContacts()');
        const url = `${this.customerContactUrl}?customerId=${customerId}&sessionId=${localStorage.getItem('SESSION_ID')}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Customer Contacts Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Customer Contacts Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updateCustomerContact(customerId: string, data: any): Promise<any> {
        console.log('*** In updateCustomerContact()');
        const url = `${this.customerContactUrl}/${customerId}${this.sessionString}`;
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Customer Contacts Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Customer Contacts Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public deleteCustomerContact(customerId: string, contactId: number): Promise<any> {
        console.log('*** In deleteCustomerContact()');
        const url = `${this.customerContactUrl}/${customerId}/${contactId}/delete${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Delete Customer Contacts Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Delete Customer Contacts Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createCustomerContact(data: any): Promise<any> {
        console.log('*** In createCustomerContact()');
        const url = `${this.customerContactUrl}/${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Customer Contacts Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Customer Contacts Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }

    // **************************************** PO Service methods
    public getPOs(): Promise<any> {
        console.log('*** In getPOs()');
        const url = `${this.POUrl}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Sales Orders Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Sales Orders Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getPODetail(ponum): Promise<any> {
        console.log('*** In getPODetail()');
        const url = `${this.PODetailUrl}?PONum=${ponum}&sessionId=${localStorage.getItem('SESSION_ID')}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Sales Order Details Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Sales Order Details Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updatePO(ponum: string, data: any): Promise<any> {
        console.log('*** In updatePO()');
        const url = `${this.POUrl}/${ponum}${this.sessionString}`;
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Sales Order Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Sales Order Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createPO(data: any): Promise<any> {
        console.log('*** In createPO()');
        const url = `${this.POUrl}${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Sales Order Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Sales Order Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updatePODetail(ponum, lineItemNo, data) {
        console.log('*** In updatePODetail()');
        const sessionId = localStorage.getItem('sessionId');
        const url = `${this.POUrl}/detail/${ponum}/${lineItemNo}${this.sessionString}`;
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Sales Order Details Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Sales Order Details Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public deletePODetail(ponum, lineItemNo) {
        console.log('*** In deletePODetail()');
        const sessionId = localStorage.getItem('sessionId');
        const url = `${this.POUrl}/detail/${ponum}/${lineItemNo}${this.sessionString}`;
        return this.http.delete(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Delete Sales Order Details Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Delete Sales Order Details Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createPODetail(data) {
        console.log('*** In createPODetail()');
        const url = `${this.POUrl}/detail${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Sales Order Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Sales Order Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    // public deletePODetail(customerId) {
    //     console.log('*** In deleteCustomer()');
    //     const url = `${this.customerUrl}/${customerId}/delete${this.sessionString}`;
    //     return this.http.put(url, {})
    //         .toPromise()
    //         .then(response => {
    //             const code = response['code'];
    //             const message = response['message'];
    //             if (code === 9000) {
    //                 console.log(response['entity']);
    //                 return response['entity'];
    //             } else if (message.includes('login')) {
    //                 this.notif('Please login again!');
    //             } else {
    //                 this.notif('Delete Customer Failed: ' + message);
    //             }
    //         }, (reason) => {
    //             this.notif('Delete Customer Failed: ' + reason);
    //         })
    //         .catch( error => {
    //             this.notif(error);
    //         });
    // }
    public finalizePO(ponum: string) {
        console.log('*** In finalizePO()');
        const url = `${this.POUrl}/${ponum}/finalize${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('finalizePO Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Finalize Sales Order Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public deletePO(ponum: string) {
        console.log('*** In deletePO()');
        const url = `${this.POUrl}/${ponum}/delete${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Delete Sales Order Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Delete Sales Order Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getRateTypes(): Promise<any> {
        console.log('*** In getRateTypes()');
        const url = `${this.POUrl}/feeTypes${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Rate Types Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Rate Types Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }

    // **************************************** Invoice Service methods
    public submitInvoice (invoiceNumber) {
        console.log('*** In submitInvoice(), invoiceNumber' + invoiceNumber);
        const url = `${this.invoiceUrl}/${invoiceNumber}/submit${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Submit Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Submit Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public voidInvoice (invoiceNumber) {
        console.log('*** In voidInvoice(), invoiceNumber' + invoiceNumber);
        const url = `${this.invoiceUrl}/${invoiceNumber}/void${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Void Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Void Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public deleteInvoice (invoiceNumber) {
        console.log('*** In deleteInvoice(), invoiceNumber' + invoiceNumber);
        const url = `${this.invoiceUrl}/${invoiceNumber}/delete${this.sessionString}`;
        return this.http.put(url, {})
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Delete Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Delete Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public generateInvoiceNumber (ponum) {
        console.log('*** In generateInvoiceNumber()');
        const url = `${this.invoiceUrl}/generateInvoiceNumber/${ponum}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Generate Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Generate Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getInvoices(): Promise<any> {
        console.log('*** In getInvoices()');
        const url = `${this.invoiceUrl}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Invoices Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Invoices Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getInvoiceDetail(invoiceId: string): Promise<any> {
        console.log('*** In getInvoiceDetail()');
        const url = `${this.invoiceDetailUrl}?invoiceNumber=${invoiceId}&sessionId=${localStorage.getItem('SESSION_ID')}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Invoice Details Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Invoice Details Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getInvoiceGen (invoiceId: string): Promise<any> {
        console.log('*** In getInvoiceGen()');
        const url = `${this.invoiceGenUrl}/${invoiceId}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Generated Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Generated Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updateInvoice(invoiceNumber: string, data: any): Promise<any> {
        console.log('*** In updateInvoice()');
        const url = `${this.invoiceUrl}/${invoiceNumber}${this.sessionString}`;
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createInvoice(data: any): Promise<any> {
        console.log('*** In createInvoice()');
        const url = `${this.invoiceUrl}${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Invoice Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Invoice Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updateInvoiceDetail(invNum, lineItemNo, data) {
        console.log('*** In updateInvoiceDetail()');
        const url = `${this.invoiceUrl}/detail/${invNum}/${lineItemNo}${this.sessionString}`;
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Invoice Detail Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Invoice Detail Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public deleteInvoiceDetail(invNum, lineItemNo) {
        console.log('*** In deleteInvoiceDetail()');
        const url = `${this.invoiceUrl}/detail/${invNum}/${lineItemNo}${this.sessionString}`;
        return this.http.delete(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Delete Invoice Detail Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Delete Invoice Detail Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createInvoiceDetail(data) {
        console.log('*** In createInvoiceDetail()');
        const url = `${this.invoiceUrl}/detail${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Invoice Details Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Invoice Details Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }


    // **************************************** Invoice Payment Service methods
    public getSumittedInvoices(): Promise<any> {
        console.log('*** In getSumittedInvoices()');
        const url = `${this.invoiceUrl}/submitted${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Submitted Invoices Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Submitted Invoices Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getInvoicePaymentTypes(): Promise<any> {
        console.log('*** In getInvoicePaymentTypes()');
        const url = `${this.invoicePaymentUrl}/paymentTypes${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Invoice Payment Types Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Invoice Payment Types Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public getInvoicePayments(): Promise<any> {
        console.log('*** In getInvoicePayments()');
        const url = `${this.invoicePaymentUrl}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Invoice Payments Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Invoice Payments Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updateInvoicePayment(invoiceNumber: string, invoicePaymentId: string, data: any): Promise<any> {
        console.log('*** In updateInvoicePayment()');
        const url = `${this.invoicePaymentUrl}/${invoiceNumber}/${invoicePaymentId}${this.sessionString}`;
        console.log('*** invoicePaymentUrl is ' + url);
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Invoice Payment Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Invoice Payment Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public addInvoicePayment(data: any): Promise<any> {
        console.log('*** In addInvoicePayment()');
        const url = `${this.invoicePaymentUrl}${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Add Invoice Payment Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Add Invoice Payment Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }

    // **************************************** Service Type methods
    public getServiceTypes(): Promise<any> {
        console.log('*** In getServiceTypes()');
        const url = `${this.serviceTypeUrl}${this.sessionString}`;
        return this.http.get(url)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Get Service Types Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Get Service Types Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public updateServiceType(serviceTypeId, data: any): Promise<any> {
        console.log('*** In updateServiceType()');
        const url = `${this.serviceTypeUrl}/${serviceTypeId}${this.sessionString}`;
        console.log('*** updateServiceType is ' + url);
        return this.http.put(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Update Service Type Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Update Service Type Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }
    public createServiceType(data: any): Promise<any> {
        console.log('*** In createServiceType()');
        const url = `${this.serviceTypeUrl}${this.sessionString}`;
        return this.http.post(url, data)
            .toPromise()
            .then(response => {
                const code = response['code'];
                const message = response['message'];
                if (code === 9000) {
                    console.log(response['entity']);
                    return response['entity'];
                } else if (message.includes('login')) {
                    this.notif('Please login again!');
                } else {
                    this.notif('Create Service Type Failed: ' + message);
                }
            }, (reason) => {
                this.notif('Create Service Type Failed: ' + reason);
            })
            .catch( error => {
                this.notif(error);
            });
    }


    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }
}
