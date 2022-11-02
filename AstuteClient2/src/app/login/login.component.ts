import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {AstuteClientService} from '../services/astute-client-service';
import {ToastManagerService} from "../services/toast-manager/toast-service.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {

    constructor(protected astuteClientService: AstuteClientService, protected toastService: ToastManagerService,
                private router: Router) {}

    ngOnInit() {
        localStorage.removeItem('SESSION_ID');
        localStorage.removeItem('SESSION_USER');
    }

    login(form: NgForm) {
        const username = form.value.username;
        const password = form.value.password;

        this.astuteClientService.login(username, password).then((data) => {
            if (data) {
                this.router.navigate(['/homepage']);
            } else {
                this.notif('login failed, checked credentials');
            }
        });
    }

    // ** toast notification method
    notif(text: string) {
        this.toastService.show(text, {classname: 'bg-danger text-light'});
    }

}
