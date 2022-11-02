import {Component, OnInit} from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {ToastManagerService} from '../services/toast-manager/toast-service.service';
import {PhoneFormatterComponent} from '../ag-grid-components/phone-formatter/phone-formatter.component';
import {PhoneEditorComponent} from '../ag-grid-components/phone-editor/phone-editor.component';
import {EmptyErrorEditorComponent} from '../ag-grid-components/empty-error-editor/empty-error-editor.component';
import {NumericEditorComponent} from "../ag-grid-components/numeric-editor/numeric-editor.component";

@Component({
    selector: 'app-customer',
    templateUrl: './customer.component.html',
    styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {
    // @ViewChild('agGrid') agGrid;
    loggedIn: boolean;
    gridApi;
    gridColumnApi;
    contactGridApi;
    contactColumnApi;
    selected = null;
    customers;
    columnDefs = [
        {headerName: 'ID', field: 'customerId'},
        {headerName: 'Name ✎', field: 'customerName', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Bill To ✎', field: 'billToDept', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Email ✎', field: 'email', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Fax ✎', field: 'fax', editable: true, cellEditor: 'phoneEditorComponent'},
        {headerName: 'Phone ✎', field: 'phone', editable: true, cellEditor: 'phoneEditorComponent'},
        {headerName: 'Ext. ✎', field: 'phExt', editable: true, cellEditor: 'numericEditorComponent'},
        {headerName: 'Address 1 ✎', field: 'add1', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Address 2 ✎', field: 'add2', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'City ✎', field: 'city', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'State ✎', field: 'state', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'ZIP ✎', field: 'zip', editable: true, cellEditor: 'numericEditorComponent'},
        {headerName: 'ZIP-4 ✎', field: 'ziplast4', editable: true, cellEditor: 'numericEditorComponent'}
    ];
    frameworkComponents = {
        phoneFormatterComponent: PhoneFormatterComponent,
        phoneEditorComponent: PhoneEditorComponent,
        emptyErrorEditorComponent: EmptyErrorEditorComponent,
        numericEditorComponent: NumericEditorComponent
    };
    rowData: any;
    states = [
        'AL',
        'AK',
        'AR',
        'AZ',
        'CA',
        'CO',
        'CT',
        'DC',
        'DE',
        'FL',
        'GA',
        'HI',
        'IA',
        'ID',
        'IL',
        'IN',
        'KS',
        'KY',
        'LA',
        'MA',
        'MD',
        'ME',
        'MI',
        'MN',
        'MO',
        'MS',
        'MT',
        'NC',
        'NE',
        'NH',
        'NJ',
        'NM',
        'NV',
        'NY',
        'ND',
        'OH',
        'OK',
        'OR',
        'PA',
        'RI',
        'SC',
        'SD',
        'TN',
        'TX',
        'UT',
        'VT',
        'VA',
        'WA',
        'WI',
        'WV',
        'WY'
    ];
    usPhoneMask = ['(', /[1-9]/, /\d/, /\d/, ')', ' ', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];

    contactsData: any;
    contactsColDef = [
        {headerName: 'Name ✎', field: 'name', editable: true, cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Title ✎', field: 'title', editable: true},
        {headerName: 'Email ✎', field: 'email', editable: true},
        {headerName: 'Work ✎', field: 'workPhone', editable: true, cellEditor: 'phoneEditorComponent'},
        {headerName: 'Phone ✎', field: 'mobile', editable: true, cellEditor: 'phoneEditorComponent'},
        {headerName: 'Ext. ✎', field: 'phExt', editable: true},
        {headerName: 'Fax ✎', field: 'fax', editable: true, cellEditor: 'phoneEditorComponent'},
        {headerName: 'Address ✎', field: 'address', editable: true}
    ];

    idMask = [/[^']/, /[^']/, /[^']/, /[^']/, /[^']/, /[^']/, /[^']/, /[^']/, /[^']/, /[^']/];

    // address: "123 Test Drive"
    // contactId: 1
    // customerId: "MDOT"
    // email: "Test@Test.com"
    // fax: 234123344
    // mobile: 1232343455
    // name: "John Shaw"
    // phExt: 1233
    // title: "Manager"
    // workPhone: 1231231233

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

    // callback for grid selection
    setSelectedRow(event) {
        this.selected = event.data;
        this.contactsData = this.astuteClientService.getCustomerContacts(this.selected.customerId);
    }

    // wrappers for customer service methods
    addCustomer(customerId, name, billTo, add1, add2, city, state, zip, zip4, email, phone, phExt, fax, ref) {
        if (fax.length > 0 && fax.length < 14) {
            this.notif('Invalid fax.');
        } else if (phone.length > 0 && phone.length < 14) {
            this.notif('Invalid phone.');
        } else if (email.length > 0 &&  /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email) === false) {
            this.notif('You have entered an invalid email address!');
        } else {
            const customerData = {
                'customerId': customerId,
                'customerName': name,
                'billToDept': billTo,
                'add1': add1,
                'add2': add2,
                'city': city,
                'state': state,
                'zip': zip,
                'ziplast4': zip4,
                'email': email,
                'phone': phone,
                'phExt': phExt,
                'fax': fax
            };
            this.astuteClientService.createCustomer(customerData).then((data) => {
                if (data) {
                    this.refreshData();
                    ref.close();
                } else {
                    this.notif('Customer Creation Failed, Check Input Fields');
                }
            }, (reason) => {
                this.notif('Add customer failed: ' + reason);
            });
        }
    }
    editCustomer(id, name, billTo, add1, add2, city, state, zip, zip4, email, phone, phExt, fax, ref) {
        if (fax.length > 0 && fax.length < 14) {
            this.notif('Invalid fax.');
        } else if (phone.length > 0 && phone.length < 14) {
            this.notif('Invalid phone.');
        } else if (email.length > 0 && /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email) === false) {
            this.notif('You have entered an invalid email address!');
        } else {
            const customerData = {
                'customerId': id,
                'customerName': name,
                'billToDept': billTo,
                'add1': add1,
                'add2': add2,
                'city': city,
                'state': state,
                'zip': zip,
                'ziplast4': zip4,
                'email': email,
                'phone': phone,
                'phExt': phExt,
                'fax': fax
            };

            this.astuteClientService.updateCustomer(id, customerData).then((data) => {
                if (data) {
                    ref.close();
                }
                this.refreshData();
            }, (reason) => {
                this.notif('Update customer failed: ' + reason);
            });
        }
    }
    deleteCustomer(customerId) {
        if (customerId) {
            if (confirm('Are you sure you want to delete customer, ' + customerId)) {
                this.astuteClientService.deleteCustomer(customerId).then((data) => {
                    if (data) {
                        console.log('Customer, ' + customerId + ' successfully deleted');
                        this.refreshData();
                    } else {
                        this.notif('Error in deleting; Customer, ' + customerId + ' has not been deleted');
                    }
                });
            }
        } else {
            this.notif('Choose a customer first!');
        }
    }

    // wrappers for contact service methods (only inline editing)
    createEmptyContact(name: string) {
        const newContactData = {
            address: '',
            customerId: this.selected.customerId,
            email: '',
            fax: '',
            mobile: '',
            name: name,
            phExt: '',
            title: '',
            workPhone: ''
        };
        console.log(newContactData);
        this.astuteClientService.createCustomerContact(newContactData).then ((data) => {
            if (!data) {
                this.notif('Contact Creation Failed, Check Input Fields');
            } else {
                this.refreshContactData(this.selected.customerId);
            }
        }, (reason) => {
            this.notif('Create customer failed: ' + reason);
        });
    }
    deleteContact() {
        const selectedNodes = this.contactGridApi.getSelectedNodes();
        if (selectedNodes.length > 0) {
            if (confirm('Are you sure?')) {
                const selec = selectedNodes.map(node => node.data)[0];
                this.astuteClientService.deleteCustomerContact(selec.customerId, selec.contactId).then((data) => {
                    if (data) {
                        this.refreshContactData(selec.customerId);
                    } else {
                        this.notif('Contact Deletion Failed, Check Input Fields');
                    }
                }, (reason) => {
                    this.notif('Delete customer failed: ' + reason);
                });
            }
        } else {
            this.notif('Choose a contact first!');
        }
    }

    // for inline updating
    updateRow(event) {
        const eventData = event.data;
        console.log(eventData);
        if (eventData.fax.length > 0 && eventData.fax.length < 14) {
            this.refreshData();
            this.notif('Invalid fax.');
        } else if (eventData.phone.length > 0 && eventData.phone.length < 14) {
            this.refreshData();
            this.notif('Invalid phone.');
        } else if (eventData.email.length > 0 && /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(eventData.email) === false) {
            this.refreshData();
            this.notif('You have entered an invalid email address!');
        } else {
            this.astuteClientService.updateCustomer(eventData.customerId, eventData).then((data) => {
                if (!data) {
                    this.refreshData();
                    this.notif('Customer Updating Failed, Check Input Fields');
                }
            }, (reason) => {
                this.notif('Update customer failed: ' + reason);
            });
        }
    }
    updateContactRow(event) {
        console.log(event);

        const eventData = event.data;
        // if (eventData.fax % 10 < 14) {
        //     this.notif('Invalid fax.');
        // } else if (eventData.mobile % 10 < 14) {
        //     this.notif('Invalid phone.');
        // } else if (eventData.workPhone % 10 < 14) {
        //     this.notif('Invalid work phone.');
        // } else
        if (eventData.email.length > 0 && /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(eventData.email) === false) {
            this.contactsData = this.astuteClientService.getCustomerContacts(eventData.customerId);
            this.notif('You have entered an invalid email address!');
        } else {
            this.astuteClientService.updateCustomerContact(eventData.customerId, eventData).then((data) => {
                if (!data) {
                    this.contactsData = this.astuteClientService.getCustomerContacts(eventData.customerId);
                    // this.notif('Customer Updating Failed, Check Input Fields');
                }
            }, (reason) => {
                this.notif('Update customer failed: ' + reason);
            });
        }
    }

    // opening and closing modal-form components
    open(ref) {
        // this.getSelectedRows();
        ref.open();
        if (this.gridColumnApi) {
            this.gridColumnApi.autoSizeAllColumns();
        }
        if (this.contactColumnApi) {
            this.contactColumnApi.autoSizeAllColumns();
        }
    }
    close(ref) {
        ref.close();
    }

    // refreshes corresponding data
    refreshData() {
        this.rowData = this.astuteClientService.getCustomers();
        this.selected = null;
        this.astuteClientService.getCustomers().then((data) => {
            this.customers = data;
        });
    }
    refreshContactData(customerId) {
        this.contactsData = this.astuteClientService.getCustomerContacts(customerId);
        this.contactColumnApi.autoSizeColumns();
    }

    // on each grid ready: sets api's and enable auto-resizing
    onGridReady(evt) {
        this.gridApi = evt.api;
        this.gridColumnApi = evt.columnApi;
    }
    onContactGridReady(evt) {
        this.contactGridApi = evt.api;
        this.contactColumnApi = evt.columnApi;
    }
    resizeColumns(evt) {
        evt.columnApi.autoSizeAllColumns();
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }

}
