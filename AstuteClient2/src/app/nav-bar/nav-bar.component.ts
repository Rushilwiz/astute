import {Component, Input, OnInit} from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {Router} from "@angular/router";
import {ToastManagerService} from "../services/toast-manager/toast-service.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
    @Input() customerActive: boolean;
    @Input() salesOrderActive: boolean;
    @Input() invoiceActive: boolean;
    @Input() invoicePaymentActive: boolean;
    // @Input() serviceTypeActive: boolean;
    // @Input() logoffActive: boolean;
    @Input() settingsActive: boolean;

    loggedIn: boolean;

    constructor(private astuteClientService: AstuteClientService,  private router: Router, private toastService: ToastManagerService) {
    }

    ngOnInit() {
        if (localStorage.getItem('SESSION_ID') && localStorage.getItem('SESSION_USER')) {
            this.loggedIn = true;
        } else {
            this.loggedIn = false;
        }
    }

    logout() {
        this.notif('Logging Out...');
        this.astuteClientService.logout().then((data) => {
            if (data) {
                this.router.navigate(['/login']);
                this.notif('Logout Successful');
            } else {
                this.notif('Logout Unsuccessful');
            }
        });
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }
}

