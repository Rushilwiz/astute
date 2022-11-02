import { Component } from '@angular/core';

@Component({
    selector: 'app-so-qty-formatter',
    template: `
        <div *ngIf="feeTypeId === 1">
            <span *ngIf="feeTypeId === 1">{{(params.value * 100).toFixed(0)}} </span>
            <span style="color: darkgrey">%</span>
        </div>
        <div *ngIf="feeTypeId === 2">
            <span *ngIf="feeTypeId === 2">{{params.value}} </span>
            <span style="color: darkgrey">hours</span>
        </div>
    `
})
export class SoQtyFormatterComponent {
    params: any;
    feeTypeId: number;

    agInit(params: any): void {
        this.params = params;
        this.feeTypeId = params.feeTypeId;
    }
}