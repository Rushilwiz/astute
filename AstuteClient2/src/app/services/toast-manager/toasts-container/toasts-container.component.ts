import {Component, TemplateRef} from '@angular/core';
import {ToastManagerService} from '../toast-service.service';

@Component({
  selector: 'app-toasts-container',
  templateUrl: './toasts-container.component.html',
  styleUrls: ['./toasts-container.component.css']
})
export class ToastsContainerComponent {
    constructor(public toastService: ToastManagerService) {}
    isTemplate(toast) { return toast.textOrTpl instanceof TemplateRef; }
}
