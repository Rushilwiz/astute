import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ToastManagerService} from '../../services/toast-manager/toast-service.service';

@Component({
  selector: 'app-phone-editor-cell',
  template: `
    <input #i class="w-100" [value]="params.value" [textMask]="{mask: usPhoneMask, guide: false}" (keydown)="onKeyDown($event)"/>
  `
})
export class PhoneEditorComponent implements AfterViewInit {
    @ViewChild('i', {'static': false}) textInput;
    usPhoneMask = ['(', /[1-9]/, /\d/, /\d/, ')', ' ', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
    params;

    constructor(protected toastService: ToastManagerService) {}

    ngAfterViewInit() {
        setTimeout(() => {
            this.textInput.nativeElement.focus();
        });
    }

    agInit(params: any): void {
        this.params = params;
    }

    getValue() {
        return this.textInput.nativeElement.value;
    }

    onKeyDown(event) {
      if (event.keyCode === 9 || event.keyCode === 13) {
        if (this.textInput.nativeElement.value.length < 14) {
            event.stopPropagation();
            this.notif('Phone number\'s should have 10 digits');
        }
      }
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-warning'});
    }
}
