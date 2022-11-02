import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ToastManagerService} from '../../services/toast-manager/toast-service.service';

@Component({
    selector: 'app-so-qty-editor',
    template: `
      <input #i *ngIf="feeTypeId === 1" type="number" min="0" [max]="1" step="0.1"
             class="w-100" [value]="params.value" (keydown)="onKeyDown($event)"/>
      <input #i *ngIf="feeTypeId === 2" type="number" min="0" step="1"
             class="w-100" [value]="params.value" (keydown)="onKeyDown($event)"/>
    `
})
export class SoQtyEditorComponent implements AfterViewInit {
    @ViewChild('i', {'static': false}) numberInput;
    params;
    feeTypeId: number;

    constructor(protected toastService: ToastManagerService) { }

    ngAfterViewInit() {
        setTimeout(() => {
            this.numberInput.nativeElement.focus();
        });
    }

    agInit(params: any): void {
        this.params = params;
        this.feeTypeId = params.feeTypeId;
    }

    getValue() {
        return this.numberInput.nativeElement.value;
    }

    onKeyDown(event) {
        if (event.keyCode === 9 || event.keyCode === 13) {
            if (!this.numberInput.nativeElement.value) {
                event.stopPropagation();
                this.notif('Value should not be empty');
                setTimeout(() => {
                    this.numberInput.nativeElement.focus();
                });
            } else if (this.feeTypeId === 1) {
                if (this.numberInput.nativeElement.value < 0 || this.numberInput.nativeElement.value > 1) {
                    event.stopPropagation();
                    this.notif('Value should be between 0 and 1');
                    setTimeout(() => {
                        this.numberInput.nativeElement.focus();
                    });
                }
            } else if (this.feeTypeId === 2) {
                if (this.numberInput.nativeElement.value < 0) {
                    event.stopPropagation();
                    this.notif('Value should less than 0');
                    setTimeout(() => {
                        this.numberInput.nativeElement.focus();
                    });
                }
            }
        }
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-warning'});
    }
}
