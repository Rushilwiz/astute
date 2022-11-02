import {Component, OnInit, ViewChild} from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {formatCurrency, formatNumber} from '@angular/common';
import {ToastManagerService} from '../services/toast-manager/toast-service.service';
import {NumberFormatterComponent} from '../ag-grid-components/number-formatter/number-formatter.component';
import {EmptyErrorEditorComponent} from "../ag-grid-components/empty-error-editor/empty-error-editor.component";
import {SoQtyEditorComponent} from "../ag-grid-components/so-qty-editor/so-qty-editor.component";
import {SoQtyFormatterComponent} from "../ag-grid-components/so-qty-formatter/so-qty-formatter.component";

@Component({
    selector: 'app-sales-order',
    templateUrl: './sales-order.component.html',
    styleUrls: ['./sales-order.component.css']
})
export class SalesOrderComponent implements OnInit {
    loggedIn: boolean;

    // both of the grid api's
    gridApi;
    gridColumnApi;
    detailGridApi;
    detailColumnApi;

    // one time fetch meta-data
    customers;
    serviceTypes;
    serviceNames = [];
    rateTypes = [];
    rateNames = [];

    // data for SO grid
    rowData: any;
    columnDefs = [
        {headerName: 'Project Number ✎', field: 'astuteProjectNumber', editable: (node => !node.data.isFinal), cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'SO Number', field: 'ponum'},
        // {headerName: 'Customer ID', field: 'customerId'},
        {headerName: 'Customer Name', field: 'customerName'},
        {headerName: 'Contract Number ✎', field: 'contractNum', editable: (node => !node.data.isFinal), cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'SO Title ✎', field: 'title', editable: (node => !node.data.isFinal), cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Contract Amount', field: 'contractAmt', cellRenderer: 'numberFormatterComponent'},
        // {headerName: 'Contract Amount', field: 'contractAmt'},
        {headerName: 'SO Date ✎', field: 'podate', editable: (node => !node.data.isFinal), cellEditor: 'emptyErrorEditorComponent'},
        {headerName: '# of Invoices', field: 'invoiceSequence'},
        {headerName: 'notes ✎', field: 'notes', editable: (node => !node.data.isFinal), cellEditor: 'agLargeTextCellEditor'}
        // {headerName: 'oneInvInDraft', field: 'oneInvInDraft'}
    ];

    gridOptions = {
        rowClassRules: {
            'text-danger': function (params) {
                return !params.data.isFinal;
            },
            'text-primary': function (params) {
                return params.data.isFinal;
            },
        }
    };

    frameworkComponents =  {
        emptyErrorEditorComponent: EmptyErrorEditorComponent,
        numberFormatterComponent: NumberFormatterComponent,
        soQtyEditorComponent: SoQtyEditorComponent,
        soQtyFormatterComponent: SoQtyFormatterComponent
    };

    selected = null; // the selected SO row

    // data for SO detail grid
    selectedPODetail;
    detailColumnDefs = [
        {headerName: '#', field: 'lineItemNo'},
        {headerName: 'Description ✎', field: 'serviceDesc', editable: (_ => (this.selected && !this.selected.isFinal)),
            cellEditor: 'emptyErrorEditorComponent'},
        {headerName: 'Rate Type ✎', field: 'rateTypeName', editable: (_ => (this.selected && !this.selected.isFinal)),
            cellEditor: 'agSelectCellEditor', cellEditorParams: {values: this.rateNames}},
        {headerName: 'Service Type ✎', field: 'serviceTypeName', editable: (_ => (this.selected && !this.selected.isFinal)),
            cellEditor: 'agSelectCellEditor', cellEditorParams: {values: this.serviceNames}},
        {headerName: 'Qty or Hours *', field: 'qty',
            editable: (node => (node.data.feeTypeId === 2 && this.selected && !this.selected.isFinal)),
            cellEditor: 'soQtyEditorComponent', cellEditorParams: (node => ({feeTypeId: node.data.feeTypeId})),
            cellRenderer: 'soQtyFormatterComponent', cellRendererParams: (node => ({feeTypeId: node.data.feeTypeId}))},
        {headerName: 'Rate ($) ✎', field: 'fee', editable: (_ => (this.selected && !this.selected.isFinal)),
            cellRenderer: 'numberFormatterComponent'}
    ];
    contractAmount = 0; // used to show total amount

    constructor(private astuteClientService: AstuteClientService, protected toastService: ToastManagerService) {
    }

    ngOnInit() {
        if (localStorage.getItem('SESSION_ID') && localStorage.getItem('SESSION_USER')) {
            this.loggedIn = true;
            this.astuteClientService.getServiceTypes().then((d) => {
                if (d) {
                    this.serviceTypes = d;
                    this.serviceTypes.forEach((type) => {
                        this.serviceNames.push(type.serviceTypeDesc);
                    });
                    // console.log(this.serviceNames);
                } else {
                    this.notif ('Get service types failed');
                }
            }, reason => {
                this.notif('Get service type failed: ' + reason);
            });
            this.astuteClientService.getRateTypes().then((d) => {
                if (d) {
                    this.rateTypes = d;
                    this.rateTypes.forEach((type) => {
                        this.rateNames.push(type.feeTypeDesc);
                    });
                } else {
                    this.notif ('Get rate types failed');
                }
            }, reason => {
                this.notif('Get rate type failed: ' + reason);
            });
            this.astuteClientService.getCustomers().then((customers) => {
                if (customers) {
                    this.customers = customers;
                } else {
                    this.notif('Get Customers Failed!');
                }
            }, (reason) => {
                this.notif('Get Customers Failed: ' + reason);
            });
            this.refreshData();
        } else {
            this.loggedIn = false;
        }
    }

    // callback for grid selection
    setSelectedRow(event) {
        if (event) {
            this.selected = event.data;
        }

        this.selectedPODetail = this.astuteClientService.getPODetail(this.selected.ponum).then((data) => {
            if (data) {
                data.forEach((row) => {
                    row.poNum = row.ponum;
                    row.serviceTypeName = this.serviceNames[row.serviceTypeId - 1];
                    row.rateTypeName = this.rateNames[row.feeTypeId - 1];
                });
                this.updateContractAmt();
                return data;
            } else {
                this.notif('Get SO detail failed!');
            }
        }, (reason) => {
            this.notif('Get SO detail failed: ' + reason);
        });
    }

    // for inline updating
    updateRow(event) {
        const eventData = event.data;
        // console.log(eventData);

        this.astuteClientService.updatePO(eventData.poNum, eventData).then((data) => {
            if (!data) {
                this.notif('SO updating failed, check input fields');
                this.refreshData();
            }
        }, (reason) => {
            this.notif('Update SO failed: ' + reason);
        });
        // this.refreshData();
    }
    updateDetailRow(event) {
        const eventData = event.data;
        // console.log(eventData);
        event.data.serviceTypeId = this.getServiceIdFromName(event.data.serviceTypeName);
        event.data.feeTypeId = this.getFeeIdFromName(event.data.rateTypeName);
        if (event.data.feeTypeId === 1 && event.data.qty > 1) { // fixed fee and qty > 1
            this.notif('Cannot have a quantity greater than 1 for fixed fee rate type.');
            this.refreshDetailsOfSelected();
        } else {
            // console.log(event.data.serviceTypeId);
            this.astuteClientService.updatePODetail(eventData.poNum, eventData.lineItemNo, eventData).then((data) => {
                if (!data) {
                    this.notif('SO Detail updating failed, check input fields');
                } else {
                    this.updateContractAmt();
                }
                this.refreshDetailsOfSelected();
                // this.refreshData();
            }, (reason) => {
                this.notif('Update SO Detail failed: ' + reason);
            });
            // this.refreshData();
        }
    }

    // wrappers for PO service methods
    addPo(projNum, ponum, podate, customerid, contractnum, contractamt, title, notes, ref) {
        const poData = {
            'astuteProjectNumber': projNum,
            'poNum': ponum,
            'podate': podate,
            'customerId': customerid,
            'contractNum': contractnum,
            'contractAmt': contractamt,
            'title': title,
            'notes': notes
        };
        // console.log(poData);
        this.astuteClientService.createPO(poData).then((data) => {
            if (data) {
                this.refreshData();
                // this.addPODetail(this.newPODetail);
                ref.close();
            } else {
                this.notif('SO Creation failed, check input fields');
            }
        }, (reason) => {
            this.notif('Add SO failed for ' + reason);
        });
    }
    editPo(projNum, ponum, podate, contractnum, contractamt, title, notes, ref) {
        const poData = {
            'astuteProjectNumber': projNum,
            'poNum': ponum,
            'podate': podate,
            'contractNum': contractnum,
            'contractAmt': contractamt,
            'title': title,
            'notes': notes,
        };
        // console.log(poData);
        this.astuteClientService.updatePO(ponum, poData).then((data) => {
            if (data) {
                this.refreshData();
                // this.editPODetail(this.selectedPODetail);
                ref.close();
            } else {
                this.notif('SO updating failed, check input fields');
            }
        }, (reason) => {
            this.notif('Update SO failed for ' + reason);
        });
    }
    finalizePO(ponum) {
        this.astuteClientService.finalizePO(ponum).then((data) => {
            if (data) {
                this.refreshData();
                this.notif('SO is now final and ready to be used, you can\'t edit it anymore!');
            } else {
                this.notif('Finalizing SO failed, check input fields');
            }
        }, reason => {
            this.notif('Finalizing SO failed: ' + reason);
        });
    }
    deletePO(ponum) {
        if (confirm('Are you sure?')) {
            this.astuteClientService.deletePO(ponum).then((data) => {
                if (data) {
                    this.refreshData();
                } else {
                    this.notif('Deleting SO failed, check input fields');
                }
            }, (reason) => {
                this.notif('Deleting SO failed: ' + reason);

            });
        }
    }

    // wrappers for SO detail service methods
    addEmptyDetail(desc: string) {
        const emptyData = {
            fee: 0,
            feeTypeId: 1,
            // lineItemNo: 7,
            poNum: this.selected.poNum,
            qty: 1,
            remainingQty: 1,
            serviceDesc: desc,
            serviceTypeId: 1
        };

        // String poNum;
        // int lineItemNo;
        // String serviceDesc;
        // int feeTypeId;
        // Double qty;
        // Double fee;
        // int serviceTypeId;
        // Double remainingQuantity;
        this.astuteClientService.createPODetail(emptyData).then((data) => {
            if (!data) {
                this.notif('Creating SO detailed failed!');
            }
            this.refreshDetailsOfSelected();
        }, (reason) => {
            this.notif('Creating SO detailed failed: ' + reason);
        });
    }
    deletePODetail() {
        const selectedNodes = this.detailGridApi.getSelectedNodes();
        if (selectedNodes.length > 0) {
            if (confirm('Are you sure?')) {
                const selec = selectedNodes.map(node => node.data)[0];
                this.astuteClientService.deletePODetail(selec.poNum, selec.lineItemNo).then((data) => {
                    if (data) {
                        this.refreshDetailsOfSelected();
                    } else {
                        this.notif('SO Detail Deletion Failed');
                    }
                }, (reason) => {
                    this.notif('Delete SO failed: ' + reason);
                });
            }
        } else {
            this.notif('Choose a SO first!');
        }
    }

    // open and closing modal-form components
    open(ref) {
        // this.getSelectedRows();
        ref.open();
        if (this.gridColumnApi) {
            this.gridColumnApi.autoSizeAllColumns();
        }
        if (this.detailColumnApi) {
            this.detailColumnApi.autoSizeAllColumns();
        }
    }
    close(ref) {
        // this.newPODetail = [];
        // this.selectedPODetail = [];
        ref.close();
    }

    // refreshing data methods
    refreshData() {
        // this.astuteClientService.getPOs().then((data) => {
        //     if (data) {
        //         // this.pos = data;
        //         this.rowData = data;
        //         this.rowData.forEach((row) => {
        //             row.customerName = this.getCustomerNameFromId(row.customerId);
        //             row.contractAmtString = formatCurrency(row.contractAmt, 'en-US', '$', 'USD');
        //             row.poNum = row.ponum;
        //         });
        //         this.selected = null;
        //         this.updateNewContractAmt();
        //         this.updateEditContractAmt();
        //     } else {
        //         this.notif('Get SO\'s Failed!');
        //     }
        // }, (reason) => {
        //     this.notif('Get SO\'s Failed: ' + reason);
        // });

        const selectedRow = (this.gridApi ? this.gridApi.getSelectedRows()[0] : null);
        this.rowData = this.astuteClientService.getPOs().then((data) => {
            if (data) {
                // this.pos = data;
                data.forEach((row) => {
                    row.customerName = this.getCustomerNameFromId(row.customerId);
                    row.poNum = row.ponum;
                });
                this.selected = null;
                this.contractAmount = 0;
                return data;
            } else {
                this.notif('Get SO\'s Failed!');
            }
        }, (reason) => {
            this.notif('Get SO\'s Failed: ' + reason);
        });
        if (selectedRow) {
            // this.gridApi.setSelectedRow(selectedRow);
        }
    }
    refreshDetailsOfSelected() {
        this.setSelectedRow(null);
    }
    updateContractAmt() {
        this.contractAmount = 0;
        if (this.selectedPODetail) {
            this.selectedPODetail.then((detail) => {
                detail.forEach((d) => {
                    this.contractAmount += +d.qty * +d.fee;
                });
            });
        }
    }

    // helper methods
    getCurrDate() {
        const d = new Date();
        return this.formatDate(d);
    }
    formatDate(d: Date) {
        let month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) {
            month = '0' + month;
        }
        if (day.length < 2) {
            day = '0' + day;
        }
        return [year, month, day].join('-');
    }
    getCustomerNameFromId(customerId) {
        let name = '';
        this.customers.forEach((customer) => {
            if (customer.customerId === customerId) {
                name = customer.customerName;
            }
        });
        return name;
    }
    getServiceIdFromName(name) {
        let id = -1;
        this.serviceTypes.forEach((type) => {
            console.log(type.serviceTypeDesc + ' ' + name);
            if (type.serviceTypeDesc === name) {
                id = type.serviceTypeId;
            }
        });
        return id;
    }
    getFeeIdFromName(name: any) {
        let id = -1;
        this.rateTypes.forEach((type) => {
            if (type.feeTypeDesc === name) {
                id = type.feeTypeId;
            }
        });
        return id;
    }

    // ag grid callbacks
    onGridReady(evt) {
        this.gridApi = evt.api;
        this.gridColumnApi = evt.columnApi;
    }
    onDetailGridReady(evt) {
        this.detailGridApi = evt.api;
        this.detailColumnApi = evt.columnApi;
    }
    resizeColumns(evt) {
        evt.columnApi.autoSizeAllColumns();
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }

}
