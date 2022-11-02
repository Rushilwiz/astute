import {Injectable, TemplateRef} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToastManagerService {
  toasts: any[] = [];
  show(textOrTpl: string, options: any = {}) {
      const words = textOrTpl.split(' ').length;
      this.toasts.push({ textOrTpl, delay: (words + 1) * 1000, ...options });
  }

  remove(toast) {
      this.toasts = this.toasts.filter(t => t !== toast);
  }
}
