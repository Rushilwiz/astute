import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ToastManagerService} from '../../services/toast-manager/toast-service.service';

@Component({
  selector: 'app-empty-error-editor',
  template: `
    <input #i class="w-100" [value]="params.value" (keypress)="onKeyPress($event)" (keydown)="onKeyDown($event)"/>
  `
})
export class EmptyErrorEditorComponent implements AfterViewInit {
  @ViewChild('i', {'static': false}) textInput;
  params;

  constructor(protected toastService: ToastManagerService) { }

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

  onKeyPress(event) {
    if (event.key === '\'') {
      event.preventDefault();
      this.notif('Cannot use \' (apostrophes)');
      setTimeout(() => {
        this.textInput.nativeElement.focus();
      });
    }
  }

  onKeyDown(event) {
    if (event.keyCode === 9 || event.keyCode === 13) {
      if (!this.textInput.nativeElement.value) {
        event.stopPropagation();
        this.notif('Value should not be empty');
        setTimeout(() => {
          this.textInput.nativeElement.focus();
        });
      }
    }
  }

  // ** toast notification method
  notif(text: string) {
    this.toastService.show(text, {classname: 'bg-warning'});
  }
}
