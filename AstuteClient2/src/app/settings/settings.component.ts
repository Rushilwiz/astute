import { Component, OnInit } from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {Router} from '@angular/router';
import {ToastManagerService} from "../services/toast-manager/toast-service.service";

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
    serviceTypeData: Promise<any>;
    columnDefs = [
        {headerName: 'Service Type ID', field: 'serviceTypeId'},
        {headerName: 'Service Type Description', field: 'serviceTypeDesc', editable: true},
    ];
    gridApi;
    gridColumnApi;

    constructor(private astuteClientService: AstuteClientService,
                private router: Router,
                private toastService: ToastManagerService) {
    }

    ngOnInit() {
        this.refreshServiceTypeData();
    }

    updateServiceTypeRow(evt) {
        const data = evt.data;
        console.log(data);
        this.astuteClientService.updateServiceType(data.serviceTypeId, data).then((d) => {
            if (!d) {
                this.notif('Service Type updating failed, check input fields');
            }
            this.refreshServiceTypeData();
        }, (reason) => {
            this.notif('Update Service Type failed: ' + reason);
        });
    }

    addEmptyServiceTypes() {
        const data = {'serviceTypeName': ''};
        this.astuteClientService.createServiceType(data).then((d) => {
            if (!d) {
                this.notif('Create Service Type Failed!');
            }
            this.refreshServiceTypeData();
        }, (reason) => {
            this.notif('Create Service Type Failed: ' + reason);
        });
    }

    open(ref) {
        ref.open();
    }
    close(ref) {
        this.refreshServiceTypeData();
        ref.close();
    }

    refreshServiceTypeData() {
        this.serviceTypeData = this.astuteClientService.getServiceTypes();
    }

    onGridReady(evt) {
        this.gridApi = evt.api;
        this.gridColumnApi = evt.columnApi;
    }
    resizeColumns(evt) {
        evt.columnApi.autoSizeAllColumns();
    }

    logout() {
        this.astuteClientService.logout().then((data) => {
            if (data) {
                this.router.navigate(['/login']);
                this.notif('Logout successful');
            } else {
                this.notif('Logout unsuccessful');
            }
        });
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }
}
