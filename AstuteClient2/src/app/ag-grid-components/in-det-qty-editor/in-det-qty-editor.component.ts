import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ToastManagerService} from '../../services/toast-manager/toast-service.service';

@Component({
    selector: 'app-in-det-qty-editor',
    template: `
      <input #i type="number" min="0" class="w-100" [step]="remainingQty/100" [value]="params.value" (keydown)="onKeyDown($event)"/>
    `
})
export class InDetQtyEditorComponent implements AfterViewInit {
    @ViewChild('i', {'static': false}) numberInput;
    params;
    remainingQty: number;

    constructor(protected toastService: ToastManagerService) { }

    ngAfterViewInit() {
        setTimeout(() => {
            this.numberInput.nativeElement.focus();
        });
    }

    agInit(params: any): void {
        this.params = params;
        this.remainingQty = params.remainingQty + params.value;
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
            } else if (!(this.numberInput.nativeElement.value >= 0
                && parseInt((this.numberInput.nativeElement.value * 100000).toFixed(0), 10) -
                   parseInt((this.remainingQty * 100000).toFixed(0), 10) <= 1)) {
                event.stopPropagation();
                this.notif('Value should between 0 and ' + this.remainingQty.toPrecision(2));
                setTimeout(() => {
                    this.numberInput.nativeElement.focus();
                });
            }
        }
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-warning'});
    }
}
