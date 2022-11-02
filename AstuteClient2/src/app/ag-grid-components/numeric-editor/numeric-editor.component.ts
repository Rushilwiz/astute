import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ToastManagerService} from '../../services/toast-manager/toast-service.service';

@Component({
    selector: 'app-numeric-editor',
    template: `
      <input #i type="number" class="w-100" [value]="params.value" (keydown)="onKeyDown($event)"/>
    `
})
export class NumericEditorComponent implements AfterViewInit {
    @ViewChild('i', {'static': false}) numberInput;
    params;
    canBeEmpty: boolean;

    constructor(protected toastService: ToastManagerService) { }

    ngAfterViewInit() {
        setTimeout(() => {
            this.numberInput.nativeElement.focus();
        });
    }

    agInit(params: any): void {
        this.params = params;
        this.canBeEmpty = params.canBeEmpty;
    }

    getValue() {
        return this.numberInput.nativeElement.value;
    }

    onKeyDown(event) {
        if (this.canBeEmpty) {
            if (event.keyCode === 9 || event.keyCode === 13) {
                if (!this.numberInput.nativeElement.value) {
                    event.stopPropagation();
                    this.notif('Value should not be empty');
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