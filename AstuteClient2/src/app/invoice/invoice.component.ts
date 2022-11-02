import {Component, OnInit} from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {PrintInvoiceService} from '../services/print-invoice.service';
import {ToastManagerService} from '../services/toast-manager/toast-service.service';
import {NumberFormatterComponent} from '../ag-grid-components/number-formatter/number-formatter.component';
import {SoQtyFormatterComponent} from '../ag-grid-components/so-qty-formatter/so-qty-formatter.component';
import {InDetQtyEditorComponent} from '../ag-grid-components/in-det-qty-editor/in-det-qty-editor.component';

declare var $: any;

@Component({
    selector: 'app-invoice',
    templateUrl: './invoice.component.html',
    styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {
    loggedIn: boolean;

    gridApi;
    gridColumnApi;
    detailGridApi;
    detailColumnApi;

    chosenCustomerID: any = 0;
    chosenInv: any = 0;
    source;

    chosenPo;
    correspondingPos = [];
    generatedInvoiceNumber = '';

    serviceTypes = [];
    serviceNames = [];
    rateTypes = [];
    rateNames = [];
    customers;
    pos = [];
    allPODetails = [];

    newInDetails = [];

    selectedInDetails;
    selectedPO;
    selectedBillAmt = 0;
    selectedPODetails = [];

    // poDetails = [];

    columnDefs = [
        {headerName: 'Customer', field: 'customerId'},
        {headerName: 'Invoice Number', field: 'invoiceNumber'},
        {headerName: 'Date', field: 'invoiceDate'},
        {headerName: 'Sales Order Number', field: 'poNum'},
        {headerName: 'Change Order Number', field: 'changeOrderNum'},
        {headerName: 'Outstanding Balance', field: 'outstandingBalance', cellRenderer: 'numberFormatterComponent'},
        {headerName: 'Bill Amount', field: 'billAmt', cellRenderer: 'numberFormatterComponent'},
        {headerName: 'Notes ✎', field: 'specialNotes',
            editable: (node => node.data.invoiceStatus === 1), cellEditor: 'agLargeTextCellEditor'}
    ];
    gridOptions = {
        rowClassRules: {
            'text-danger': (node => node.data.invoiceStatus === 1),
            'text-primary': (node => node.data.invoiceStatus === 2),
            'text-warning': (node => node.data.invoiceStatus === 3)
        }
    };
    frameworkComponents =  {
        numberFormatterComponent: NumberFormatterComponent,
        soQtyFormatterComponent: SoQtyFormatterComponent,
        inDetQtyEditorComponent: InDetQtyEditorComponent
    };
    detailColumnDefs = [
        {headerName: '#', field: 'lineItemNum'},
        {headerName: 'PO Detail', field: 'poDetailName'},
        {headerName: 'Description ✎*', field: 'desc',
            editable: (_ => (this.chosenInv && this.chosenInv.invoiceStatus === 1)), cellEditor: 'agLargeTextCellEditor'},
        {headerName: 'Fee Type *', field: 'rateTypeName',
            editable: (node => (node.data.poLineItemNum === -1 && this.chosenInv && this.chosenInv.invoiceStatus === 1)),
            cellEditor: 'agSelectCellEditor', cellEditorParams: {values: this.rateNames}},
        {headerName: 'Service Type', field: 'serviceTypeName'},
        {headerName: 'Qty or Hours ✎*', field: 'qty',
            editable: (_ => (this.chosenInv && this.chosenInv.invoiceStatus === 1)),
            cellEditor: 'inDetQtyEditorComponent', cellEditorParams: (node => ({remainingQty: node.data.draftRemainingQty})),
            cellRenderer: 'soQtyFormatterComponent', cellRendererParams: (node => ({feeTypeId: node.data.feeTypeId}))},
        {headerName: 'Remaining Qty', field: 'remainingQty',
            cellRenderer: 'soQtyFormatterComponent', cellRendererParams: (node => ({feeTypeId: node.data.feeTypeId}))},
        {headerName: 'Fee *', field: 'fee',
            editable: (node => (node.data.poLineItemNum === -1 && this.chosenInv && this.chosenInv.invoiceStatus === 1)),
            cellRenderer: 'numberFormatterComponent'}
    ];



    constructor(protected astuteClientService: AstuteClientService,
                protected printService: PrintInvoiceService,
                protected toastService: ToastManagerService) {
    }

    customerDropdownChange(index) {
        this.chosenCustomerID = this.customers[index].customerId;
        this.correspondingPos = this.pos.filter((po) => {
            // console.log(po);
            return po.customerId === this.chosenCustomerID && !po.oneInvInDraft && po.isFinal && !po.fulfilled;
        });
    }
    poDropdownChange(ponum) {
        this.pos.forEach((po) => {
            if (po.ponum === ponum) {
                this.chosenPo = po;
            }
        })
        // this.astuteClientService.getPODetail(ponum).then((data) => {
        //     if (data) {
        //         // lineItemNo, feeTypeId, serviceTypeId, serviceDesc, fee, remainingQty
        //         this.poDetails = data;
        //         this.poDetails[-1] = {
        //             'lineItemNo': -1,
        //             'feeTypeId': 1,
        //             'serviceTypeId': 1,
        //             'serviceDesc': 'Out of Pocket Expenses',
        //             'fee': 0,
        //             'remainingQty': 0
        //         };
        //     } else {
        //         this.notif('get PO detail failed!');
        //     }
        // });
        this.astuteClientService.generateInvoiceNumber(ponum).then((data) => {
            if (data) {
                this.generatedInvoiceNumber = data;
            } else {
                this.notif('gen inv num failed!');
            }
        });
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
            this.refreshPOs();
            this.refreshData();
        } else {
            this.loggedIn = false;
        }
    }

    refreshPOs() {
        this.astuteClientService.getPOs().then((data) => {
            if (data) {
                this.pos = data;
                this.pos.forEach((po) => {
                    this.astuteClientService.getPODetail(po.ponum).then((details) => {
                        if (details) {
                            details.forEach((detail) => {
                                detail.tempRemainingQty = detail.remainingQty;
                                this.allPODetails.push(detail);
                            });
                            // console.log(this.allPODetails);
                        } else {
                            this.notif('Get PO Detail for ' + po.ponum + ' failed!');
                        }
                    }, (reason) => {
                        this.notif('Get PO Detail for ' + po.ponum + ' failed: ' + reason);

                    });
                });
            } else {
                this.notif ('Get PO failed');
            }
        }, (reason) => {
            this.notif('Get SOs Failed: ' + reason);
        });
    }

    refreshData() {
        this.astuteClientService.getInvoices().then((data) => {
            if (data) {
                this.source = data;
            } else {
                this.notif('Get Invoices failed!');
            }
        });
    }
    refreshDetailsOfSelected() {
        this.setSelectedRow(null);
    }

    getPODetails(poIndex) {
        const ponum = this.pos[poIndex].ponum;
        this.selectedInDetails = this.astuteClientService.getPODetail(ponum);
    }

    updateRow(event) {
        const eventData = event.data;
        console.log(eventData);
        this.astuteClientService.updateInvoice(eventData.invoiceNumber, eventData).then((data) => {
            if (!data) {
                this.refreshData();
                this.notif('Invoice Updating Failed, Check Input Fields');
            }
        }, (reason) => {
            this.notif('Update invoice failed: ' + reason);
        });
        // this.refreshData();
    }
    updateDetailRow(event) {
        // console.log(eventData);
        event.data.feeTypeId = this.getFeeIdFromName(event.data.rateTypeName);
        const eventData = event.data;
        this.astuteClientService.updateInvoiceDetail(eventData.invoiceNum, eventData.lineItemNum, eventData).then((data) => {
            if (!data) {
                this.notif('Detail Updating Failed, Check Input Fields');
            } else {
                this.updateSelectedBillAmt();
            }
            this.refreshDetailsOfSelected();
        }, (reason) => {
            this.notif('Update Detail failed: ' + reason);
        });
    }

    setSelectedRow(event) {
        if (event) {
            this.chosenInv = event.data;
        }
        const editable = (this.chosenInv && this.chosenInv.invoiceStatus === 1);
        // this.detailColumnDefs[2].editable = editable;
        // this.detailColumnDefs[5].editable = editable;
        // this.detailColumnDefs[6].editable = editable;

        this.selectedBillAmt = this.chosenInv.billAmt;
        this.selectedPODetails = this.allPODetails.filter((detail) => {
            return (detail.ponum === this.chosenInv.poNum);
        });
        this.selectedInDetails = this.astuteClientService.getInvoiceDetail(this.chosenInv.invoiceNumber).then((data) => {
            if (data) {
                data.forEach((invDetail) => {
                    if (invDetail.poLineItemNum === -1) {
                        invDetail.remainingQty = '-';
                        invDetail.poDetailName = 'Out of Pocket Expenses';
                    } else {
                        if (this.chosenInv.invoiceStatus === 1) {
                            invDetail.remainingQty = invDetail.draftRemainingQty;
                        } else {
                            const tempPo = this.selectedPODetails[invDetail.poLineItemNum - 1];
                            if (tempPo) {
                                invDetail.remainingQty = tempPo.remainingQty;
                            } else {
                                invDetail.remainingQty = '';
                            }
                        }

                        const temp = this.selectedPODetails[invDetail.poLineItemNum - 1];
                        if (temp) {
                            invDetail.poDetailName = this.selectedPODetails[invDetail.poLineItemNum - 1].serviceDesc;
                        } else {
                            invDetail.poDetailName = '';
                        }
                    }

                    invDetail.serviceTypeName = this.serviceNames[invDetail.serviceTypeId - 1];
                    invDetail.rateTypeName = this.rateNames[invDetail.feeTypeId - 1];
                });
                this.updateSelectedBillAmt();
                return data;
            } else {
                this.notif('get Inv detail failed!');
            }
        });
        this.pos.forEach((po) => {
            if (po.ponum === this.chosenInv.poNum) {
                this.selectedPO = po;
            }
        });
    }

    onSelectedCellChange(row: number, col: string, value) {
        // this.selectedInDetails[row][col] = value;
        // console.log(this.selectedInDetails);
    }

    onNewCellChange(row: number, col: string, value) {
        this.newInDetails[row][col] = value;
        console.log(this.newInDetails);
    }

    pushOntoSelectedDetail(invoiceNum, lineItemNum, poLineItemNum, serviceTypeId, desc, qty, fee) {
    //     this.selectedInDetails.push({
    //         'invoiceNum': invoiceNum,
    //         'lineItemNum': lineItemNum,
    //         'poLineItemNum': poLineItemNum,
    //         'serviceTypeId': serviceTypeId,
    //         'desc': desc,
    //         'qty': +qty,
    //         'fee': +fee
    //     });
    }

    pushOntoNewDetail(invoiceNum, lineItemNum, poLineItemNum, feeTypeId, serviceTypeId, desc, qty, fee, remainingQty, poNum) {
        this.newInDetails.push({
            'invoiceNum': invoiceNum,
            'lineItemNum': lineItemNum,
            'poLineItemNum': poLineItemNum,
            'feeTypeId': feeTypeId,
            'serviceTypeId': serviceTypeId,
            'desc': desc,
            'qty': +qty,
            'fee': +fee,
            'remainingQty': +remainingQty,
            'poNum': poNum
        });
        console.log(this.newInDetails);
    }

    updateNewBillAmt() {
        let tot = 0;
        this.newInDetails.forEach((d) => {
            tot += +d.qty * +d.fee;
        });
        // this.newBillAmt = tot;
    }
    updateSelectedBillAmt() {
        this.selectedBillAmt = 0;
        if (this.selectedInDetails) {
            this.selectedInDetails.then((data) => {
                data.forEach((d) => {
                    this.selectedBillAmt += +d.qty * +d.fee;
                });
            });
        }
    }
    // updateSelectedDetailRemainingQty(poLineItemNum: number) {
    //     this.selectedInDetails.then((data) => {
    //         if (data) {
    //             let newRemainingQty = this.selectedPODetails[poLineItemNum - 1].remainingQty;
    //             data.forEach(invDetail => {
    //                 if (invDetail.poLineItemNum.toString() === poLineItemNum.toString()) {
    //                     newRemainingQty = newRemainingQty - invDetail.qty;
    //                 }
    //             });
    //             this.selectedPODetails[poLineItemNum - 1].tempRemainingQty = newRemainingQty;
    //         }
    //     });
    // }

    printInvoice() {
        this.printService.printPDF (this.chosenInv.invoiceNumber);
    }

    // getSelectedRows() {
    //     const selectedNodes = this.gridApi.getSelectedNodes();
    //     if (selectedNodes.length) {
    //         this.chosenInv = selectedNodes.map(node => node.data)[0];
    //         // console.log (this.chosenInv);
    //         this.selectedBillAmt = +(this.chosenInv.billAmt.replace(',', '').replace('$', ''));
    //         this.astuteClientService.getPODetail(this.chosenInv.poNum).then((poDetails) => {
    //             if (poDetails) {
    //                 this.selectedPODetails = poDetails;
    //                 this.astuteClientService.getInvoiceDetail(this.chosenInv.invoiceNumber).then((invoiceDetails) => {
    //                     if (invoiceDetails) {
    //                         this.selectedInDetails = invoiceDetails;
    //                         this.selectedInDetails.forEach((invDetail) => {
    //                             const tempPo = this.selectedPODetails.filter((po) => {
    //                                 // console.log (po.lineItemNo + " and " + invDetail.poLineItemNum);
    //                                 return po.lineItemNo === invDetail.poLineItemNum;
    //                             })[0];
    //                             if (tempPo) {
    //                                 invDetail.remainingQty = tempPo.remainingQty;
    //                             }
    //                         });
    //                     } else {
    //                         this.notif("get Inv detail failed!");
    //                     }
    //                 });
    //             } else {
    //                 this.notif("get PO detail failed!")
    //             }
    //         });
    //         this.pos.forEach((po) => {
    //             if (po.ponum === this.chosenInv.poNum) {
    //                 this.selectedPO = po;
    //             }
    //         });
    //     } else {
    //         this.chosenInv = null;
    //         this.selectedPODetails = [];
    //     }
    // }

    // open(content, indexPO, indexINV) {
    open(content) {
        // this.setCorrespondingCustomer();
        content.open();
        if (this.gridColumnApi) {
            this.gridColumnApi.autoSizeAllColumns();
        }
        if (this.detailColumnApi) {
            this.detailColumnApi.autoSizeAllColumns();
        }
        // this.detailDescription = ViewChild('detailDescription');
        // this.detailAmount = ViewChild('detailAmount');
        // this.detailRate = ViewChild('detailRate');
        // this.detailTotal = ViewChild('detailTotal');
        // if (indexINV) {
        //   this.chosenInv = indexINV;
        // }
        // if (indexPO) {
        //   this.chosenPo = indexPO;
        //   this.getPODetails(this.chosenPo);
        // }
        // this.modalService.open(content, { size: 'lg' }).result.then((result) => {
        //   this.closeResult = `Closed with: ${result}`;
        // }, (reason) => {
        //   this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        // });
    }
    close(content) {
        content.close();
        // this.newInDetails = [];
    }

    getCurrDate() {
        const d = new Date();
        let month = '' + (d.getMonth() + 1),
            day = '' + d.getDate();
        const year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;


        return [year, month, day].join('-');
    }

    formatDate(d: Date) {
        let month = '' + (d.getMonth() + 1),
            day = '' + d.getDate();
        const year = d.getFullYear();

        if (month.length < 2) {
            month = '0' + month;
        }
        if (day.length < 2) {
            day = '0' + day;
        }
        return [year, month, day].join('-');
    }

    deleteInvoice (invoiceNum) {
        if (confirm('Are you sure you want to delete invoice, ' + invoiceNum)) {
            this.astuteClientService.deleteInvoice(invoiceNum).then((data) => {
                if (data) {
                    console.log('Invoice, ' + invoiceNum + ' successfully deleted');
                    this.refreshData();
                    this.refreshPOs();
                } else {
                    this.notif ('Error in deleting; Invoice, ' + invoiceNum + ' has not been deleted');
                }
            });
        }
    }
    addInvoice(invoiceNumber, poNum, changeOrderNum, pmtStatus, billAmt, specialNotes, certification, status, ref) {
        // String  invoiceNumber;
        // Date invoiceDate;
        // String  poNum;
        // String  changeOrderNum;
        // int     pmtStatus;
        // Double  billAmt;
        // String  specialNotes;
        // String  certification;
        // Date pmtReceivedDate;
        const invData = {
            "invoiceNumber": invoiceNumber,
            "invoiceDate": this.formatDate(new Date()),
            "poNum": poNum,
            "changeOrderNum": changeOrderNum,
            "pmtStatus": +pmtStatus,
            "billAmt": +billAmt,
            "specialNotes": specialNotes,
            "certification": certification,
            "pmtReceivedDate": this.formatDate(new Date()),
            'invoiceStatus': status
        };
        console.log(invData);

        this.astuteClientService.createInvoice(invData)
            .catch((response) => {
                console.log("rejected: " + response);
            })
            .then((data) => {
                if (data) {
                    this.refreshData();
                    // this.addInvoiceDetail(this.newInDetails);
                    ref.close();
                } else {
                    this.notif('Invoice Creation Failed, Check Input Fields');
                }
            });
    }
    editInvoice(invoiceNumber, poNum, changeOrderNum, pmtStatus, billAmt, specialNotes, certification) {
        // String  invoiceNumber;
        // Date invoiceDate;
        // String  poNum;
        // String  changeOrderNum;
        // int     pmtStatus;
        // Double  billAmt;
        // String  specialNotes;
        // String  certification;
        // Date pmtReceivedDate;
        const invData = {
            "invoiceNumber": invoiceNumber,
            "invoiceDate": new Date(),
            "poNum": poNum,
            "changeOrderNum": changeOrderNum,
            "pmtStatus": +pmtStatus,
            "billAmt": +billAmt,
            "specialNotes": specialNotes,
            "certification": certification,
            "pmtReceivedDate": new Date()
        };

        this.astuteClientService.updateInvoice(invoiceNumber, invData)
            .catch((response) => {
                console.log("rejected: " + response);
            })
            .then((data) => {
                if (data) {
                    this.notif("invoice " + invoiceNumber + " updated!");
                    console.log("fulfilled: " + data);
                    // this.source[this.chosenInv] = invData;
                    this.refreshData();
                } else {
                    this.notif("Invoice Update Failed, Check Input Fields")
                }
            });
    }
    voidInvoice(invoiceNumber) {
        if (confirm('Are you sure you want to void invoice, ' + invoiceNumber)) {
            this.astuteClientService.voidInvoice(invoiceNumber).then((data) => {
                if (data) {
                    this.refreshData();
                    this.refreshPOs();
                } else {
                    this.notif('void invoice failed.');
                }
            });
        }
    }
    submitInvoice(invoiceNumber) {
        this.astuteClientService.submitInvoice(invoiceNumber).then((data) => {
            if (data) {
                this.refreshData();
                this.refreshPOs();
            } else {
                this.notif('submit invoice failed.');
            }
        });
    }

    // creates empty line item detail
    addEmptyDetail(poLineItemNum) {
        let emptyData;
        if (poLineItemNum === '-1') {
            emptyData = {
                desc: 'Out of Pocket Expenses',
                fee: 0,
                feeTypeId: 1,
                invoiceNum: this.chosenInv.invoiceNumber,
                // lineItemNum: 1,
                poLineItemNum: poLineItemNum,
                qty: 0,
                // remainingQty: 1,
                serviceTypeId: 6
            };
        } else {
            emptyData = {
                desc: this.selectedPODetails[poLineItemNum - 1].serviceDesc,
                fee: this.selectedPODetails[poLineItemNum - 1].fee,
                feeTypeId: this.selectedPODetails[poLineItemNum - 1].feeTypeId,
                invoiceNum: this.chosenInv.invoiceNumber,
                // lineItemNum: 1,
                poLineItemNum: poLineItemNum,
                qty: 0,
                // remainingQty: this.selectedPODetails[poLineItemNum - 1].remainingQty,
                serviceTypeId: this.selectedPODetails[poLineItemNum - 1].serviceTypeId
            };
        }

        // desc: "Design Somethign"
        // fee: 2500
        // feeTypeId: 1
        // invoiceNum: "MDO-01_DRAFT_157"
        // lineItemNum: 1
        // poLineItemNum: 1
        // qty: 1
        // remainingQty: 0
        // serviceTypeId: 2

        this.astuteClientService.createInvoiceDetail(emptyData).then((data) => {
            if (!data) {
                this.notif('Creating Invoice details failed!');
            }
            this.refreshDetailsOfSelected();
        }, (reason) => {
            this.notif('Creating Invoice details failed: ' + reason);
        });
    }
    deleteInvDetail() {
        const selectedNodes = this.detailGridApi.getSelectedNodes();
        if (selectedNodes.length > 0) {
            if (confirm('Are you sure?')) {
                const selec = selectedNodes.map(node => node.data)[0];
                this.astuteClientService.deleteInvoiceDetail(selec.invoiceNum, selec.lineItemNum).then((data) => {
                    if (data) {
                        this.refreshDetailsOfSelected();
                    } else {
                        this.notif('Invoice Detail Deletion Failed!');
                    }
                }, (reason) => {
                    this.notif('Delete Invoice Detail failed: ' + reason);
                });
            }
        } else {
            this.notif('Choose a Invoice Detail first!');
        }
    }

    // addInvoiceDetail(details) {
    //     if (details.length) {
    //         // console.log(details[0]);
    //         // if (details[0].poLineItemNum !== -1) {
    //         this.astuteClientService.createInvoiceDetail(details[0]).then((data) => {
    //             if (data) {
    //                 details.splice(0, 1);
    //                 this.addInvoiceDetail(details);
    //             } else {
    //                 this.notif('add inv detail failed');
    //             }
    //         });
    //         // } else {
    //         // desc
    //         // fee
    //         // feeTypeId
    //         // invoiceNum
    //         // lineItemNum
    //         // poLineItemNum
    //         // qty
    //         // remainingQty
    //         // serviceTypeId
    //         // poNum
    //
    //         // const data = {
    //         //     // 'lineItemNo': details[0].,
    //         //     'poNum': details[0].poNum,
    //         //     'serviceDesc': details[0].desc,
    //         //     'feeTypeId': details[0].feeTypeId,
    //         //     'serviceTypeId': details[0].serviceTypeId,
    //         //     'qty': +details[0].qty,
    //         //     'fee': +details[0].fee,
    //         //     'remainingQty': +details[0].fee * +details[0].qty
    //         // };
    //         // this.astuteClientService.createPODetail(data).then((d) => {
    //         //     if (d) {
    //         //         console.log (d);
    //         //     } else {
    //         //         this.notif('create custom PO failed.');
    //         //     }
    //         // });
    //         // }
    //     } else {
    //         this.newInDetails = [];
    //     }
    // }


    getPerc(amt, total): number {
        return Math.floor(((amt) / total) * 100);
    }
    getRangeMax(total, num) {
        return Math.floor(total / num);
    }


    // setCorrespondingCustomer() {
    //     this.correspondingCustomer = this.customers.filter((customer, index, array) => {
    //         return customer.customerId === this.chosenInv.customerId;
    //     });
    // }

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
